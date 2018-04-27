package de.biomia.spigot.minigames;

import de.biomia.spigot.events.game.GameEndEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import de.biomia.spigot.events.game.GameLeaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameRewardHandler implements Listener {
    @EventHandler
    public void onKill(GameKillEvent e) {
        if (e.isFinalKill())
            GameRewards.KILL.giveReward(e.getOfflineBiomiaPlayer().getBiomiaPlayer(), e.getMode().getInstance());
    }

    @EventHandler
    public void onWin(GameEndEvent e) {
        e.getWinner().forEach(each -> {
            GameRewards.WIN.giveReward(each, e.getMode().getInstance());
            GameRewards.PLAYED.giveReward(each, e.getMode().getInstance());
        });
    }

    @EventHandler
    public void onPlayed(GameLeaveEvent e) {
        GameRewards.PLAYED.giveReward(e.getOfflineBiomiaPlayer().getBiomiaPlayer(), e.getMode().getInstance());
    }
}
