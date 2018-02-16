package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsKillEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();

    private final BiomiaPlayer killer;

    public BedWarsKillEvent(BiomiaPlayer killedPlayer, BiomiaPlayer killer) {
        super(killedPlayer);
        this.killer = killer;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public BiomiaPlayer getKilledPlayer() {
        return killer;
    }
}
