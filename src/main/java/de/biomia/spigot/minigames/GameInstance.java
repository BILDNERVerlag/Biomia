package de.biomia.spigot.minigames;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.minigames.kitpvp.KitPvP;
import de.biomia.spigot.minigames.skywars.SkyWars;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class GameInstance {

    private final GameType type;
    private final World world;
    private GameMode gameMode;
    private final ArrayList<BiomiaPlayer> players = new ArrayList<>();
    private final int teamSize;
    private final int teamAmount;
    private int playersOnStart = 0;
    private final String mapDisplayName;

    public GameInstance(GameType type, String mapDisplayName, String mapName, int teamAmount, int teamSize) {
        this.teamAmount = teamAmount;
        this.teamSize = teamSize;
        this.type = type;
        this.mapDisplayName = mapDisplayName;
        this.world = new WorldCreator(mapName).createWorld();
        world.setGameRuleValue("announceAdvancements", "false");
        world.setGameRuleValue("keepInventory", "true");
        MinigamesConfig.mapName = mapDisplayName;

        switch (type) {
        case KIT_PVP_VS:
            gameMode = new KitPvP(this);
            break;
        case BED_WARS_VS:
        case BED_WARS:
            gameMode = new BedWars(this);
            break;
        case SKY_WARS_VS:
        case SKY_WARS:
            gameMode = new SkyWars(this);
            break;
        default:
            Bukkit.getLogger().log(Level.SEVERE, "GameType does not exist!");
            new Exception().printStackTrace();
            break;
        }
        if (!type.isVersus())
            new BukkitRunnable() {
                @Override
                public void run() {
                    TimoCloudAPI.getBukkitAPI().getThisServer().setExtra(String.format("\u00A7b%d \u00A77x \u00A7c%d", teamAmount, teamSize));
                }
            }.runTaskLater(Main.getPlugin(), 15);
    }

    public void registerPlayer(BiomiaPlayer bp) {
        players.add(bp);
    }

    public void removePlayer(BiomiaPlayer bp) {
        players.remove(bp);
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

    void deleteWorld() {
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

    public boolean containsPlayer(BiomiaPlayer bp) {
        return getPlayers().contains(bp);
    }

    public int getTeamAmount() {
        return teamAmount;
    }

    public void setPlayersOnStart() {
        this.playersOnStart = players.size();
    }

    public String getMapDisplayName() {
        return mapDisplayName;
    }

    public int getPlayersOnStart() {
        return playersOnStart;
    }

}
