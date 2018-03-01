package de.biomia.events.bedwars;

import de.biomia.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsLeaveEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();

    public BedWarsLeaveEvent(BiomiaPlayer biomiaPlayer) {
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
