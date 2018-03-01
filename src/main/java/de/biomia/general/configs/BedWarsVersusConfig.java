package de.biomia.general.configs;

import de.biomia.minigames.versus.bw.var.ItemType;
import de.biomia.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;

public class BedWarsVersusConfig extends Config {

    public static void addLocation(Location loc, int mapID, int team) {
        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();

        getConfig().set("BedWars." + mapID + "." + team + ".Spawnpoints.X", x + 0.5);
        getConfig().set("BedWars." + mapID + "." + team + ".Spawnpoints.Y", y);
        getConfig().set("BedWars." + mapID + "." + team + ".Spawnpoints.Z", z + 0.5);
        getConfig().set("BedWars." + mapID + "." + team + ".Spawnpoints.Yaw", ya);
        Main.getPlugin().saveConfig();
    }

    public static ArrayList<Block> getBed(int mapID, int team, World w) {
        ArrayList<Block> bed = new ArrayList<>();

        double fx = getConfig().getDouble("BedWars." + mapID + "." + team + ".BedPart1.X");
        double fy = getConfig().getDouble("BedWars." + mapID + "." + team + ".BedPart1.Y");
        double fz = getConfig().getDouble("BedWars." + mapID + "." + team + ".BedPart1.Z");

        double hx = getConfig().getDouble("BedWars." + mapID + "." + team + ".BedPart2.X");
        double hy = getConfig().getDouble("BedWars." + mapID + "." + team + ".BedPart2.Y");
        double hz = getConfig().getDouble("BedWars." + mapID + "." + team + ".BedPart2.Z");

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

        getConfig().set("BedWars." + mapID + "." + team + ".BedPart1.X", fx + 0.5);
        getConfig().set("BedWars." + mapID + "." + team + ".BedPart1.Y", fy);
        getConfig().set("BedWars." + mapID + "." + team + ".BedPart1.Z", fz + 0.5);

        getConfig().set("BedWars." + mapID + "." + team + ".BedPart2.X", hx + 0.5);
        getConfig().set("BedWars." + mapID + "." + team + ".BedPart2.Y", hy);
        getConfig().set("BedWars." + mapID + "." + team + ".BedPart2.Z", hz + 0.5);
        Main.getPlugin().saveConfig();
    }

    public static Location getLocation(int mapID, int team, World wo) {

        double x = getConfig().getDouble("BedWars." + mapID + "." + team + ".Spawnpoints.X");
        double y = getConfig().getDouble("BedWars." + mapID + "." + team + ".Spawnpoints.Y");
        double z = getConfig().getDouble("BedWars." + mapID + "." + team + ".Spawnpoints.Z");
        float ya = getConfig().getInt("BedWars." + mapID + "." + team + ".Spawnpoints.Yaw");

        return new Location(wo, x, y, z, ya, 0);
    }

    public static void addSpawnerLocations(Location loc, ItemType spawner, int mapID) {
        int i = getConfig().getInt("BedWars." + mapID + "." + spawner.name() + ".lastID") + 1;

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        getConfig().set("BedWars." + mapID + "." + spawner.name() + "." + i + ".X", x);
        getConfig().set("BedWars." + mapID + "." + spawner.name() + "." + i + ".Y", y);
        getConfig().set("BedWars." + mapID + "." + spawner.name() + "." + i + ".Z", z);
        getConfig().set("BedWars." + mapID + "." + spawner.name() + ".lastID", i);
        Main.getPlugin().saveConfig();
    }

    public static HashMap<ItemType, ArrayList<Location>> getSpawner(int mapID, World w) {
        HashMap<ItemType, ArrayList<Location>> map = new HashMap<>();
        for (ItemType types : ItemType.values()) {
            ArrayList<Location> locations = new ArrayList<>();
            int i = getConfig().getInt("BedWars." + mapID + "." + types.name() + ".lastID");
            for (int j = 1; j <= i; j++) {
                int x = getConfig().getInt("BedWars." + mapID + "." + types.name() + "." + j + ".X");
                int y = getConfig().getInt("BedWars." + mapID + "." + types.name() + "." + j + ".Y");
                int z = getConfig().getInt("BedWars." + mapID + "." + types.name() + "." + j + ".Z");
                locations.add(new Location(w, x, y, z));
            }
            map.put(types, locations);
        }
        return map;
    }
}