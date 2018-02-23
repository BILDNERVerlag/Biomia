package de.biomia.freebuild.home.storage;

import com.google.common.io.Files;
import de.biomia.freebuild.home.util.UUIDFetcher;
import de.biomiaAPI.main.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class HomeFileManager {
	private static final String fileName = "Homes.yml";
	private YamlConfiguration homes;
	private File homesFile;

	public YamlConfiguration getHomes() {
		if (homes == null) {
			reloadHomes();
		}
		return homes;
	}

	private void reloadHomes() {
		if (homesFile == null) {
			homesFile = new File(Main.getPlugin().getDataFolder(), "Homes.yml");
		}
		homes = YamlConfiguration.loadConfiguration(homesFile);

		InputStream defHomes = Main.getPlugin().getResource("Homes.yml");
		if (defHomes != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(new BufferedReader(new InputStreamReader(defHomes)));
			homes.setDefaults(defConfig);
		}
	}

	public void saveHomes() {
		if ((homes == null) || (homesFile == null)) {
			return;
		}
		try {
			getHomes().save(homesFile);
		} catch (IOException e) {
			Main.getPlugin().getLogger().log(Level.SEVERE, "Could not save homes file to " + homesFile, e);
		}
	}

	public void UuidUpdate() {
		File oldFile = new File(Main.getPlugin().getDataFolder(), "Homes.yml.bak");

		Main.getPlugin().getLogger().info("Beginning UUID conversion. This might take a while...");
		YamlConfiguration oldConfig = getHomes();
		YamlConfiguration result = new YamlConfiguration();
		Map<String, UUID> lusers;
		Map<String, UUID> users;

		try {
			users = new UUIDFetcher(new ArrayList(oldConfig.getKeys(false))).call();

			lusers = new HashMap(users.size());
			for (Map.Entry<String, UUID> e : users.entrySet())
				lusers.put(e.getKey().toLowerCase(), e.getValue());
		} catch (Exception e) {
			return;
		}
		for (String key : oldConfig.getKeys(false)) {
			try {
				String target = lusers.get(key.toLowerCase()).toString();
				ConfigurationSection section = result.createSection(target);
				ConfigurationSection oldSection = oldConfig.getConfigurationSection(key);

				for (String homeName : oldSection.getKeys(false)) {
					ConfigurationSection homeSection = section.createSection(homeName);
					ConfigurationSection oldHome = oldSection.getConfigurationSection(homeName);
					homeSection.set("world", oldHome.get("world"));
					homeSection.set("x", oldHome.get("x"));
					homeSection.set("y", oldHome.get("y"));
					homeSection.set("z", oldHome.get("z"));
				}
			} catch (Exception e2) {
				ConfigurationSection section;
				ConfigurationSection oldSection;
				e2.printStackTrace();
			}
		}
		try {
			if (homesFile == null) {
				homesFile = new File(Main.getPlugin().getDataFolder(), "Homes.yml");
			}
			Files.copy(homesFile, oldFile);
			result.save(homesFile);
			reloadHomes();
			Main.getPlugin().getConfig().options().copyDefaults(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Main.getPlugin().getConfig().set("ConfigVersion", 2);
		Main.getPlugin().getLogger().info("UUID conversion completed successfully.");
	}
}
