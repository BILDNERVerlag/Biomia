package de.biomia.spigot.events.bedwars;

import de.biomia.spigot.minigames.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedWarsStartEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final GameMode mode;

    public BedWarsStartEvent(GameMode mode) {
        this.mode = mode;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    public GameMode getMode() {
        return mode;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

}
