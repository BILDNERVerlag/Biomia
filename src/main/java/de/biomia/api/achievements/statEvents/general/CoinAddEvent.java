package de.biomia.api.achievements.statEvents.general;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.OfflineBiomiaPlayer;
import org.bukkit.event.HandlerList;

public class CoinAddEvent extends CoinEvent {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public CoinAddEvent(OfflineBiomiaPlayer offlineBiomiaPlayer, int amount) {
        super(offlineBiomiaPlayer, amount);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
