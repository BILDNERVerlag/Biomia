package de.biomiaAPI.achievements.statEvents;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.Event;

public abstract class BiomiaPlayerEvent extends Event {

    protected final BiomiaPlayer biomiaPlayer;

    public BiomiaPlayer getBiomiaPlayer() {
        return biomiaPlayer;
    }

    public BiomiaPlayerEvent(BiomiaPlayer biomiaPlayer) {
        this.biomiaPlayer = biomiaPlayer;
    }
}
