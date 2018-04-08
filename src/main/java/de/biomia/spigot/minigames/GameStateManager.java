package de.biomia.spigot.minigames;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.events.game.GameEndEvent;
import de.biomia.spigot.events.game.GameStartEvent;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.general.CountDown;
import de.biomia.spigot.minigames.general.Scoreboards;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

    private EndState getEndState() {
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

        LobbyState(GameMode mode) {
            super(mode);
            this.countDown = new CountDown(mode);
        }

        public void start() {
            if (!getMode().getInstance().getType().isVersus()) {
                Scoreboards.initLobbySB(getMode());
                countDown.startCountDown();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        TimoCloudAPI.getBukkitInstance().getThisServer().setExtra(String.format(MinigamesMessages.mapSize, MinigamesConfig.getTeamAmount() + "", MinigamesConfig.getTeamSize() + ""));
                    }
                }.runTaskLater(Main.getPlugin(), 20);
            } else {
                stop();
            }
        }

        public void stop() {
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

        public void start() {
            getMode().getStateManager().setActualGameState(GameState.INGAME);

            if (!getMode().getInstance().getType().isVersus())
                TimoCloudAPI.getBukkitInstance().getThisServer().setState(GameState.INGAME.name());
            getMode().getInstance().setPlayersOnStart();
            for (BiomiaPlayer bp : getMode().getInstance().getPlayers()) {
                for (BiomiaPlayer p2 : getMode().getInstance().getPlayers()) {
                    bp.getPlayer().showPlayer(Main.getPlugin(), p2.getPlayer());
                }

                bp.setDamageEntitys(true);
                bp.setGetDamage(true);
                bp.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
                Scoreboards.setInGameScoreboard(bp.getPlayer(), getMode().getInstance().getType());
                bp.getPlayer().getInventory().clear();
                bp.getPlayer().setFlying(false);
                bp.getPlayer().setAllowFlight(false);
                bp.setBuild(true);

                bp.getPlayer().setFallDistance(0);
                bp.getPlayer().teleport(bp.getTeam().getHome());
                bp.sendMessage(MinigamesMessages.explainMessages);
            }

            Bukkit.getPluginManager().callEvent(new GameStartEvent(getMode()));
            Scoreboards.initSpectatorSB(getMode());
        }

        public void stop() {
            Bukkit.getPluginManager().callEvent(new GameEndEvent(getMode()));
            getMode().getStateManager().setActualGameState(GameState.END);
            getMode().getStateManager().getEndState().start();
        }
    }

    static class EndState extends State {

        EndState(GameMode mode) {
            super(mode);
        }

        @Override
        void start() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
                if (!getMode().getInstance().getWorld().equals(p.getWorld()))
                    continue;
                bp.setBuild(false);
                bp.setDamageEntitys(false);
                bp.setGetDamage(false);
                p.setGameMode(org.bukkit.GameMode.ADVENTURE);
                if (bp.getTeam() != null)
                    bp.getTeam().leave(bp);

                if (getMode().getInstance().getType().isVersus())
                    Versus.getInstance().getManager().moveToLobby(p, false);
                else
                    p.teleport(GameMode.getSpawn(false));
            }
            if (!getMode().getInstance().getType().isVersus()) {
                new BukkitRunnable() {
                    int i = 15;
                    @Override
                    public void run() {
                        if (i == 0) {
                            stop();
                            cancel();
                            return;
                        } else if (i == 15 || i == 10 || i <= 5)
                            Bukkit.broadcastMessage(Messages.PREFIX + MinigamesMessages.restartCountDown.replaceAll("%t", i + ""));
                        i--;
                    }
                }.runTaskTimer(Main.getPlugin(), 0, 20);
            } else {
                getMode().getInstance().deleteWorld();
            }
        }

        @Override
        void stop() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.kickPlayer("");
            }
            Bukkit.shutdown();
        }
    }

}