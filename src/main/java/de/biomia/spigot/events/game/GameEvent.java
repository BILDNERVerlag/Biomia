package de.biomia.spigot.events.game;

import de.biomia.spigot.minigames.GameMode;
import org.bukkit.event.Event;

public abstract class GameEvent extends Event {

    private final GameMode mode;

    protected GameEvent(GameMode mode) {
        this.mode = mode;
    }

    public GameMode getMode() {
        return mode;
    }
}
