package de.biomia.demoserver.config;

import de.biomia.api.itemcreator.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Teleporter {

	public static void giveItem(Player p) {

		p.getInventory().setItem(0, ItemCreator.itemCreate(Material.CHEST, "00A7dTeleporter"));

	}

}
