package de.biomia.server.freebuild.home;

import de.biomia.Main;
import de.biomia.dataManager.configs.Config;
import de.biomia.server.freebuild.home.homes.HomeManager;
import de.biomia.server.freebuild.home.listeners.GatewayListener;
import de.biomia.server.freebuild.home.storage.HomeFileManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class Home {
	public static final String HOME_DELETED = ChatColor.YELLOW + "Home gel\u00f6scht.";
	public static final String HOME_NOT_FOUND = ChatColor.RED + "Home nicht gefunden.";
	public static final String HOME_MAX_REACHED = ChatColor.RED
			+ "Home kann nicht gesetzt werden. Die maximale Home-Anzahl wurde erreicht.";
	public static final String HOME_SET = ChatColor.YELLOW + "Home gesetzt.";
	public static final String PLAYER_COMMAND_ONLY = ChatColor.RED + "Nur Spieler k\u00f6nnen dieses Command benutzen.";
	public static final String TELEPORT_SUCCESS = ChatColor.YELLOW + "Teleportiert.";
	//TODO: Neu schreiben
	private HomeFileManager homeFileManager;
	private HomeManager homeManager;

	public void onEnable() {
		FileConfiguration config = Main.getPlugin().getConfig();

		homeFileManager = new HomeFileManager();
		homeManager = new HomeManager(homeFileManager);

		config.options().copyDefaults(true);
		Config.saveConfig();

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
