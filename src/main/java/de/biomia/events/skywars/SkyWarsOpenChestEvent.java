package de.biomia.events.skywars;

import de.biomia.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class SkyWarsOpenChestEvent extends SkyWarsEvent {

    private static final HandlerList list = new HandlerList();
    private final boolean isFirstOpen;
    private final ChestType chestType;

    public SkyWarsOpenChestEvent(BiomiaPlayer biomiaPlayer, boolean isFirstOpen, ChestType type) {
        super(biomiaPlayer);
        this.isFirstOpen = isFirstOpen;
        this.chestType = type;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public boolean isFirstOpen() {
        return isFirstOpen;
    }

    public ChestType getChestType() {
        return chestType;
    }

    public enum ChestType {
        GOOD_Chest, NORMAL_Chest
    }
}