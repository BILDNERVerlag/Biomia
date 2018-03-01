package de.biomia.general.configs;

import de.biomia.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

abstract public class Config {

    static {
        addDefaults();
    }

    private static final FileConfiguration config = Main.getPlugin().getConfig();

    public static FileConfiguration getConfig() {
        return config;
    }

    private static void addDefaults() {
        Main plugin = Main.getPlugin();

        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.getLogger().info("configs.yml not found, creating!");
            plugin.saveDefaultConfig();
            plugin.saveConfig();
            getConfig().options().copyDefaults(true);
        } else {
            plugin.getLogger().info("configs.yml found, loading!");
        }
    }
}