package de.biomiaAPI.tools;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.biomiaAPI.itemcreator.ItemCreator;

public class BackToLobby {

	public static void getLobbyItem(Player p, int slot) {
		p.getInventory().setItem(slot, ItemCreator.itemCreate(Material.MAGMA_CREAM, "�cLobby"));
	}

}