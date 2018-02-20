package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsKillEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();

    private final BiomiaPlayer killer;
    private final boolean finalKill;

    public BedWarsKillEvent(BiomiaPlayer killer, BiomiaPlayer killedPlayer, boolean finalKill) {
        super(killedPlayer);
        this.killer = killer;
        this.finalKill = finalKill;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public BiomiaPlayer getKilledPlayer() {
        return killer;
    }

    public boolean isFinalKill() {
        return finalKill;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
