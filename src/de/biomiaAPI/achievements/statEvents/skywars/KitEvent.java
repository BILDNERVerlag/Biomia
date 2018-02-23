package de.biomiaAPI.achievements.statEvents.skywars;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.BiomiaPlayerEvent;

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