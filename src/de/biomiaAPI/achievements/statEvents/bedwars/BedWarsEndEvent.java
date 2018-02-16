package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.Teams.Teams;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class BedWarsEndEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final ArrayList<Integer> winnerBiomiaID;
    private final int durationInSeconds;
    private final Teams teamcolor;


    BedWarsEndEvent(ArrayList<Integer> winnerBiomiaIDs, int durationInSeconds, Teams teamcolor) {
        this.winnerBiomiaID = winnerBiomiaIDs;
        this.durationInSeconds = durationInSeconds;
        this.teamcolor = teamcolor;
    }

    public ArrayList<Integer> getBiomiaPlayerIDWinner() {
        return winnerBiomiaID;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public Teams getTeamcolor() {
        return teamcolor;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }
}
