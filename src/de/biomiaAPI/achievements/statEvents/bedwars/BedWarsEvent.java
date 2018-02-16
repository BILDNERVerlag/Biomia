package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.BiomiaPlayerEvent;

abstract class BedWarsEvent extends BiomiaPlayerEvent {

    BedWarsEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
}

