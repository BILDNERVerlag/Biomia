package de.biomia.spigot.minigames.general.shop;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.game.bedwars.BedWarsBuyItemEvent;
import de.biomia.spigot.minigames.general.ColorType;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class ShopItem extends Price {

    private final ItemStack itemStack;
    private final boolean colorable;
    private final ColorType type;
    private String name;

    ShopItem(int price, ItemStack item, ColorType colorType) {
        super(ItemType.BRONZE, price);
        this.itemStack = item;
        this.colorable = true;
        this.type = colorType;
        this.name = itemStack.getItemMeta().getDisplayName();
    }

    ShopItem(ItemType itemType, int price, ItemStack item, ColorType colorType) {
        super(itemType, price);
        this.itemStack = item;
        this.type = colorType;
        this.colorable = (this.type != null);
        this.name = itemStack.getItemMeta().getDisplayName();
    }

    ShopItem(ItemType itemType, int price, ItemStack item) {
        super(itemType, price);
        this.itemStack = item;
        colorable = false;
        type = null;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public boolean isColorable() {
        return colorable;
    }

    public ColorType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean take(BiomiaPlayer bp) {
        BedWarsBuyItemEvent e = new BedWarsBuyItemEvent(bp, this, getItem().getAmount(), null);
        Bukkit.getPluginManager().callEvent(e);
        return super.take(bp);
    }
}
