package de.biomia.freebuild.home;

import de.biomia.freebuild.home.configuration.ConfigManager;
import de.biomia.freebuild.home.configuration.languages.LanguageFileManager;
import de.biomia.freebuild.home.configuration.languages.LanguageManager;
import de.biomia.freebuild.home.homes.HomeManager;
import de.biomia.freebuild.home.listeners.GatewayListener;
import de.biomia.freebuild.home.storage.HomeFileManager;
import de.biomiaAPI.main.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class Home {
	private HomeFileManager homeFileManager;
	private HomeManager homeManager;

	public void onEnable() {
		FileConfiguration config = Main.getPlugin().getConfig();

		homeFileManager = new HomeFileManager();
		homeManager = new HomeManager(homeFileManager);

		config.options().copyDefaults(true);
		Main.getPlugin().saveConfig();
		LanguageFileManager languageFileManager = new LanguageFileManager();
		languageFileManager.saveLanguages();
		new LanguageManager(languageFileManager);

		new ConfigManager(this);
		homeFileManager.saveHomes();
		loadHomeListeners();
		Main.getPlugin().getLogger().info("Homes Enabled!");
	}

	public void terminateHomes() {
		homeFileManager.saveHomes();
		Main.getPlugin().getLogger().log(Level.INFO, "Homes Disabled!");
	}

	public void loadHomeListeners() {
		Main.getPlugin().getServer().getPluginManager().registerEvents(new GatewayListener(homeManager),
				Main.getPlugin());
	}

	public HomeManager getHomeManager() {
		return homeManager;
	}
}
