package de.biomia.spigot.events.game.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.game.BiomiaPlayerGameEvent;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.general.shop.ShopItem;
import org.bukkit.event.HandlerList;

public class BedWarsBuyItemEvent extends BiomiaPlayerGameEvent {

    private static final HandlerList list = new HandlerList();
    private final ShopItem shopItem;
    private final int amount;

    public BedWarsBuyItemEvent(BiomiaPlayer bp, ShopItem shopItem, int amount, GameMode mode) {
        super(bp, mode);
        this.shopItem = shopItem;
        this.amount = amount;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public int getAmount() {
        return amount;
    }

    public ShopItem getItem() {
        return shopItem;
    }
}
