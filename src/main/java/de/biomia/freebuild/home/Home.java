package de.biomia.freebuild.home;

import de.biomia.freebuild.home.configuration.ConfigManager;
import de.biomia.freebuild.home.homes.HomeManager;
import de.biomia.freebuild.home.listeners.GatewayListener;
import de.biomia.freebuild.home.storage.HomeFileManager;
import de.biomia.api.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class Home {
	private HomeFileManager homeFileManager;
	private HomeManager homeManager;

	public static final String HOME_DELETED = ChatColor.YELLOW + "Home gel\u00f6scht.";
	public static final String HOME_LIST_PREFIX = ChatColor.YELLOW + "Homes:";
	public static final String HOME_NOT_FOUND = ChatColor.RED + "Home nicht gefunden.";
	public static final String HOME_MAX_REACHED = ChatColor.RED
			+ "Home kann nicht gesetzt werden. Die maximale Home-Anzahl wurde erreicht.";

	public static final String HOME_SET = ChatColor.YELLOW + "Home gesetzt.";
	public static final String NO_HOMES_FOUND = ChatColor.RED + "Keine Homes gefunden.";
	public static final String PLAYER_COMMAND_ONLY = ChatColor.RED + "Nur Spieler k\u00f6nnen dieses Command benutzen.";
	public static final String TELEPORT_OTHERHOME = ChatColor.YELLOW + "Zu %p's Home teleportiert.";
	public static final String TELEPORT_SUCCESS = ChatColor.YELLOW + "Teleportiert.";
	public static final String PLAYER_NOT_EXIST = ChatColor.RED + "Dieser Spieler existiert nicht.";

	public void onEnable() {
		FileConfiguration config = Main.getPlugin().getConfig();

		homeFileManager = new HomeFileManager();
		homeManager = new HomeManager(homeFileManager);

		config.options().copyDefaults(true);
		Main.getPlugin().saveConfig();

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
