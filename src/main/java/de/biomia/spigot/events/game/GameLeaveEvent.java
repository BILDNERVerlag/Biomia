package de.biomia.spigot.events.game;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.GameMode;
import org.bukkit.event.HandlerList;


public class GameLeaveEvent extends BiomiaPlayerGameEvent {

    private static final HandlerList handlerList = new HandlerList();

    public GameLeaveEvent(BiomiaPlayer bp, GameMode mode) {
        super(bp, mode);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
