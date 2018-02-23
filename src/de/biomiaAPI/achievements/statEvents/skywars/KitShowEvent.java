package de.biomiaAPI.achievements.statEvents.skywars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class KitShowEvent extends KitEvent {


    private static final HandlerList list = new HandlerList();

    public KitShowEvent(BiomiaPlayer biomiaPlayer, int kitID) {
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