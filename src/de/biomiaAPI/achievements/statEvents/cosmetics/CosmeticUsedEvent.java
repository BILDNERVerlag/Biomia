package de.biomiaAPI.achievements.statEvents.cosmetics;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.BiomiaPlayerEvent;
import de.biomiaAPI.cosmetics.Cosmetic;
import de.biomiaAPI.cosmetics.CosmeticItem;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class CosmeticUsedEvent extends BiomiaPlayerEvent {

    private static final HandlerList list = new HandlerList();

    private final CosmeticItem item;

    public CosmeticUsedEvent(BiomiaPlayer bp, CosmeticItem item) {
        super(bp);
        this.item = item;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public CosmeticItem getItem() {
        return item;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

}
