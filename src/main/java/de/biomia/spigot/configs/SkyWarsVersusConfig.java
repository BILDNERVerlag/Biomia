package de.biomia.spigot.configs;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class SkyWarsVersusConfig extends Config {

    public static void addLocation(Location loc, String mapDisplayName, int teamID) {

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();

        getConfig().set("SkyWars." + mapDisplayName + ".Spawnpoints." + teamID + ".X", x + 0.5);
        getConfig().set("SkyWars." + mapDisplayName + ".Spawnpoints." + teamID + ".Y", y);
        getConfig().set("SkyWars." + mapDisplayName + ".Spawnpoints." + teamID + ".Z", z + 0.5);
        getConfig().set("SkyWars." + mapDisplayName + ".Spawnpoints." + teamID + ".Yaw", ya);
        saveConfig();
    }

    public static void addGoodChestLocation(Location loc, String mapDisplayName) {
        int chestID = getConfig().getInt("SkyWars." + mapDisplayName + ".lastGoodChestID") + 1;

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();

        getConfig().set("SkyWars." + mapDisplayName + ".Chests.GoodChest." + chestID + ".X", x);
        getConfig().set("SkyWars." + mapDisplayName + ".Chests.GoodChest." + chestID + ".Y", y);
        getConfig().set("SkyWars." + mapDisplayName + ".Chests.GoodChest." + chestID + ".Z", z);

        getConfig().set("SkyWars." + mapDisplayName + ".lastGoodChestID", chestID);
        saveConfig();
    }

    public static void addNormalChestLocation(Location loc, String mapDisplayName) {
        int lastChestID = getConfig().getInt("SkyWars." + mapDisplayName + ".lastNormalChestID") + 1;

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();

        getConfig().set("SkyWars." + mapDisplayName + ".Chests.NormalChest." + lastChestID + ".X", x);
        getConfig().set("SkyWars." + mapDisplayName + ".Chests.NormalChest." + lastChestID + ".Y", y);
        getConfig().set("SkyWars." + mapDisplayName + ".Chests.NormalChest." + lastChestID + ".Z", z);

        getConfig().set("SkyWars." + mapDisplayName + ".lastNormalChestID", lastChestID);
        saveConfig();
    }

    public static Location loadLocsFromConfig(String mapDisplayName, int teamID, World wo) {
        double x = getConfig().getDouble("SkyWars." + mapDisplayName + ".Spawnpoints." + teamID + ".X");
        double y = getConfig().getDouble("SkyWars." + mapDisplayName + ".Spawnpoints." + teamID + ".Y");
        double z = getConfig().getDouble("SkyWars." + mapDisplayName + ".Spawnpoints." + teamID + ".Z");
        float ya = getConfig().getInt("SkyWars." + mapDisplayName + ".Spawnpoints." + teamID + ".Yaw");
        return new Location(wo, x, y, z, ya, 0);
    }

    public static ArrayList<Location> loadNormalChestsFromConfig(String mapDisplayName, World wo) {

        ArrayList<Location> normalChests = new ArrayList<>();

        for (int i = 0; i <= getConfig().getInt("SkyWars." + mapDisplayName + ".lastNormalChestID"); i++) {
            double x = getConfig().getDouble("SkyWars." + mapDisplayName + ".Chests.NormalChest." + i + ".X");
            double y = getConfig().getDouble("SkyWars." + mapDisplayName + ".Chests.NormalChest." + i + ".Y");
            double z = getConfig().getDouble("SkyWars." + mapDisplayName + ".Chests.NormalChest." + i + ".Z");
            normalChests.add(new Location(wo, x, y, z));
        }
        return normalChests;
    }

    public static ArrayList<Location> loadGoodChestsFromConfig(String mapDisplayName, World wo) {

        ArrayList<Location> goodChests = new ArrayList<>();

        for (int i = 0; i <= getConfig().getInt("SkyWars." + mapDisplayName + ".lastGoodChestID"); i++) {
            double x = getConfig().getDouble("SkyWars." + mapDisplayName + ".Chests.GoodChest." + i + ".X");
            double y = getConfig().getDouble("SkyWars." + mapDisplayName + ".Chests.GoodChest." + i + ".Y");
            double z = getConfig().getDouble("SkyWars." + mapDisplayName + ".Chests.GoodChest." + i + ".Z");

            goodChests.add(new Location(wo, x, y, z));
        }

        return goodChests;
    }
}