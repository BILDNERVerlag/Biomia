package de.biomia.spigot.tools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BackToLobby {

    private static final ItemStack backToLobbyItem = ItemCreator.itemCreate(Material.MAGMA_CREAM, "\u00A7cLobby");

    public static void getLobbyItem(Player p, int slot) {
        p.getInventory().setItem(slot, backToLobbyItem);
    }

    public static ItemStack getBackToLobbyItem() {
        return backToLobbyItem;
    }
}