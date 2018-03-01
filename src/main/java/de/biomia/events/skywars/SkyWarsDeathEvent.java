package de.biomia.events.skywars;

import de.biomia.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class SkyWarsDeathEvent extends SkyWarsEvent {

    private static final HandlerList list = new HandlerList();
    private final BiomiaPlayer killer;

    public SkyWarsDeathEvent(BiomiaPlayer killedPlayer, BiomiaPlayer killer) {
        super(killedPlayer);
        this.killer = killer;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public BiomiaPlayer getKiller() {
        return killer;
    }

}
