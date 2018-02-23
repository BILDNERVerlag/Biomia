package de.biomia.freebuild.home.configuration.languages;

import de.biomiaAPI.main.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class LanguageFileManager {
	private static final String LANGUAGE_FILE_NAME = "languages.yml";
	private FileConfiguration languageConfig = null;
	private File languageFile = null;

	public LanguageFileManager() {
		saveDefaultLanguages();
	}

	public FileConfiguration getLanguageConfig() {
		if (languageConfig == null) {
			reloadLanguages();
		}
		return languageConfig;
	}

	void reloadLanguages() {
		if (languageFile == null) {
			languageFile = new File(Main.getPlugin().getDataFolder(), "languages.yml");
		}
		languageConfig = YamlConfiguration.loadConfiguration(languageFile);

		InputStream defLanguageConfig = Main.getPlugin().getResource("languages.yml");
		if (defLanguageConfig != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(new BufferedReader(new InputStreamReader(defLanguageConfig)));
			languageConfig.setDefaults(defConfig);
		}
	}

	public void saveLanguages() {
		if ((languageConfig == null) || (languageFile == null)) {
			return;
		}
		try {
			getLanguageConfig().save(languageFile);
		} catch (IOException ex) {
			Main.getPlugin().getLogger().log(Level.SEVERE, "Could not save languages file to " + languageFile, ex);
		}
	}

	public void saveDefaultLanguages() {
		if (languageFile == null) {
			languageFile = new File(Main.getPlugin().getDataFolder(), "languages.yml");
		}
		if (!languageFile.exists()) {
			Main.getPlugin().saveResource("languages.yml", false);
		}
	}
}
