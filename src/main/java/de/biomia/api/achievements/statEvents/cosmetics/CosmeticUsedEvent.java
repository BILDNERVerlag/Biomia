package de.biomia.api.achievements.statEvents.cosmetics;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.achievements.statEvents.BiomiaPlayerEvent;
import de.biomia.api.cosmetics.CosmeticItem;
import org.bukkit.event.HandlerList;

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
