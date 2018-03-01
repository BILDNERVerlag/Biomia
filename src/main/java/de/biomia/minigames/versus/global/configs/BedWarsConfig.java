package de.biomia.minigames.versus.global.configs;

import de.biomia.minigames.versus.bw.var.ItemType;
import de.biomia.api.main.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class BedWarsConfig {

    public static final FileConfiguration config;

    static {
        config = Main.getPlugin().getConfig();
        addDefaults();
    }

    public static void addLocation(Location loc, int mapID, int team) {
        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();

        config.set("BedWars." + mapID + "." + team + ".Spawnpoints.X", x + 0.5);
        config.set("BedWars." + mapID + "." + team + ".Spawnpoints.Y", y);
        config.set("BedWars." + mapID + "." + team + ".Spawnpoints.Z", z + 0.5);
        config.set("BedWars." + mapID + "." + team + ".Spawnpoints.Yaw", ya);
        Main.getPlugin().saveConfig();
    }

    public static ArrayList<Block> getBed(int mapID, int team, World w) {
        ArrayList<Block> bed = new ArrayList<>();

        double fx = config.getDouble("BedWars." + mapID + "." + team + ".BedPart1.X");
        double fy = config.getDouble("BedWars." + mapID + "." + team + ".BedPart1.Y");
        double fz = config.getDouble("BedWars." + mapID + "." + team + ".BedPart1.Z");

        double hx = config.getDouble("BedWars." + mapID + "." + team + ".BedPart2.X");
        double hy = config.getDouble("BedWars." + mapID + "." + team + ".BedPart2.Y");
        double hz = config.getDouble("BedWars." + mapID + "." + team + ".BedPart2.Z");

        Location l1 = new Location(w, fx, fy, fz);
        Location l2 = new Location(w, hx, hy, hz);
        bed.add(l1.getBlock());
        bed.add(l2.getBlock());
        return bed;
    }

    public static void setBed(int mapID, int team, Location head, Location foot) {
        double fx = foot.getBlockX();
        double fy = foot.getBlockY();
        double fz = foot.getBlockZ();

        double hx = head.getBlockX();
        double hy = head.getBlockY();
        double hz = head.getBlockZ();

        config.set("BedWars." + mapID + "." + team + ".BedPart1.X", fx + 0.5);
        config.set("BedWars." + mapID + "." + team + ".BedPart1.Y", fy);
        config.set("BedWars." + mapID + "." + team + ".BedPart1.Z", fz + 0.5);

        config.set("BedWars." + mapID + "." + team + ".BedPart2.X", hx + 0.5);
        config.set("BedWars." + mapID + "." + team + ".BedPart2.Y", hy);
        config.set("BedWars." + mapID + "." + team + ".BedPart2.Z", hz + 0.5);
        Main.getPlugin().saveConfig();
    }

    public static Location getLocation(int mapID, int team, World wo) {

        double x = config.getDouble("BedWars." + mapID + "." + team + ".Spawnpoints.X");
        double y = config.getDouble("BedWars." + mapID + "." + team + ".Spawnpoints.Y");
        double z = config.getDouble("BedWars." + mapID + "." + team + ".Spawnpoints.Z");
        float ya = config.getInt("BedWars." + mapID + "." + team + ".Spawnpoints.Yaw");

        return new Location(wo, x, y, z, ya, 0);
    }

    public static void addSpawnerLocations(Location loc, ItemType spawner, int mapID) {
        int i = config.getInt("BedWars." + mapID + "." + spawner.name() + ".lastID") + 1;

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        config.set("BedWars." + mapID + "." + spawner.name() + "." + i + ".X", x);
        config.set("BedWars." + mapID + "." + spawner.name() + "." + i + ".Y", y);
        config.set("BedWars." + mapID + "." + spawner.name() + "." + i + ".Z", z);
        config.set("BedWars." + mapID + "." + spawner.name() + ".lastID", i);
        Main.getPlugin().saveConfig();
    }

    private static void addDefaults() {

        Main plugin = Main.getPlugin();

        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.getLogger().info("configs.yml not found, creating!");
            plugin.saveDefaultConfig();
            plugin.saveConfig();
            config.options().copyDefaults(true);
        } else {
            plugin.getLogger().info("configs.yml found, loading!");
        }
    }

    public static HashMap<ItemType, ArrayList<Location>> getSpawner(int mapID, World w) {
        HashMap<ItemType, ArrayList<Location>> map = new HashMap<>();
        for (ItemType types : ItemType.values()) {
            ArrayList<Location> locations = new ArrayList<>();
            int i = config.getInt("BedWars." + mapID + "." + types.name() + ".lastID");
            for (int j = 1; j <= i; j++) {
                int x = config.getInt("BedWars." + mapID + "." + types.name() + "." + j + ".X");
                int y = config.getInt("BedWars." + mapID + "." + types.name() + "." + j + ".Y");
                int z = config.getInt("BedWars." + mapID + "." + types.name() + "." + j + ".Z");
                locations.add(new Location(w, x, y, z));
            }
            map.put(types, locations);
        }
        return map;
    }
}