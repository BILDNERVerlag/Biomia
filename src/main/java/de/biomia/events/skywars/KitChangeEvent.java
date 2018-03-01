package de.biomia.events.skywars;

import de.biomia.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class KitChangeEvent extends KitEvent {

    private static final HandlerList list = new HandlerList();

    public KitChangeEvent(BiomiaPlayer biomiaPlayer, int kitID) {
        super(biomiaPlayer, kitID);
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

}
