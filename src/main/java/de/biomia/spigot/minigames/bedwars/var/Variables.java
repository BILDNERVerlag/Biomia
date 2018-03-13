package de.biomia.spigot.minigames.bedwars.var;

import de.biomia.spigot.configs.Config;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Variables {

    //Game
    public static final ArrayList<Block> destroyableBlocks = new ArrayList<>();
    public static final String name = Config.getConfig().getString("Name");
    public static final int teams = Config.getConfig().getInt("NumberOfTeams");
    public static final int playerPerTeam = Config.getConfig().getInt("TeamSize");
    public static final int maxPlayers = playerPerTeam * teams;
    public static final int minPlayers = playerPerTeam + 1;
    // Locs
    public static final Location warteLobbySpawn = new Location(Bukkit.getWorld("Spawn"), 0.5, 75, -0.5, 45, 0);
    public static final HashMap<Sign, Integer> signLocations = new HashMap<>();
    // Spawner
    public static final HashMap<UUID, ArrayList<Player>> handlerMap = new HashMap<>();
    // Joiner
    public static final HashMap<TeamColor, Entity> joiner = new HashMap<>();
    public static final ItemStack teamChest = ItemCreator.itemCreate(Material.ENDER_CHEST, BedWarsItemNames.teamChest);
    public static final HashMap<GameTeam, Inventory> teamChests = new HashMap<>();
    public static final HashMap<GameTeam, ArrayList<Block>> teamChestsLocs = new HashMap<>();

    public static GameTeam getTeamByTeamChests(Block block) {
        for (GameTeam team : BedWars.getBedWars().getTeams()) {
            if (teamChestsLocs.containsKey(team)) {
                for (Block b : teamChestsLocs.get(team)) {
                    if (block.equals(b)) {
                        return team;
                    }
                }
            }
        }
        return null;
    }

    public static BlockFace getDirection(Player p) {
        float yaw = p.getLocation().getYaw();
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            return BlockFace.SOUTH;
        } else if (yaw < 135) {
            return BlockFace.WEST;
        } else if (yaw < 225) {
            return BlockFace.NORTH;
        } else if (yaw < 315) {
            return BlockFace.EAST;
        }
        return BlockFace.NORTH;
    }
}
