package de.biomia.demoserver.config;

import de.biomiaAPI.main.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static FileConfiguration config = Main.getPlugin().getConfig();

    public static void addObjekt(int seite, String name, Location loc, Material material) {
        int lastID = config.getInt("lastID");

        name = ChatColor.translateAlternateColorCodes('&', name);

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float pi = loc.getPitch();
        float ya = loc.getYaw();
        String wo = loc.getWorld().getName();

        config.set((lastID + 1) + ".Seite", seite);
        config.set((lastID + 1) + ".Name", name);
        config.set((lastID + 1) + ".X", x);
        config.set((lastID + 1) + ".Y", y);
        config.set((lastID + 1) + ".Z", z);
        config.set((lastID + 1) + ".Yaw", ya);
        config.set((lastID + 1) + ".Pitch", pi);
        config.set((lastID + 1) + ".World", wo);
        config.set((lastID + 1) + ".Material", material.name());

        config.set("lastID", lastID + 1);
        new Bauten(name, seite, loc, material);
        Main.getPlugin().saveConfig();
    }

    public static void removeObjekt(String name) {

        name = ChatColor.translateAlternateColorCodes('&', name);

        for (int i = 0; i <= config.getInt("lastID"); i++) {

            if (config.getString(i + ".Name") != null && config.getString(i + ".Name").equalsIgnoreCase(name)) {
                config.set(i + "", null);
                Bukkit.broadcastMessage("§aObjekt entfernt!");
                Main.getPlugin().saveConfig();
                return;
            }

        }
        Bukkit.broadcastMessage("§cObjekt nicht gefunden!");

    }

    public static void hookInPlugin() {
        for (int i = 0; i <= config.getInt("lastID"); i++) {
            if (config.getString(i + ".Name") != null) {

                int seite = config.getInt(i + ".Seite");
                String name = config.getString(i + ".Name");

                double x = config.getDouble(i + ".X");
                double y = config.getDouble(i + ".Y");
                double z = config.getDouble(i + ".Z");
                float pi = config.getInt(i + ".Pitch");
                float ya = config.getInt(i + ".Yaw");
                World wo = Bukkit.getWorld(config.getString(i + ".World"));
                Material m = Material.valueOf(config.getString(i + ".Material"));

                Location loc = new Location(wo, x, y, z, ya, pi);

                new Bauten(name, seite, loc, m);
            }
        }
    }

}
