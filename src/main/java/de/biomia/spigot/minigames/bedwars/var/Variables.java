package de.biomia.spigot.minigames.bedwars.var;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.minigames.bedwars.listeners.CountDown;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.minigames.general.teams.Team;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
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
    // Player
    public static final ArrayList<Player> spectator = new ArrayList<>();
    public static final ArrayList<Player> livingPlayer = new ArrayList<>();
    // Locs
    public static final Location warteLobbySpawn = new Location(Bukkit.getWorld("Spawn"), 0.5, 75, -0.5, 45, 0);
    public static final HashMap<Sign, Integer> signLocations = new HashMap<>();
    public static final HashMap<Team, Location> teamSpawns = new HashMap<>();
    // Beds
    public static final HashMap<Team, ArrayList<Location>> beds = new HashMap<>();
    public static final ArrayList<Team> teamsWithBeds = new ArrayList<>();
    // Spawner
    public static final HashMap<ItemType, ArrayList<Location>> spawner = new HashMap<>();
    public static final HashMap<UUID, ArrayList<Player>> handlerMap = new HashMap<>();
    // Joiner
    public static final HashMap<Team, UUID> joiner = new HashMap<>();
    public static final ItemStack teamChest = ItemCreator.itemCreate(Material.ENDER_CHEST, BedWarsItemNames.teamChest);
    public static final HashMap<Team, Inventory> teamChests = new HashMap<>();
    public static final HashMap<Team, ArrayList<Block>> teamChestsLocs = new HashMap<>();
    public static CountDown countDown;
    // Inventorys
    public static Inventory teamJoiner;

    public static Team getTeamByTeamChests(Block block) {
        for (Team team : Biomia.getTeamManager().getTeams()) {
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
