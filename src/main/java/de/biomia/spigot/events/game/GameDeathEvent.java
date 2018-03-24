package de.biomia.spigot.events.game;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.GameMode;
import org.bukkit.event.HandlerList;

public class GameDeathEvent extends BiomiaPlayerGameEvent {

    private static final HandlerList handlerList = new HandlerList();

    private BiomiaPlayer killer;
    private boolean finalDeath;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public GameDeathEvent(BiomiaPlayer bp, BiomiaPlayer killer, boolean finalDeath, GameMode mode) {
        super(bp, mode);
        this.killer = killer;
        this.finalDeath = finalDeath;
    }

    public boolean isFinalDeath() {
        return finalDeath;
    }

    public BiomiaPlayer getKiller() {
        return killer;
    }
}
