package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class BedWarsEndEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final ArrayList<BiomiaPlayer> biomiaPlayerWinner;
    private final int durationInSeconds;
    private final String teamcolor;


    public BedWarsEndEvent(ArrayList<BiomiaPlayer> biomiaPlayerWinners, int durationInSeconds, String teamcolor) {
        this.biomiaPlayerWinner = biomiaPlayerWinners;
        this.durationInSeconds = durationInSeconds;
        this.teamcolor = teamcolor;
    }

    public ArrayList<BiomiaPlayer> getBiomiaPlayerIDWinner() {
        return biomiaPlayerWinner;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public String getTeamcolor() {
        return teamcolor;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
