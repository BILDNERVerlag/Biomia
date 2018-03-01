package de.biomia.freebuild.home.configuration;

import de.biomia.Main;

public class ConfigManager {
	private static int maxHomes;

	public ConfigManager() {
		maxHomes = Main.getPlugin().getConfig().getInt("MaxHomes", 1);
	}

	public static int getMaxHomes() {
		return maxHomes;
	}
}
