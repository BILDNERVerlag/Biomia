package de.biomiaAPI.achievements.statEvents.skywars;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.BiomiaPlayerEvent;

abstract class SkyWarsEvent extends BiomiaPlayerEvent {

    SkyWarsEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
}
