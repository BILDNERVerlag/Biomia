package de.biomiaAPI.achievements.statEvents;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CoinEvent extends Event implements Cancellable {

    private final HandlerList handlers = new HandlerList();
    private final int amount;
    private boolean addCoins, cancelled;
    private final BiomiaPlayer biomiaPlayer;

    public CoinEvent(int amount0, boolean addCoins0, BiomiaPlayer bp0) {
        amount = amount0;
        addCoins = addCoins0;
        biomiaPlayer = bp0;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public int getAmount() {
        return amount;
    }

    public boolean addCoins() {
        return addCoins;
    }

    public boolean takeCoins() {
        return !addCoins;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
    cancelled = b;
    }

    public BiomiaPlayer getBiomiaPlayer() {
        return biomiaPlayer;
    }
}
