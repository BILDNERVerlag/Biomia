package de.biomia.api.tools;

import de.biomia.api.itemcreator.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BackToLobby {

	public static void getLobbyItem(Player p, int slot) {
		p.getInventory().setItem(slot, ItemCreator.itemCreate(Material.MAGMA_CREAM, "\u00A7cLobby"));
	}

}