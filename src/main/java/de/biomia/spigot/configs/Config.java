package de.biomia.spigot.configs;

import de.biomia.spigot.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

abstract public class Config {

    private static final FileConfiguration config = Main.getPlugin().getConfig();

    static {
        addDefaults();
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    private static void addDefaults() {

        File file = new File(Main.getPlugin().getDataFolder(), "config.yml");
        if (!file.exists()) {
            Main.getPlugin().getLogger().info("configs.yml not found, creating!");
            Main.getPlugin().saveDefaultConfig();
            saveConfig();
            getConfig().options().copyDefaults(true);
        } else {
            Main.getPlugin().getLogger().info("configs.yml found, loading!");
        }
    }

    public static void saveConfig() {
        Main.getPlugin().saveConfig();
    }
}
