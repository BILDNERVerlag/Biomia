package de.biomia.spigot.tools;

import de.biomia.spigot.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static de.biomia.spigot.configs.Config.saveConfig;

public class Hologram {

    public static void newHologram(Player p, String[] s) {
        int i = 0;
        for (String string : s) {
            Location loc = p.getLocation();
            ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(new Location(loc.getWorld(),
                            loc.getBlockX() + 0.5, loc.getBlockY() - (i * 0.225), loc.getBlockZ() + 0.5),
                    EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', string));
            armorStand.setCustomNameVisible(true);

            ArrayList<String> l = new ArrayList<>(Main.getPlugin().getConfig().getStringList("Holograms"));
            l.add(armorStand.getUniqueId().toString());

            Main.getPlugin().getConfig().set("Holograms", l);
            saveConfig();
            i++;
        }
    }

    public static void removeHologram(ArmorStand armorStand) {

        ArrayList<String> l = new ArrayList<>(Main.getPlugin().getConfig().getStringList("Holograms"));

        if (l.contains(armorStand.getUniqueId().toString())) {
            armorStand.remove();
            l.remove(armorStand.getUniqueId().toString());
            Main.getPlugin().getConfig().set("Holograms", l);
            saveConfig();
        }
    }
}