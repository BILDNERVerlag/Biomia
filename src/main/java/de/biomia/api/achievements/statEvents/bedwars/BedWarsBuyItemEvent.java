package de.biomia.api.achievements.statEvents.bedwars;

import de.biomia.api.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsBuyItemEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();
    private final String itemName;
    private final int amount;

    public BedWarsBuyItemEvent(BiomiaPlayer biomiaPlayer, String itemName, int amount) {
        super(biomiaPlayer);
        this.itemName = itemName;
        this.amount = amount;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public int getAmount() {
        return amount;
    }

    public String getItemName() {
        return itemName;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
