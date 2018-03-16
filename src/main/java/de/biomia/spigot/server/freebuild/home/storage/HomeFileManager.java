package de.biomia.spigot.server.freebuild.home.storage;

import de.biomia.spigot.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
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
            homesFile = new File(Main.getPlugin().getDataFolder(), fileName);
        }
        homes = YamlConfiguration.loadConfiguration(homesFile);

        InputStream defHomes = Main.getPlugin().getResource(fileName);
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

}
