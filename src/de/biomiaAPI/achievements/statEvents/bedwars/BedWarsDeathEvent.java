package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsDeathEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();

    private final BiomiaPlayer killer;

    public BedWarsDeathEvent(BiomiaPlayer killedPlayer, BiomiaPlayer killer) {
        super(killedPlayer);
        this.killer = killer;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public BiomiaPlayer getKiller() {
        return killer;
    }

}
