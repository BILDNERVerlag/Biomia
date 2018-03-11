package de.biomia.spigot.minigames.skywars.var;

import de.biomia.spigot.configs.Config;
import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.general.kits.Kit;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Variables {

    // Message
    public static final String prefix = Messages.PREFIX;
    public static final String name = Config.getConfig().getString("Name");
    public static final int teams = Config.getConfig().getInt("NumberOfTeams");
    public static final int playerPerTeam = Config.getConfig().getInt("TeamSize");
    // Locations
    public static final Location warteLobbySpawn = new Location(Bukkit.getWorld("Spawn"), 0.5, 75, -0.5, 45, 0);
    public static final HashMap<Sign, Integer> signLocations = new HashMap<>();
    public static final HashMap<GameTeam, Location> teamSpawns = new HashMap<>();
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
    // Items
    public static final ItemStack kitItem = ItemCreator.itemCreate(Material.CHEST, SkyWarsItemNames.kitItemName);
    // Joiner
    public static final HashMap<TeamColor, Entity> joiner = new HashMap<>();
    // Setup
    public static boolean chestAddMode = false;

    // Stats
    public static HashMap<Chest, ItemStack[]> normalChestsFill;
    public static HashMap<Chest, ItemStack[]> goodChestsFill;
    public static Kit standardKit;
}
