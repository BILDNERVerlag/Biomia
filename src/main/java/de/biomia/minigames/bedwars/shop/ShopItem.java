package de.biomia.minigames.bedwars.shop;

import de.biomia.minigames.bedwars.var.ColorType;
import de.biomia.minigames.bedwars.var.ItemType;
import de.biomia.api.Biomia;
import de.biomia.api.achievements.statEvents.bedwars.BedWarsBuyItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopItem extends Price {

    private final ItemStack itemStack;
    private final boolean colorble;
    private final ColorType type;
    private String name;

    ShopItem(int price, ItemStack item, ColorType colorType) {
        super(ItemType.BRONZE, price);
        this.itemStack = item;
        this.colorble = true;
        this.type = colorType;
        this.name = itemStack.getItemMeta().getDisplayName();
    }

    ShopItem(ItemType itemType, int price, ItemStack item) {
        super(itemType, price);
        this.itemStack = item;
        colorble = false;
        type = null;
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

    @Override
    public boolean take(Player p) {
        BedWarsBuyItemEvent e = new BedWarsBuyItemEvent(Biomia.getBiomiaPlayer(p), name, getPrice());
        Bukkit.getPluginManager().callEvent(e);
        return super.take(p);
    }
}
