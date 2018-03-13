package de.biomia.spigot.events.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsDestroyBedEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();
    private final String teamcolor;

    public BedWarsDestroyBedEvent(BiomiaPlayer destroyer, String teamcolor) {
        super(destroyer);
        this.teamcolor = teamcolor;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public String getTeamcolor() {
        return teamcolor;
    }

}
