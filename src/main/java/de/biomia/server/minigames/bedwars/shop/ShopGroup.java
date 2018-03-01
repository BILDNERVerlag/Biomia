package de.biomia.server.minigames.bedwars.shop;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ShopGroup {

    private final String name;
    private final ChatColor color;
    private final ItemStack icon;
    private final ArrayList<ShopItem> items = new ArrayList<>();
    private int invSize = 36;
    private Inventory inv = null;

    public ShopGroup(ChatColor chatColor, String name, ItemStack icon) {
        this.color = chatColor;
        this.name = name;
        this.icon = icon;
    }

    public Inventory getInventory() {
        if (items.size() > 7)
            invSize = 54;

        if (inv == null) {
            inv = Bukkit.createInventory(null, invSize, color + name);

            inv.setItem(0, Shop.backItem);

            int itemNum = 0;
            for (ShopItem item : items) {
                if (itemNum % 9 == 7 && items.size() - 1 > itemNum) {
                    itemNum += 11;
                }
                inv.setItem(itemNum + 10, item.getItem());
                inv.setItem(itemNum + 19, item.getPriceItem());
                itemNum++;
            }
        }
        return inv;
    }

    public ShopItem getShopItem(ItemStack is) {

        for (ShopItem si : items) {
            if (si.getItem().equals(is)) {
                return si;
            }
        }
        return null;
    }

    public void addItem(ShopItem item) {
        items.add(item);
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return color + name;
    }

    public ItemStack getIcon() {
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(color + name);
        icon.setItemMeta(meta);
        return icon;
    }
}
