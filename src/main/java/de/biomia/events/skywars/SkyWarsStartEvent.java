package de.biomia.events.skywars;

import de.biomia.BiomiaPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class SkyWarsStartEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final HashMap<BiomiaPlayer, Integer> biomiaPlayers;

    public SkyWarsStartEvent(HashMap<BiomiaPlayer, Integer> biomiaPlayers) {
        this.biomiaPlayers = biomiaPlayers;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    public HashMap<BiomiaPlayer, Integer> getPlayers() {
        return biomiaPlayers;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

}
