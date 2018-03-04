package de.biomia.spigot.events.skywars;

import de.biomia.spigot.BiomiaPlayer;
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
