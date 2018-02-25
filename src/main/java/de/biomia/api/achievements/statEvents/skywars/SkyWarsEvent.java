package de.biomia.api.achievements.statEvents.skywars;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.achievements.statEvents.BiomiaPlayerEvent;

abstract class SkyWarsEvent extends BiomiaPlayerEvent {

    SkyWarsEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
}
