package de.biomia.spigot.events.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class BedWarsStartEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final HashMap<BiomiaPlayer, String> biomiaPlayers;

    public BedWarsStartEvent(HashMap<BiomiaPlayer, String> biomiaPlayers) {
        this.biomiaPlayers = biomiaPlayers;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    public HashMap<BiomiaPlayer, String> getPlayers() {
        return biomiaPlayers;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

}