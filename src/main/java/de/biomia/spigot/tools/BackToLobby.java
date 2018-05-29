package de.biomia.spigot.tools;

import de.biomia.universal.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BackToLobby {

    private static final ItemStack backToLobbyItem = ItemCreator.itemCreate(Material.MAGMA_CREAM, String.format("%sLobby", Messages.COLOR_MAIN));

    public static void getLobbyItem(Player p, int slot) {
        p.getInventory().setItem(slot, backToLobbyItem);
    }

    public static ItemStack getBackToLobbyItem() {
        return backToLobbyItem;
    }
}