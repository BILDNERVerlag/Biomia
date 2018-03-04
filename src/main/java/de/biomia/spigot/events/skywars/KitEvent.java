package de.biomia.spigot.events.skywars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.BiomiaPlayerEvent;

abstract class KitEvent extends BiomiaPlayerEvent {

    private final int kitID;

    KitEvent(BiomiaPlayer biomiaPlayer, int kitID) {
        super(biomiaPlayer);
        this.kitID = kitID;
    }

    public int getKitID() {
        return kitID;
    }
}
