package de.biomia.events.skywars;

import de.biomia.BiomiaPlayer;
import de.biomia.events.BiomiaPlayerEvent;

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
