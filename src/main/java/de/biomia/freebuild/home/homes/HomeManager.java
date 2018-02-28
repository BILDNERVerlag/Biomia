package de.biomia.freebuild.home.homes;

import de.biomia.freebuild.home.configuration.ConfigManager;
import de.biomia.freebuild.home.storage.HomeFileManager;
import de.biomia.api.Biomia;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HomeManager {
    private final HomeFileManager fileManager;
    private final HashMap<Integer, HashMap<String, Location>> loadedHomes;


    public HomeManager(HomeFileManager fileManager) {
        this.fileManager = fileManager;
        loadedHomes = new HashMap();
    }

    public boolean reachedMaxHomes(Integer biomiaID) {
        return getHomesSize(biomiaID) == ConfigManager.getMaxHomes();
    }

    private int getHomesSize(Integer biomiaID) {
        if (fileManager.getHomes().contains(biomiaID.toString())) {
            return fileManager.getHomes().getConfigurationSection(biomiaID.toString()).getKeys(false).size();
        }
        return 0;
    }

    private void saveHomeToFile(Integer biomiaID, Location location, String homeName) {
        ConfigurationSection home = fileManager.getHomes()
                .getConfigurationSection(biomiaID.toString() + "." + homeName.toLowerCase());
        if (home == null) {
            home = fileManager.getHomes().createSection(biomiaID.toString() + "." + homeName.toLowerCase());
        }

        home.set("world", location.getWorld().getName());
        home.set("x", location.getBlockX());
        home.set("y", location.getBlockY());
        home.set("z", location.getBlockZ());

        fileManager.saveHomes();
    }

    public void saveHome(Player player, String homeName) {
        Integer biomiaID = Biomia.getBiomiaPlayer(player).getBiomiaPlayerID();
        Location location = player.getLocation();

        HashMap<String, Location> homeLocation = loadedHomes.get(biomiaID);
        if (homeLocation == null) {
            homeLocation = new HashMap();
        }
        homeLocation.put(homeName.toLowerCase(), location);
        loadedHomes.put(biomiaID, (HashMap<String, Location>) homeLocation);

        saveHomeToFile(biomiaID, location, homeName);
    }

    public void deleteHome(Integer biomiaID, String homeName) {
        HashMap homeLocations = loadedHomes.get(biomiaID);
        if (homeLocations != null) {
            homeLocations.remove(homeName.toLowerCase());
            loadedHomes.put(biomiaID, homeLocations);
        }
        ConfigurationSection home = fileManager.getHomes().getConfigurationSection(biomiaID.toString());
        home.set(homeName.toLowerCase(), null);
        fileManager.saveHomes();
    }

    public void loadPlayerHomes(Integer biomiaID) {
        ConfigurationSection homes = fileManager.getHomes().getConfigurationSection(biomiaID.toString());
        if (homes != null) {
            HashMap<String, Location> homeLocation = new HashMap();

            for (String homeName : homes.getKeys(false)) {
                ConfigurationSection home = homes.getConfigurationSection(homeName);

                String world = home.getString("world", null);
                int x = home.getInt("x", Integer.MIN_VALUE);
                int y = home.getInt("y", Integer.MIN_VALUE);
                int z = home.getInt("z", Integer.MIN_VALUE);

                if ((world != null) && (x != Integer.MIN_VALUE) && (y != Integer.MIN_VALUE)
                        && (z != Integer.MIN_VALUE)) {
                    homeLocation.put(homeName.toLowerCase(), new Location(Bukkit.getWorld(world), x, y, z));
                } else {
                    System.out.println("Error in home, not loaded.");
                }
            }
            loadedHomes.put(biomiaID, homeLocation);
        } else {
            loadedHomes.put(biomiaID, new HashMap());
        }
    }

    public void unloadPlayerHomes(Integer biomiaID) {
        loadedHomes.remove(biomiaID);
    }

    public Location getPlayerHome(Integer biomiaID, String homeName) {
        Map<String, Location> homeLocations = loadedHomes.get(biomiaID);
        if (homeLocations != null) {
            return homeLocations.get(homeName.toLowerCase());
        }
        return null;
    }

    public Object getPlayerHomeFromFile(Integer biomiaID, String homeName) {
        ConfigurationSection homes = fileManager.getHomes().getConfigurationSection(biomiaID.toString());
        HashMap homeLocation;
        homeLocation = new HashMap();
        if (homes != null) {
            for (String home : homes.getKeys(false)) {
                ConfigurationSection homeSection = homes.getConfigurationSection(home);
                String world = homeSection.getString("world");
                int x = homeSection.getInt("x");
                int y = homeSection.getInt("y");
                int z = homeSection.getInt("z");

                homeLocation.put(home.toLowerCase(), new Location(Bukkit.getWorld(world), x, y, z));
            }
        }
        return homeLocation.get(homeName.toLowerCase());
    }

    public HashMap getPlayerHomes(Integer biomiaID) {
        if (loadedHomes.containsKey(biomiaID)) {
            return (HashMap) loadedHomes.get(biomiaID);
        }
        loadPlayerHomes(biomiaID);
        HashMap playerHomes = (HashMap) loadedHomes.get(biomiaID);
        unloadPlayerHomes(biomiaID);
        return playerHomes;
    }

    public HashMap<Integer, HashMap<String, Location>> getHomes() {
        return loadedHomes;
    }
}
