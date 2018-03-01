package de.biomia.events.bedwars;

import de.biomia.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class BedWarsUseShopEvent extends BedWarsEvent {

    private static final HandlerList list = new HandlerList();

    private final boolean isVillager;

    public BedWarsUseShopEvent(BiomiaPlayer biomiaPlayer, boolean isVillager) {
        super(biomiaPlayer);
        this.isVillager = isVillager;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public boolean isVillager() {
        return isVillager;
    }
}
