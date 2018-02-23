package de.biomia.freebuild.home.configuration;

import de.biomia.freebuild.home.Home;
import de.biomiaAPI.main.Main;

public class ConfigManager {
	public static final int CONFIG_VERSION_UUID_INTRODUCED = 2;
	public static final int CONFIG_VERSION = 2;
	private static int maxHomes;

	public ConfigManager(Home simpleHomes) {
		maxHomes = Main.getPlugin().getConfig().getInt("MaxHomes", 1);
	}

	public static int getMaxHomes() {
		return maxHomes;
	}
}
