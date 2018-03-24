package de.biomia.spigot.events.game;


import de.biomia.spigot.minigames.GameMode;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends GameEvent {

    private static final HandlerList handlerList = new HandlerList();

    public GameStartEvent(GameMode mode) {
        super(mode);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
