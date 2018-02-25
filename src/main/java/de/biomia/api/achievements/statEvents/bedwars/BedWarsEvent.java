package de.biomia.api.achievements.statEvents.bedwars;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.achievements.statEvents.BiomiaPlayerEvent;

abstract class BedWarsEvent extends BiomiaPlayerEvent {

    BedWarsEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
}

