package de.biomia.spigot.events.game;

import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.events.BiomiaPlayerEvent;
import de.biomia.spigot.minigames.GameMode;

public abstract class BiomiaPlayerGameEvent extends BiomiaPlayerEvent {

    private final GameMode mode;

    protected BiomiaPlayerGameEvent(OfflineBiomiaPlayer bp, GameMode mode) {
        super(bp);
        this.mode = mode;
    }

    public GameMode getMode() {
        return mode;
    }
}
