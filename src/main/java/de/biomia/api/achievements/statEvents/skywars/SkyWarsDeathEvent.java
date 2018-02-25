package de.biomia.api.achievements.statEvents.skywars;

import de.biomia.api.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class SkyWarsDeathEvent extends SkyWarsEvent {

    private static final HandlerList list = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    private final BiomiaPlayer killer;

    public SkyWarsDeathEvent(BiomiaPlayer killedPlayer, BiomiaPlayer killer) {
        super(killedPlayer);
        this.killer = killer;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    public BiomiaPlayer getKiller() {
        return killer;
    }

}
