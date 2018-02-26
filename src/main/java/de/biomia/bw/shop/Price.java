package de.biomia.bw.shop;

import de.biomia.bw.var.ItemType;
import de.biomia.api.itemcreator.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Price {

    private final int price;
    private final ItemType itemType;

    Price(ItemType itemType, int price) {
        this.price = price;
        this.itemType = itemType;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public ItemStack getPriceItem() {

        Material m = null;
        String name = null;
        switch (getItemType()) {
            case BRONZE:
                m = Material.CLAY_BRICK;
                name = "\u00A7c" + getPrice() + " Bronze";
                break;
            case IRON:
                m = Material.IRON_INGOT;
                name = "\u00A77" + getPrice() + " Eisen";
                break;
            case GOLD:
                m = Material.GOLD_INGOT;
                name = "\u00A76" + getPrice() + " Gold";
                break;
            default:
                break;
        }

        return ItemCreator.setAmount(ItemCreator.itemCreate(m, name), price);
    }

    public int getPrice() {
        return price;
    }

    private boolean hasEnough(Player p) {

        int i = 0;

        int menge = getPrice();
        Material material = ItemType.toMaterial(getItemType());

        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null)
                if (is.getType() == material)
                    i += is.getAmount();
        }
        return i >= menge;
    }

    private void takePriceFromInventory(Player p) {

        int i = 0;

        int menge = getPrice();
        Material material = ItemType.toMaterial(getItemType());

        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null)
                if (is.getType() == material)
                    if (is.getAmount() >= menge || is.getAmount() >= menge - i) {
                        is.setAmount(is.getAmount() - (menge - i));
                        return;
                    } else {
                        i += is.getAmount();
                        is.setAmount(0);
                    }
        }
    }

    boolean take(Player p) {
        if (hasEnough(p)) {
            takePriceFromInventory(p);
            return true;
        }
        return false;
    }

}
