package de.biomia.events.skywars;

import de.biomia.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class SkyWarsKillEvent extends SkyWarsEvent {

    private static final HandlerList list = new HandlerList();

    private final BiomiaPlayer killedPlayer;

    public SkyWarsKillEvent(BiomiaPlayer biomiaPlayer, BiomiaPlayer killedPlayer) {
        super(biomiaPlayer);
        this.killedPlayer = killedPlayer;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public BiomiaPlayer getKilledPlayer() {
        return killedPlayer;
    }
}
