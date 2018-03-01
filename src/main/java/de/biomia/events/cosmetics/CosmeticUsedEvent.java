package de.biomia.events.cosmetics;

import de.biomia.BiomiaPlayer;
import de.biomia.events.BiomiaPlayerEvent;
import de.biomia.general.cosmetics.CosmeticItem;
import org.bukkit.event.HandlerList;

public class CosmeticUsedEvent extends BiomiaPlayerEvent {

    private static final HandlerList list = new HandlerList();

    private final CosmeticItem item;

    public CosmeticUsedEvent(BiomiaPlayer bp, CosmeticItem item) {
        super(bp);
        this.item = item;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public CosmeticItem getItem() {
        return item;
    }

}
