package de.biomiaAPI.tools;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.biomiaAPI.main.Main;
import net.md_5.bungee.api.ChatColor;

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

			ArrayList<String> l = new ArrayList<>(Main.plugin.getConfig().getStringList("Holograms"));
			l.add(armorStand.getUniqueId().toString());

			Main.plugin.getConfig().set("Holograms", l);
			Main.plugin.saveConfig();
			i++;
		}
	}

	public static void removeHologram(ArmorStand armorStand) {

		ArrayList<String> l = new ArrayList<>(Main.plugin.getConfig().getStringList("Holograms"));

		if (l.contains(armorStand.getUniqueId().toString())) {
			armorStand.remove();
			l.remove(armorStand.getUniqueId().toString());
			Main.plugin.getConfig().set("Holograms", l);
			Main.plugin.saveConfig();
		}
	}
}
