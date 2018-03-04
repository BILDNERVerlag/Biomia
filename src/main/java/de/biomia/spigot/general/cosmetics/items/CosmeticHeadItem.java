package de.biomia.spigot.general.cosmetics.items;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.general.cosmetics.Cosmetic.Group;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CosmeticHeadItem extends CosmeticItem {

    private final ItemStack head;
    public static ArrayList<BiomiaPlayer> heads = new ArrayList<>();

    public CosmeticHeadItem(int id, String name, ItemStack is, Commonness c, ItemStack head) {
        super(id, name, is, c, Group.HEADS);
        this.head = head;
    }

    @Override
    public void use(BiomiaPlayer bp) {
        super.use(bp);
        bp.getPlayer().getInventory().setHelmet(head);
    }

    @Override
    public void remove(BiomiaPlayer bp) {
        bp.getPlayer().getInventory().setHelmet(null);
        if (CosmeticSuitItem.suits.containsKey(bp))
            CosmeticSuitItem.suits.get(bp).use(bp);
    }

    public ItemStack getHead() {
        return head;
    }

}
