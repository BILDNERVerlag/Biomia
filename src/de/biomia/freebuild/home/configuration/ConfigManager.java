package de.biomia.freebuild.home.configuration;

import de.biomia.freebuild.home.Home;
import de.biomiaAPI.main.Main;

public class ConfigManager {
	private static int maxHomes;

	public ConfigManager(Home simpleHomes) {
		maxHomes = Main.getPlugin().getConfig().getInt("MaxHomes", 1);
	}

	public static int getMaxHomes() {
		return maxHomes;
	}
}
