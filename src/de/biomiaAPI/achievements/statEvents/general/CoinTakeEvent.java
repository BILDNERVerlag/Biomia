package de.biomiaAPI.achievements.statEvents.general;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class CoinTakeEvent extends CoinEvent {

    private static final HandlerList handlers = new HandlerList();

    public CoinTakeEvent(BiomiaPlayer biomiaPlayer, int amount) {
        super(biomiaPlayer, amount);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
