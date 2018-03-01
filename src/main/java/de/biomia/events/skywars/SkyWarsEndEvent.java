package de.biomia.events.skywars;

import de.biomia.BiomiaPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class SkyWarsEndEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final ArrayList<BiomiaPlayer> biomiaPlayerWinner;
    private final int durationInSeconds;
    private final String teamcolor;

    public SkyWarsEndEvent(ArrayList<BiomiaPlayer> biomiaPlayerWinners, int durationInSeconds, String teamcolor) {
        super();
        this.biomiaPlayerWinner = biomiaPlayerWinners;
        this.durationInSeconds = durationInSeconds;
        this.teamcolor = teamcolor;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public String getTeamcolor() {
        return teamcolor;
    }

    public ArrayList<BiomiaPlayer> getBiomiaPlayerWinner() {
        return biomiaPlayerWinner;
    }
}
