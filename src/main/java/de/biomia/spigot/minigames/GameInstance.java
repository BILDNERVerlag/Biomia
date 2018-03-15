package de.biomia.spigot.minigames;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.minigames.versus.games.bedwars.VersusBedWars;
import de.biomia.spigot.minigames.versus.games.kitpvp.KitPvP;
import de.biomia.spigot.minigames.versus.games.skywars.VersusSkyWars;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;

public class GameInstance implements Listener {

    private final GameType type;
    private World world;
    private GameMode gameMode;
    private int playedTime = 0;
    private final String mapDisplayName;
    private final ArrayList<BiomiaPlayer> players = new ArrayList<>();
    private final int teamSize;
    private final int teamAmount;

    public GameInstance(GameType type, String mapDisplayName, int teamAmount, int teamSize) {
        Bukkit.broadcastMessage("%%% making a new gameInstance");
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
        this.teamAmount = teamAmount;
        this.teamSize = teamSize;
        this.type = type;
        this.world = new WorldCreator(mapDisplayName).createWorld();
        this.mapDisplayName = mapDisplayName;
        switch (type) {
        case KIT_PVP_VS:
            gameMode = new KitPvP(this);
            break;
        case BED_WARS_VS:
            gameMode = new VersusBedWars(this);
            break;
        case SKY_WARS_VS:
            gameMode = new VersusSkyWars(this);
            break;
        case BED_WARS:
            gameMode = new BedWars(this);
            break;
        }
        gameMode.setConfig(gameMode.initConfig());
        gameMode.initTeams();

        new BukkitRunnable() {
            @Override
            public void run() {
                TimoCloudAPI.getBukkitInstance().getThisServer().setExtra(String.format("§b%d §7x §c%d", teamAmount, teamSize));
            }
        }.runTaskLater(Main.getPlugin(), 15);
    }

    public GameInstance(GameType type, World world, String mapDisplayName, int teamAmount, int teamSize) {
        this(type, mapDisplayName, teamAmount, teamSize);
        this.world = world;
    }

    public void registerPlayer(BiomiaPlayer bp) {
        players.add(bp);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public ArrayList<BiomiaPlayer> getPlayers() {
        return players;
    }

    public World getWorld() {
        return world;
    }

    void startDeleting() {
        Bukkit.unloadWorld(world, false);
        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException ignored) {
        }
    }

    public int getTeamSize() {
        return teamSize;
    }

    public GameType getType() {
        return type;
    }

    public void incPlayTime() {
        playedTime++;
    }

    public boolean containsPlayer(BiomiaPlayer bp) {
        return getPlayers().contains(bp);
    }

    public String getMapDisplayName() {
        return mapDisplayName;
    }

    public int getPlayedTime() {
        return playedTime;
    }

    public int getTeamAmount() {
        return teamAmount;
    }

    //LISTENER

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (getGameMode().getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {
            getGameMode().getInstance().registerPlayer(Biomia.getBiomiaPlayer(e.getPlayer()));
        }
    }
}
