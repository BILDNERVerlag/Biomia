package de.biomiaAPI.achievements.statEvents.skywars;

import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.event.HandlerList;

public class SkyWarsOpenChestEvent extends SkyWarsEvent {

    public enum ChestType {
        GOOD_Chest, NORMAL_Chest
    }

    private final boolean isFirstOpen;
    private final ChestType chestType;

    private static final HandlerList list = new HandlerList();

    public SkyWarsOpenChestEvent(BiomiaPlayer biomiaPlayer, boolean isFirstOpen, ChestType type) {
        super(biomiaPlayer);
        this.isFirstOpen = isFirstOpen;
        this.chestType = type;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public boolean isFirstOpen() {
        return isFirstOpen;
    }
    public static HandlerList getHandlerList() {
        return list;
    }
    public ChestType getChestType() {
        return chestType;
    }
}