package de.biomiaAPI.achievements.statEvents.general;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.BiomiaPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import sun.dc.pr.PRError;

public abstract class CoinEvent extends BiomiaPlayerEvent implements Cancellable {

    private final int amount;
    private boolean cancelled = false;

    CoinEvent(BiomiaPlayer biomiaPlayer, int amount) {
        super(biomiaPlayer);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
