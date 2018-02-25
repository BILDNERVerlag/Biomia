package de.biomia.api.achievements.statEvents.skywars;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.achievements.statEvents.BiomiaPlayerEvent;

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
