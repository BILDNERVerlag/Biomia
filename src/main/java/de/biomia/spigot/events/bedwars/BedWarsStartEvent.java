package de.biomia.spigot.events.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class BedWarsStartEvent extends Event {

    private static final HandlerList list = new HandlerList();
    private final HashMap<BiomiaPlayer, TeamColor> biomiaPlayers;

    public BedWarsStartEvent(HashMap<BiomiaPlayer, TeamColor> biomiaPlayers) {
        this.biomiaPlayers = biomiaPlayers;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    public HashMap<BiomiaPlayer, TeamColor> getPlayers() {
        return biomiaPlayers;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

}
