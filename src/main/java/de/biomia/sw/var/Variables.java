package de.biomia.sw.var;

import de.biomia.sw.kits.Kit;
import de.biomia.sw.listeners.CountDown;
import de.biomia.sw.messages.ItemNames;
import de.biomia.api.Teams.Team;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
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
    // Chest System
    public static final int minimaleBelegteSlots = 7;
    public static final int maximalBelegteSlots = 9;
    public static final ArrayList<Chest> goodChests = new ArrayList<>();
    public static final ArrayList<Chest> normalChests = new ArrayList<>();
    public static final HashMap<ItemStack, Integer> normalItems = new HashMap<>();
    public static final HashMap<ItemStack, Integer> goodItems = new HashMap<>();
    public static final ArrayList<Location> opendChests = new ArrayList<>();
    // Kits
    public static final HashMap<Integer, Kit> kits = new HashMap<>();
    public static final HashMap<Player, ArrayList<Kit>> availableKits = new HashMap<>();
    public static final HashMap<Player, Kit> selectedKit = new HashMap<>();
    public static final int playReward = 60;
    // Items
    public static final ItemStack kitItem = ItemCreator.itemCreate(Material.CHEST, ItemNames.kitItemName);
    // Joiner
    public static final HashMap<Team, UUID> joiner = new HashMap<>();
    // Setup
    public static boolean chestAddMode = false;
    // Game
    public static CountDown countDown;
    public static boolean end = false;

    // Stats
//	public static ArrayList<Stats> stats = new ArrayList<>();
    public static HashMap<Chest, ItemStack[]> normalChestsFill;
    public static HashMap<Chest, ItemStack[]> goodChestsFill;
    public static Kit standardKit;
    // Rewards
    public static int winReward = 300;
    public static int killReward = 100;
    // Inventorys
    public static Inventory teamJoiner;

}
