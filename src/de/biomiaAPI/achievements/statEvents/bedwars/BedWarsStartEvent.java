package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.Teams.Teams;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class BedWarsStartEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final HashMap<BiomiaPlayer, Teams> biomiaPlayers;


    BedWarsStartEvent(HashMap<BiomiaPlayer, Teams> biomiaPlayers) {
        this.biomiaPlayers = biomiaPlayers;

    }

    public HashMap<BiomiaPlayer, Teams> getPlayers() {
        return biomiaPlayers;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

}
