package de.biomiaAPI.achievements.statEvents.bedwars;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedWarsEndEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final int winnerBiomiaID;
    private final int durationInSeconds;


    BedWarsEndEvent(int winnerBiomiaID, int durationInSeconds) {
        this.winnerBiomiaID = winnerBiomiaID;
        this.durationInSeconds = durationInSeconds;
    }

    public int getBiomiaPlayerIDWinner() {
        return winnerBiomiaID;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }
}
