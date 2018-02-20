package de.biomiaAPI.achievements.statEvents.bedwars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsUseShopEvent extends BedWarsEvent {

    public static final HandlerList list = new HandlerList();

    private final boolean isVillager;

    public BedWarsUseShopEvent(BiomiaPlayer biomiaPlayer, boolean isVillager) {
        super(biomiaPlayer);
        this.isVillager = isVillager;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public boolean isVillager() {
        return isVillager;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
