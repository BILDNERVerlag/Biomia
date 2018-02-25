package de.biomia.api.achievements.statEvents.bedwars;

import de.biomia.api.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsUseShopEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();

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
