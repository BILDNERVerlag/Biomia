package de.biomia.general.configs;

import de.biomia.api.main.Main;
import org.bukkit.configuration.file.FileConfiguration;

abstract public class Config {

    private static final FileConfiguration config = Main.getPlugin().getConfig();

    public static FileConfiguration getConfig() {
        return config;
    }
}
