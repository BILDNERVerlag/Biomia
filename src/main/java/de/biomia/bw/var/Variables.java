package de.biomia.bw.var;

import de.biomia.bw.listeners.CountDown;
import de.biomia.bw.messages.ItemNames;
import de.biomia.api.Biomia;
import de.biomia.api.Teams.Team;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.msg.Messages;
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

    // Message
    public static final String prefix = Messages.PREFIX;

    // Game
    public static final ArrayList<Block> destroyableBlocks = new ArrayList<>();
    public static final String name = Config.config.getString("Name");
    public static final int teams = Config.config.getInt("NumberOfTeams");
    public static final int playerPerTeam = Config.config.getInt("TeamSize");
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
    public static final int bronzeSpawnDelay = 1;
    public static final int ironSpawnDelay = 10;
    public static final int goldSpawnDelay = 30;
    public static final HashMap<UUID, ArrayList<Player>> handlerMap = new HashMap<>();
    // Joiner
    public static final HashMap<Team, UUID> joiner = new HashMap<>();
    public static final ItemStack teamChest = ItemCreator.itemCreate(Material.ENDER_CHEST, ItemNames.teamChest);
    public static final HashMap<Team, Inventory> teamChests = new HashMap<>();
    public static final HashMap<Team, ArrayList<Block>> teamChestsLocs = new HashMap<>();
    public static CountDown countDown;
    // Inventorys
    public static Inventory teamJoiner;

    public static Team getTeamByTeamChests(Block block) {
        for (Team team : Biomia.TeamManager().getTeams()) {
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
