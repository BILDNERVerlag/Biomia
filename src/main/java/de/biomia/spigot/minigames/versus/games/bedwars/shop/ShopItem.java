package de.biomia.spigot.minigames.versus.games.bedwars.shop;

import de.biomia.spigot.minigames.general.ColorType;
import org.bukkit.inventory.ItemStack;

public class ShopItem {

    private final ItemStack itemStack;
    private final Price price;
    private final boolean colorble;
    private final ColorType type;

    public ShopItem(Price price, ItemStack item, boolean colorble, ColorType type) {
        this.itemStack = item;
        this.price = price;
        this.colorble = colorble;
        this.type = type;
    }

    public ShopItem(Price price, ItemStack item) {
        this.itemStack = item;
        this.price = price;
        colorble = false;
        type = null;
    }

    public Price getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public boolean isColorble() {
        return colorble;
    }

    public ColorType getType() {
        return type;
    }

}
