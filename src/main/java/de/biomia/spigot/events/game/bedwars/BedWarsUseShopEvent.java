package de.biomia.spigot.events.game.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.game.BiomiaPlayerGameEvent;
import de.biomia.spigot.minigames.GameMode;
import org.bukkit.event.HandlerList;

public class BedWarsUseShopEvent extends BiomiaPlayerGameEvent {

    private static final HandlerList list = new HandlerList();

    private final boolean isVillager;

    public BedWarsUseShopEvent(BiomiaPlayer bp, boolean isVillager, GameMode mode) {
        super(bp, mode);
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
