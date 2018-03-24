package de.biomia.spigot.events.game.skywars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.game.BiomiaPlayerGameEvent;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.general.kits.Kit;
import org.bukkit.event.HandlerList;

public class KitEvent extends BiomiaPlayerGameEvent {

    private static final HandlerList handlerList = new HandlerList();

    private final Kit kit;

    private final KitEventType type;

    public KitEvent(BiomiaPlayer biomiaPlayer, Kit kit, KitEventType type, GameMode mode) {
        super(biomiaPlayer, mode);
        this.kit = kit;
        this.type = type;
    }

    public Kit getKit() {
        return kit;
    }

    public KitEventType getType() {
        return type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public enum KitEventType {
        BUY, USE, SHOW
    }

}
