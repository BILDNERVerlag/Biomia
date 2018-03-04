package de.biomia.spigot.events.skywars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.BiomiaPlayerEvent;

abstract class SkyWarsEvent extends BiomiaPlayerEvent {

    SkyWarsEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
}
