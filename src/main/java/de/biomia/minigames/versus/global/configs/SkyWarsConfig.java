package de.biomia.minigames.versus.global.configs;

import de.biomia.api.main.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class SkyWarsConfig {

    private final static FileConfiguration config = Main.getPlugin().getConfig();

    public static void addLocation(Location loc, int mapID, int teamID) {

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();

        config.set("SkyWars." + mapID + ".Spawnpoints." + teamID + ".X", x + 0.5);
        config.set("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Y", y);
        config.set("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Z", z + 0.5);
        config.set("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Yaw", ya);
        Main.getPlugin().saveConfig();
    }

    public static void addGoodChestLocation(Location loc, int mapID) {
        int chestID = config.getInt("SkyWars." + mapID + ".lastGoodChestID") + 1;

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();

        config.set("SkyWars." + mapID + ".Chests.GoodChest." + chestID + ".X", x);
        config.set("SkyWars." + mapID + ".Chests.GoodChest." + chestID + ".Y", y);
        config.set("SkyWars." + mapID + ".Chests.GoodChest." + chestID + ".Z", z);

        config.set("SkyWars." + mapID + ".lastGoodChestID", chestID);
        Main.getPlugin().saveConfig();
    }

    public static void addNormalChestLocation(Location loc, int mapID) {
        int lastChestID = config.getInt("SkyWars." + mapID + ".lastNormalChestID") + 1;

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();

        config.set("SkyWars." + mapID + ".Chests.NormalChest." + lastChestID + ".X", x);
        config.set("SkyWars." + mapID + ".Chests.NormalChest." + lastChestID + ".Y", y);
        config.set("SkyWars." + mapID + ".Chests.NormalChest." + lastChestID + ".Z", z);

        config.set("SkyWars." + mapID + ".lastNormalChestID", lastChestID);
        Main.getPlugin().saveConfig();
    }

    public static Location loadLocsFromConfig(int mapID, int teamID, World wo) {
        double x = config.getDouble("SkyWars." + mapID + ".Spawnpoints." + teamID + ".X");
        double y = config.getDouble("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Y");
        double z = config.getDouble("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Z");
        float ya = config.getInt("SkyWars." + mapID + ".Spawnpoints." + teamID + ".Yaw");
        return new Location(wo, x, y, z, ya, 0);
    }

    public static ArrayList<Location> loadNormalChestsFromConfig(int mapID, World wo) {

        ArrayList<Location> normalChests = new ArrayList<>();

        for (int i = 0; i <= config.getInt("SkyWars." + mapID + ".lastNormalChestID"); i++) {
            double x = config.getDouble("SkyWars." + mapID + ".Chests.NormalChest." + i + ".X");
            double y = config.getDouble("SkyWars." + mapID + ".Chests.NormalChest." + i + ".Y");
            double z = config.getDouble("SkyWars." + mapID + ".Chests.NormalChest." + i + ".Z");
            normalChests.add(new Location(wo, x, y, z));
        }
        return normalChests;
    }

    public static ArrayList<Location> loadGoodChestsFromConfig(int mapID, World wo) {

        ArrayList<Location> goodChests = new ArrayList<>();

        for (int i = 0; i <= config.getInt("SkyWars." + mapID + ".lastGoodChestID"); i++) {
            double x = config.getDouble("SkyWars." + mapID + ".Chests.GoodChest." + i + ".X");
            double y = config.getDouble("SkyWars." + mapID + ".Chests.GoodChest." + i + ".Y");
            double z = config.getDouble("SkyWars." + mapID + ".Chests.GoodChest." + i + ".Z");

            goodChests.add(new Location(wo, x, y, z));
        }

        return goodChests;
    }
}