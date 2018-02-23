package de.biomia.freebuild.home.homes;

import de.biomia.freebuild.home.configuration.ConfigManager;
import de.biomia.freebuild.home.storage.HomeFileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager {
	private final HomeFileManager fileManager;
	private final Map<UUID, Map<String, Location>> loadedHomes;

	public HomeManager(HomeFileManager fileManager) {
		this.fileManager = fileManager;
		loadedHomes = new HashMap();
	}

	public boolean reachedMaxHomes(UUID uuid) {
		return getHomesSize(uuid) == ConfigManager.getMaxHomes();
	}

	private int getHomesSize(UUID uuid) {
		if (fileManager.getHomes().contains(uuid.toString())) {
			return fileManager.getHomes().getConfigurationSection(uuid.toString()).getKeys(false).size();
		}
		return 0;
	}

	private void saveHomeToFile(UUID uuid, Location location, String homeName) {
		ConfigurationSection home = fileManager.getHomes()
				.getConfigurationSection(uuid.toString() + "." + homeName.toLowerCase());
		if (home == null) {
			home = fileManager.getHomes().createSection(uuid.toString() + "." + homeName.toLowerCase());
		}

		home.set("world", location.getWorld().getName());
		home.set("x", location.getBlockX());
		home.set("y", location.getBlockY());
		home.set("z", Integer.valueOf(location.getBlockZ()));

		fileManager.saveHomes();
	}

	public void saveHome(Player player, String homeName) {
		UUID uuid = player.getUniqueId();
		Location location = player.getLocation();

		Map<String, Location> homeLocation = loadedHomes.get(uuid);
		if (homeLocation == null) {
			homeLocation = new HashMap();
		}
		homeLocation.put(homeName.toLowerCase(), location);
		loadedHomes.put(uuid, homeLocation);

		saveHomeToFile(uuid, location, homeName);
	}

	public void deleteHome(UUID uuid, String homeName) {
		Map homeLocations = loadedHomes.get(uuid);
		if (homeLocations != null) {
			homeLocations.remove(homeName.toLowerCase());
			loadedHomes.put(uuid, homeLocations);
		}
		ConfigurationSection home = fileManager.getHomes().getConfigurationSection(uuid.toString());
		home.set(homeName.toLowerCase(), null);
		fileManager.saveHomes();
	}

	public void loadPlayerHomes(UUID uuid) {
		ConfigurationSection homes = fileManager.getHomes().getConfigurationSection(uuid.toString());
		if (homes != null) {
			Map<String, Location> homeLocation = new HashMap();

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
			loadedHomes.put(uuid, homeLocation);
		} else {
			loadedHomes.put(uuid, new HashMap());
		}
	}

	public void unloadPlayerHomes(UUID uuid) {
		loadedHomes.remove(uuid);
	}

	public Location getPlayerHome(UUID uuid, String homeName) {
		Map<String, Location> homeLocations = loadedHomes.get(uuid);
		if (homeLocations != null) {
			return homeLocations.get(homeName.toLowerCase());
		}
		return null;
	}

	public Object getPlayerHomeFromFile(UUID uuid, String homeName) {
		ConfigurationSection homes = fileManager.getHomes().getConfigurationSection(uuid.toString());
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

	public Map<String, Location> getPlayerHomes(UUID uuid) {
		if (loadedHomes.containsKey(uuid)) {
			return loadedHomes.get(uuid);
		}
		loadPlayerHomes(uuid);
		HashMap playerHomes = (HashMap) loadedHomes.get(uuid);
		unloadPlayerHomes(uuid);
		return playerHomes;
	}

	public Map<UUID, Map<String, Location>> getHomes() {
		return loadedHomes;
	}
}
