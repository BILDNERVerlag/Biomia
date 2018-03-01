package de.biomia.events.bedwars;

import de.biomia.BiomiaPlayer;
import de.biomia.events.BiomiaPlayerEvent;

abstract class BedWarsEvent extends BiomiaPlayerEvent {

    BedWarsEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
}

