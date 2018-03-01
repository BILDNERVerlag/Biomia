package de.biomia.events;

import de.biomia.OfflineBiomiaPlayer;
import org.bukkit.event.Event;

public abstract class BiomiaPlayerEvent extends Event {

    private final OfflineBiomiaPlayer offlineBiomiaPlayer;

    protected BiomiaPlayerEvent(OfflineBiomiaPlayer offlineBiomiaPlayer) {
        this.offlineBiomiaPlayer = offlineBiomiaPlayer;
    }

    public OfflineBiomiaPlayer getBiomiaPlayer() {
        return offlineBiomiaPlayer;
    }
}
