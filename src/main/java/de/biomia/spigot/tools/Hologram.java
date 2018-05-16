package de.biomia.spigot.tools;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Hologram {
    public static void newHologram(Location loc, String[] s) {
        for (int i = 0; i < s.length; i++) {
            String string = s[i];
            ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() - (i * 0.225), loc.getBlockZ() + 0.5), EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', string));
            armorStand.setCustomNameVisible(true);
        }
    }
}