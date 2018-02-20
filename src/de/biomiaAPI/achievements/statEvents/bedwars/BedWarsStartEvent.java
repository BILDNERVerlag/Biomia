package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class BedWarsStartEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final HashMap<BiomiaPlayer, String> biomiaPlayers;

    public BedWarsStartEvent(HashMap<BiomiaPlayer, String> biomiaPlayers) {
        this.biomiaPlayers = biomiaPlayers;
    }

    public HashMap<BiomiaPlayer, String> getPlayers() {
        return biomiaPlayers;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

}
