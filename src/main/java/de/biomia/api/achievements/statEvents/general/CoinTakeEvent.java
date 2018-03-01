package de.biomia.api.achievements.statEvents.general;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.OfflineBiomiaPlayer;
import org.bukkit.event.HandlerList;

public class CoinTakeEvent extends CoinEvent {

    private static final HandlerList handlers = new HandlerList();

    public CoinTakeEvent(OfflineBiomiaPlayer biomiaPlayer, int amount) {
        super(biomiaPlayer, amount);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
