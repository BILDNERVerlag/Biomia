package de.biomia.api.achievements.statEvents;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.OfflineBiomiaPlayer;
import org.bukkit.event.Event;

public abstract class BiomiaPlayerEvent extends Event {

    private final OfflineBiomiaPlayer offlineBiomiaPlayer;

    public OfflineBiomiaPlayer getBiomiaPlayer() {
        return offlineBiomiaPlayer;
    }

    protected BiomiaPlayerEvent(OfflineBiomiaPlayer offlineBiomiaPlayer) {
        this.offlineBiomiaPlayer = offlineBiomiaPlayer;
    }
}
