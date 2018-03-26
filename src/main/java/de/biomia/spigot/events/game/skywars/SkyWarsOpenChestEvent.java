package de.biomia.spigot.events.game.skywars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.game.BiomiaPlayerGameEvent;
import de.biomia.spigot.minigames.GameMode;
import org.bukkit.event.HandlerList;

public class SkyWarsOpenChestEvent extends BiomiaPlayerGameEvent {

    private static final HandlerList list = new HandlerList();
    private final boolean isFirstOpen;
    private final ChestType chestType;

    public SkyWarsOpenChestEvent(BiomiaPlayer bp, boolean isFirstOpen, ChestType type, GameMode mode) {
        super(bp, mode);
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
        GoodChest, NormalChest
    }
}