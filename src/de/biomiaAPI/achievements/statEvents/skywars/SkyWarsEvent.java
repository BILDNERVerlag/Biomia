package de.biomiaAPI.achievements.statEvents.skywars;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.BiomiaPlayerEvent;

public abstract class SkyWarsEvent extends BiomiaPlayerEvent {

    public SkyWarsEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
}
