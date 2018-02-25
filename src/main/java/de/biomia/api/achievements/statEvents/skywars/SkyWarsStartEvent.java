package de.biomia.api.achievements.statEvents.skywars;

import de.biomia.api.BiomiaPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class SkyWarsStartEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final HashMap<BiomiaPlayer, Integer> biomiaPlayers;

    public SkyWarsStartEvent(HashMap<BiomiaPlayer, Integer> biomiaPlayers) {
        this.biomiaPlayers = biomiaPlayers;
    }

    public HashMap<BiomiaPlayer, Integer> getPlayers() {
        return biomiaPlayers;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

}
