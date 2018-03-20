package de.biomia.spigot.minigames;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.bedwars.Variables;
import de.biomia.spigot.minigames.general.CountDown;
import de.biomia.spigot.minigames.general.Scoreboards;
import de.biomia.spigot.minigames.general.Teleport;
import de.biomia.spigot.tools.PlayerToServerConnector;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameStateManager {

    public enum GameState {
        LOBBY, INGAME, WAITING_FOR_START, END
    }

    private LobbyState lobbyState;
    private InGameState inGameState;
    private EndState endState;
    private GameState actualGameState = GameState.LOBBY;

    private final GameMode mode;

    GameStateManager(GameMode mode) {
        this.mode = mode;
    }

    public GameState getActualGameState() {
        return actualGameState;
    }

    public void setLobbyState(LobbyState lobbyState) {
        this.lobbyState = lobbyState;
    }

    public void setInGameState(InGameState inGameState) {
        this.inGameState = inGameState;
    }

    public void setActualGameState(GameState actualGameState) {
        this.actualGameState = actualGameState;
    }

    public LobbyState getLobbyState() {
        return lobbyState != null ? lobbyState : (lobbyState = new LobbyState(mode));
    }

    InGameState getInGameState() {
        return inGameState != null ? inGameState : (inGameState = new InGameState(mode));
    }

    public EndState getEndState() {
        return endState != null ? endState : (endState = new EndState(mode));
    }

    private static abstract class State {

        private final GameMode mode;

        State(GameMode mode) {
            this.mode = mode;
        }

        public GameMode getMode() {
            return mode;
        }

        abstract void start();

        abstract void stop();

    }

    public static class LobbyState extends State {

        private final CountDown countDown;

        protected LobbyState(GameMode mode) {
            super(mode);
            this.countDown = new CountDown(mode);
        }

        public void start() {
            Scoreboards.initLobbySB(getMode());
            countDown.startCountDown();
            new BukkitRunnable() {
                @Override
                public void run() {
                    TimoCloudAPI.getBukkitInstance().getThisServer().setExtra(String.format(MinigamesMessages.mapSize, Variables.teams + "", Variables.playerPerTeam + ""));
                }
            }.runTaskLater(Main.getPlugin(), 20);

        }

        public void stop() {
            countDown.cancel();
            getMode().setAllToTeams();
            getMode().getStateManager().getInGameState().start();
        }

        public void setCountDown(int sec) {
            countDown.setCountdown(sec);
        }

        public int getCountDown() {
            return countDown.getCountdown();
        }
    }

    public static class InGameState extends State {

        protected InGameState(GameMode mode) {
            super(mode);
        }

        private BukkitTask clock;

        public void start() {

            clock = new BukkitRunnable() {
                @Override
                public void run() {
                    getMode().getInstance().incPlayTime();
                }
            }.runTaskTimer(Main.getPlugin(), 0, 20);

            getMode().getStateManager().setActualGameState(GameState.INGAME);
            TimoCloudAPI.getBukkitInstance().getThisServer().setState(GameState.INGAME.name());
            for (BiomiaPlayer bp : getMode().getInstance().getPlayers()) {

                for (BiomiaPlayer p2 : getMode().getInstance().getPlayers()) {
                    bp.getPlayer().showPlayer(p2.getPlayer());
                }

                bp.setBuild(true);
                bp.setDamageEntitys(true);
                bp.setGetDamage(true);
                bp.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
                Scoreboards.setInGameScoreboard(bp.getPlayer(), GameType.BED_WARS);
                bp.getPlayer().getInventory().clear();
                bp.getPlayer().setFlying(false);
                bp.getPlayer().setAllowFlight(false);
            }

            Scoreboards.initSpectatorSB(getMode());
            Teleport.teleportPlayerToMap(getMode());
        }

        public void stop() {

            clock.cancel();
            getMode().getStateManager().setActualGameState(GameState.END);
            getMode().getStateManager().getEndState().start();
        }
    }

    public static class EndState extends State {

        EndState(GameMode mode) {
            super(mode);
        }

        @Override
        public void start() {

            for (Player p : Bukkit.getOnlinePlayers()) {
                Biomia.getBiomiaPlayer(p).setBuild(false);
                Biomia.getBiomiaPlayer(p).setDamageEntitys(false);
                Biomia.getBiomiaPlayer(p).setGetDamage(false);
            }

            Teleport.teleportAllToWarteLobby(Variables.warteLobbySpawn);

            new BukkitRunnable() {
                int i = 15;

                @Override
                public void run() {
                    while (i >= 0) {
                        if (i == 15) {
                            Bukkit.broadcastMessage(Messages.PREFIX + MinigamesMessages.restartCountDown.replaceAll("%t", i + ""));
                        } else if (i == 10) {
                            Bukkit.broadcastMessage(Messages.PREFIX + MinigamesMessages.restartCountDown.replaceAll("%t", i + ""));
                        } else if (i <= 5 && i != 0) {
                            Bukkit.broadcastMessage(Messages.PREFIX + MinigamesMessages.restartCountDown.replaceAll("%t", i + ""));
                        } else if (i == 0) {
                            stop();
                            return;
                        }
                        i--;
                    }

                }
            }.runTaskTimer(Main.getPlugin(), 0, 20);
        }

        @Override
        void stop() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                PlayerToServerConnector.connectToRandom(p, BiomiaServerType.Lobby);
            }
            Bukkit.shutdown();
        }
    }

}