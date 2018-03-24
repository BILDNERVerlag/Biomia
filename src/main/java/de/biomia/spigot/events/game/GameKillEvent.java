package de.biomia.spigot.events.game;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.GameMode;
import org.bukkit.event.HandlerList;

public class GameKillEvent extends BiomiaPlayerGameEvent {

    private static final HandlerList handlerList = new HandlerList();

    private final BiomiaPlayer killedPlayer;
    private final boolean finalKill;

    public GameKillEvent(BiomiaPlayer bp, BiomiaPlayer killedPlayer, boolean finalKill, GameMode mode) {
        super(bp, mode);
        this.killedPlayer = killedPlayer;
        this.finalKill = finalKill;
    }

    public boolean isFinalKill() {
        return finalKill;
    }

    public BiomiaPlayer getKilledPlayer() {
        return killedPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
