package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.bedwars.BedWarsTeam;
import de.biomia.spigot.minigames.general.TeamSwitcher;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public abstract class GameMode {

    private static final Location spawn = new Location(Bukkit.getWorld("Spawn"), 0.5, 75, -0.5, 45, 0);
    private static final Location versusSpawn = new Location(Bukkit.getWorld("Spawn"), -34.5, 85, -113.5, 0, 0);
    private MinigamesConfig config;
    protected Inventory teamSwitcher;
    private final GameInstance instance;
    private final ArrayList<GameTeam> teams = new ArrayList<>();
    private final GameStateManager stateManager = new GameStateManager(this);
    private GameHandler handler;
    private final HashMap<TeamColor, Entity> joiner = new HashMap<>();

    protected GameMode(GameInstance instance) {
        this.instance = instance;
        setConfig();
        setHandler();
        initTeams();
    }

    public GameTeam getTeamFromData(short data) {
        for (GameTeam te : teams) {
            if (te.getColordata() == data) {
                return te;
            }
        }
        return null;
    }

    public void registerTeam(GameTeam team) {
        teams.add(team);
    }

    public void start() {
        stateManager.getLobbyState().start();
        TeamSwitcher.getTeamSwitcher(this);
        config.loadTeamJoiner(initTeamJoiner());
    }

    public void stop() {
        handler.unregister();
        stateManager.getInGameState().stop();
    }

    public GameStateManager getStateManager() {
        return stateManager;
    }

    public GameInstance getInstance() {
        return instance;
    }

    public boolean canStop() {
        int teamsWhoLive = 0;
        for (GameTeam team : getTeams()) {
            for (BiomiaPlayer players : team.getPlayers()) {
                if (team.lives(players)) {
                    teamsWhoLive++;
                    break;
                }
            }
        }
        return teamsWhoLive <= 1;
    }

    public ArrayList<GameTeam> getTeams() {
        return teams;
    }

    public void setAllToTeams() {
        Iterator<BiomiaPlayer> l = getInstance().getPlayers().iterator();
        for (GameTeam team : getTeams()) {
            while (!team.isFull()) {
                if (l.hasNext()) {
                    BiomiaPlayer bp = l.next();
                    if (bp.getTeam() == null)
                        team.join(bp);
                } else {
                    return;
                }
            }
        }
    }

    public void partyJoin(BiomiaPlayer bp) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (bp.isPartyLeader()) {
                    ArrayList<BiomiaPlayer> onlineParty = new ArrayList<>();

                    for (PAFPlayer paf : bp.getParty().getAllPlayers()) {
                        Player target = Bukkit.getPlayer(paf.getUniqueId());
                        if (target != null) {
                            onlineParty.add(Biomia.getBiomiaPlayer(target));
                        }
                    }
                    if (onlineParty.size() > getInstance().getTeamSize()) {
                        for (BiomiaPlayer p : onlineParty) {
                            p.sendMessage(MinigamesMessages.noFittingTeamParty);
                            cancel();
                        }
                    } else {
                        for (GameTeam t : getTeams()) {
                            if (getInstance().getTeamSize() - t.getPlayers().size() >= onlineParty.size()) {
                                for (BiomiaPlayer p : onlineParty)
                                    t.join(p);
                                return;
                            }
                        }
                        for (BiomiaPlayer p : onlineParty) {
                            p.sendMessage(MinigamesMessages.noFittingTeamPlayer);
                        }
                    }
                }
            }
        }.runTaskLater(Main.getPlugin(), 20);
    }

    public Inventory getTeamSwitcher() {
        return teamSwitcher;
    }

    private void initTeams() {

        for (TeamColor colors : TeamColor.values()) {
            if (colors.getID() > getInstance().getTeamAmount())
                continue;

            switch (getInstance().getType()) {
                case BED_WARS:
                    new BedWarsTeam(colors, this);
                    break;
                default:
                case SKY_WARS:
                    new GameTeam(colors, this);
                    break;
            }


        }
    }

    protected abstract MinigamesConfig initConfig();

    public MinigamesConfig getConfig() {
        return config;
    }

    private void setConfig() {
        this.config = initConfig();
    }

    protected abstract GameHandler initHandler();

    protected abstract HashMap<TeamColor, UUID> initTeamJoiner();

    private void setHandler() {
        this.handler = initHandler();
    }

    public HashMap<TeamColor, Entity> getJoiner() {
        return joiner;
    }

    public static Location getSpawn(boolean isVersus) {
        if (isVersus)
            return versusSpawn;
        return spawn;
    }

    public GameHandler getHandler() {
        return handler;
    }

    public boolean isSpectator(BiomiaPlayer bp) {
        return !getInstance().containsPlayer(bp) && !(bp.getTeam() != null && bp.getTeam().lives(bp)) && bp.getPlayer().getWorld().equals(getInstance().getWorld());
    }
}
