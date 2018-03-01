package de.biomia.events.skywars;

import de.biomia.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class SkyWarsLeaveEvent extends SkyWarsEvent {

    private static final HandlerList list = new HandlerList();

    public SkyWarsLeaveEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
    public static HandlerList getHandlerList() {
        return list;
    }
    @Override
    public HandlerList getHandlers() {
        return list;
    }

}
