package de.biomia.spigot.events.skywars;

import de.biomia.spigot.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class KitBuyEvent extends KitEvent {

    private static final HandlerList list = new HandlerList();

    public KitBuyEvent(BiomiaPlayer biomiaPlayer, int kitID) {
        super(biomiaPlayer, kitID);
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

}