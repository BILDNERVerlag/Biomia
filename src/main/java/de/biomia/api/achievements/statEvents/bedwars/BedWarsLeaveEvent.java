package de.biomia.api.achievements.statEvents.bedwars;

import de.biomia.api.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsLeaveEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();

    public BedWarsLeaveEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }
    public static HandlerList getHandlerList() {
        return list;
    }
}
