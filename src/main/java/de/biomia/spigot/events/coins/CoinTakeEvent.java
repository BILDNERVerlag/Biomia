package de.biomia.spigot.events.coins;

import de.biomia.spigot.OfflineBiomiaPlayer;
import org.bukkit.event.HandlerList;

public class CoinTakeEvent extends CoinEvent {

    private static final HandlerList handlers = new HandlerList();

    public CoinTakeEvent(OfflineBiomiaPlayer biomiaPlayer, int amount) {
        super(biomiaPlayer, amount);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
