package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsDeathEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();

    private final BiomiaPlayer killer;
    private final boolean finalDeath;

    public BedWarsDeathEvent(BiomiaPlayer killedPlayer, BiomiaPlayer killer, boolean finalDeath) {
        super(killedPlayer);
        this.killer = killer;
        this.finalDeath = finalDeath;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public BiomiaPlayer getKiller() {
        return killer;
    }

    public boolean isFinalDeath() {
        return finalDeath;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
