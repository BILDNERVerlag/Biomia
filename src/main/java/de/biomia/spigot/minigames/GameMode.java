package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.bedwars.BedWarsTeam;
import de.biomia.spigot.minigames.general.TeamSwitcher;
import de.biomia.spigot.minigames.general.Teleport;
import de.biomia.spigot.minigames.versus.Versus;
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

public abstract class GameMode {

    private static Location spawn = new Location(Bukkit.getWorld("Spawn"), 0.5, 75, -0.5, 45, 0);
    private MinigamesConfig config;
    protected Inventory teamSwitcher;
    private final GameInstance instance;
    private final ArrayList<GameTeam> teams = new ArrayList<>();
    private GameStateManager stateManager = new GameStateManager(this);
    private GameHandler handler;
    private final HashMap<TeamColor, Entity> joiner = new HashMap<>();

    protected GameMode(GameInstance instance) {

        this.instance = instance;
    }

    public GameTeam getTeamFromData(short data) {
        for (GameTeam te : teams) {
            if (te.getColordata() == data) {
                return te;
            }
        }
        return null;
    }

    protected void splitPlayersInTwoTeams() {
        boolean b = true;
        for (BiomiaPlayer bp : getInstance().getPlayers()) {
            if (b)
                getTeams().get(0).join(bp);
            else
                getTeams().get(1).join(bp);
            b = !b;
        }
    }

    public void registerTeam(GameTeam team) {
        teams.add(team);
    }

    public void start() {
        stateManager.getLobbyState().start();
        TeamSwitcher.getTeamSwitcher(this);
        config.loadTeamJoiner();
    }

    public void stop() {
        Bukkit.broadcastMessage("%%%stop");
        Teleport.teleportAllToWarteLobby(spawn, instance.getPlayers());
        handler.unregister();
        instance.startDeleting();
        stateManager.getInGameState().stop();
        Bukkit.broadcastMessage("%%%stop2");
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

    public void initTeams() {

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

    public void setConfig() {
        this.config = initConfig();
    }

    protected abstract GameHandler initHandler();

    public void setHandler() {
        this.handler = initHandler();
    }

    public HashMap<TeamColor, Entity> getJoiner() {
        return joiner;
    }

    public static Location getSpawn() {
        return spawn;
    }

    public GameHandler getHandler() {
        return handler;
    }
}
