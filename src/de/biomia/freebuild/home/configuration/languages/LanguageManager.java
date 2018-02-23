package de.biomia.freebuild.home.configuration.languages;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class LanguageManager {
	public static String HOME_DELETED = ChatColor.YELLOW + "Home gel\u00f6scht.";
	public static String HOME_LIST_PREFIX = ChatColor.YELLOW + "Homes:";
	public static String HOME_NOT_FOUND = ChatColor.RED + "Home nicht gefunden.";
	public static String HOME_MAX_REACHED = ChatColor.RED
			+ "Home kann nicht gesetzt werden. Die maximale Home-Anzahl wurde erreicht.";

	public static String HOME_SET = ChatColor.YELLOW + "Home gesetzt.";
	public static String NO_HOMES_FOUND = ChatColor.RED + "Keine Homes gefunden.";
	public static String PLAYER_COMMAND_ONLY = ChatColor.RED + "Nur Spieler k\u00f6nnen dieses Command benutzen.";
	public static String TELEPORT_OTHERHOME = ChatColor.YELLOW + "Zu %p's Home teleportiert.";
	public static String TELEPORT_SUCCESS = ChatColor.YELLOW + "Teleportiert.";
	public static String PLAYER_NOT_EXIST = ChatColor.RED + "Dieser Spieler existiert nicht.";

	public LanguageManager(LanguageFileManager fileManager) {
		loadMessages(fileManager.getLanguageConfig());
	}

	private String convertColours(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	private void loadMessages(FileConfiguration languageConfig) {
		HOME_DELETED = convertColours(languageConfig.getString("home-deleted"));
		HOME_LIST_PREFIX = convertColours(languageConfig.getString("home-list-prefix"));
		HOME_NOT_FOUND = convertColours(languageConfig.getString("home-not-found"));
		HOME_MAX_REACHED = convertColours(languageConfig.getString("home-max-reached"));
		HOME_SET = convertColours(languageConfig.getString("home-set"));
		NO_HOMES_FOUND = convertColours(languageConfig.getString("no-homes-found"));
		PLAYER_COMMAND_ONLY = convertColours(languageConfig.getString("player-command-only"));
		TELEPORT_OTHERHOME = convertColours(languageConfig.getString("teleport-otherhome"));
		TELEPORT_SUCCESS = convertColours(languageConfig.getString("teleport-success"));
		PLAYER_NOT_EXIST = convertColours(languageConfig.getString("player-not-exist"));
	}
}
