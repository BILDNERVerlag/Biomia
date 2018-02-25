package de.biomia.api.achievements.statEvents;

import de.biomia.api.BiomiaPlayer;
import org.bukkit.event.Event;

public abstract class BiomiaPlayerEvent extends Event {

    private final BiomiaPlayer biomiaPlayer;

    public BiomiaPlayer getBiomiaPlayer() {
        return biomiaPlayer;
    }

    protected BiomiaPlayerEvent(BiomiaPlayer biomiaPlayer) {
        this.biomiaPlayer = biomiaPlayer;
    }
}
