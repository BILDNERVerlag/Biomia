package de.biomia.spigot.events.game.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.game.BiomiaPlayerGameEvent;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.event.HandlerList;

public class BedWarsDestroyBedEvent extends BiomiaPlayerGameEvent {

    private static final HandlerList list = new HandlerList();
    private final TeamColor teamcolor;

    public BedWarsDestroyBedEvent(BiomiaPlayer destroyer, TeamColor teamcolor, GameMode mode) {
        super(destroyer, mode);
        this.teamcolor = teamcolor;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public TeamColor getTeamColor() {
        return teamcolor;
    }

}
