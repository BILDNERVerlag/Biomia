package de.biomia.general.cosmetics.items;

import de.biomia.BiomiaPlayer;
import de.biomia.general.cosmetics.Cosmetic.Group;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CosmeticSuitItem extends CosmeticItem {

    private ItemStack helmet = null;
    private ItemStack chestplate = null;
    private ItemStack leggins = null;
    private ItemStack boots = null;

    public static final HashMap<BiomiaPlayer, CosmeticSuitItem> suits = new HashMap<>();

    public CosmeticSuitItem(int id, String name, ItemStack is, Commonness c) {
        super(id, name, is, c, Group.SUITS);
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggins;
    }

    public void setLeggins(ItemStack leggins) {
        this.leggins = leggins;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    @Override
    public void use(BiomiaPlayer bp) {
        super.use(bp);
        Player p = bp.getPlayer();
        p.getInventory().setHelmet(getHelmet());
        p.getInventory().setChestplate(getChestplate());
        p.getInventory().setLeggings(getLeggings());
        p.getInventory().setBoots(getBoots());
        suits.put(bp, this);
    }

    @Override
    public void remove(BiomiaPlayer bp) {
        Player p = bp.getPlayer();
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        suits.remove(bp);
    }
}
