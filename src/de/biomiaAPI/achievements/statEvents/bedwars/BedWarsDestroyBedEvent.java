package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class BedWarsDestroyBedEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();
    private final Location blockHead;
    private final Location blockFoot;
    private final String teamcolor;

    public BedWarsDestroyBedEvent(BiomiaPlayer destroyer, Location blockHead, Location blockFoot, String teamcolor) {
        super(destroyer);
        this.blockFoot = blockFoot;
        this.blockHead = blockHead;
        this.teamcolor = teamcolor;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public Location getBlockFoot() {
        return blockFoot;
    }

    public Location getBlockHead() {
        return blockHead;
    }

    public String getTeamcolor() {
        return teamcolor;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

}
