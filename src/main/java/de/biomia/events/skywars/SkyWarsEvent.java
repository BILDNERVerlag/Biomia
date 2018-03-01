package de.biomia.events.skywars;

import de.biomia.BiomiaPlayer;
import de.biomia.events.BiomiaPlayerEvent;

abstract class SkyWarsEvent extends BiomiaPlayerEvent {

    SkyWarsEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
}
