package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
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


}
