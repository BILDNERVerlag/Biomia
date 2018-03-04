package de.biomia.spigot.configs;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class SkyWarsVersusConfig extends Config {

    public static void addLocation(Location loc, int mapID, int teamID) {

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();

        getConfig().set("SkyWars." + mapID + ".Spawnpoints." + teamID + ".X", x + 0.5);
        getConfig().set("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Y", y);
        getConfig().set("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Z", z + 0.5);
        getConfig().set("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Yaw", ya);
        saveConfig();
    }

    public static void addGoodChestLocation(Location loc, int mapID) {
        int chestID = getConfig().getInt("SkyWars." + mapID + ".lastGoodChestID") + 1;

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();

        getConfig().set("SkyWars." + mapID + ".Chests.GoodChest." + chestID + ".X", x);
        getConfig().set("SkyWars." + mapID + ".Chests.GoodChest." + chestID + ".Y", y);
        getConfig().set("SkyWars." + mapID + ".Chests.GoodChest." + chestID + ".Z", z);

        getConfig().set("SkyWars." + mapID + ".lastGoodChestID", chestID);
        saveConfig();
    }

    public static void addNormalChestLocation(Location loc, int mapID) {
        int lastChestID = getConfig().getInt("SkyWars." + mapID + ".lastNormalChestID") + 1;

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();

        getConfig().set("SkyWars." + mapID + ".Chests.NormalChest." + lastChestID + ".X", x);
        getConfig().set("SkyWars." + mapID + ".Chests.NormalChest." + lastChestID + ".Y", y);
        getConfig().set("SkyWars." + mapID + ".Chests.NormalChest." + lastChestID + ".Z", z);

        getConfig().set("SkyWars." + mapID + ".lastNormalChestID", lastChestID);
        saveConfig();
    }

    public static Location loadLocsFromConfig(int mapID, int teamID, World wo) {
        double x = getConfig().getDouble("SkyWars." + mapID + ".Spawnpoints." + teamID + ".X");
        double y = getConfig().getDouble("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Y");
        double z = getConfig().getDouble("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Z");
        float ya = getConfig().getInt("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Yaw");
        return new Location(wo, x, y, z, ya, 0);
    }

    public static ArrayList<Location> loadNormalChestsFromConfig(int mapID, World wo) {

        ArrayList<Location> normalChests = new ArrayList<>();

        for (int i = 0; i <= getConfig().getInt("SkyWars." + mapID + ".lastNormalChestID"); i++) {
            double x = getConfig().getDouble("SkyWars." + mapID + ".Chests.NormalChest." + i + ".X");
            double y = getConfig().getDouble("SkyWars." + mapID + ".Chests.NormalChest." + i + ".Y");
            double z = getConfig().getDouble("SkyWars." + mapID + ".Chests.NormalChest." + i + ".Z");
            normalChests.add(new Location(wo, x, y, z));
        }
        return normalChests;
    }

    public static ArrayList<Location> loadGoodChestsFromConfig(int mapID, World wo) {

        ArrayList<Location> goodChests = new ArrayList<>();

        for (int i = 0; i <= getConfig().getInt("SkyWars." + mapID + ".lastGoodChestID"); i++) {
            double x = getConfig().getDouble("SkyWars." + mapID + ".Chests.GoodChest." + i + ".X");
            double y = getConfig().getDouble("SkyWars." + mapID + ".Chests.GoodChest." + i + ".Y");
            double z = getConfig().getDouble("SkyWars." + mapID + ".Chests.GoodChest." + i + ".Z");

            goodChests.add(new Location(wo, x, y, z));
        }

        return goodChests;
    }
}