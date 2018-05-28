package de.biomia.spigot.minigames.general.shop;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.spigot.tools.ItemCreator;
import net.minecraft.server.v1_12_R1.EntityShulker;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Price {

    private final int price;
    private final ItemType itemType;

    public Price(ItemType itemType, int price) {
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
                name = "§c" + getPrice() + " " + BedWarsItemNames.bronze;
                break;
            case IRON:
                m = Material.IRON_INGOT;
                name = "§7" + getPrice() + " " + BedWarsItemNames.iron;
                break;
            case GOLD:
                m = Material.GOLD_INGOT;
                name = "§6" + getPrice() + " " + BedWarsItemNames.gold;
                break;
            default:
                break;
        }

        return ItemCreator.setAmount(ItemCreator.itemCreate(m, name), price);
    }

    private int getPrice() {
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
    
    boolean take(BiomiaPlayer bp) {
        if (hasEnough(bp.getPlayer())) {
            new TakeItemEvent(ItemType.toMaterial(getItemType()), getPrice()).executeEvent(bp);
            return true;
        }
        return false;
    }

}
