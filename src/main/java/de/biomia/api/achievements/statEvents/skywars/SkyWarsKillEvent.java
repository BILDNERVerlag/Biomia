package de.biomia.api.achievements.statEvents.skywars;

import de.biomia.api.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class SkyWarsKillEvent extends SkyWarsEvent {

    private static final HandlerList list = new HandlerList();

    private final BiomiaPlayer killedPlayer;

    public SkyWarsKillEvent(BiomiaPlayer biomiaPlayer, BiomiaPlayer killedPlayer) {
        super(biomiaPlayer);
        this.killedPlayer = killedPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }
    public static HandlerList getHandlerList() {
        return list;
    }
    public BiomiaPlayer getKilledPlayer() {
        return killedPlayer;
    }
}
