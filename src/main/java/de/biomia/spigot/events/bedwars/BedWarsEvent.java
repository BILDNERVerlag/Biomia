package de.biomia.spigot.events.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.BiomiaPlayerEvent;

abstract class BedWarsEvent extends BiomiaPlayerEvent {

    BedWarsEvent(BiomiaPlayer biomiaPlayer) {
        super(biomiaPlayer);
    }
}

