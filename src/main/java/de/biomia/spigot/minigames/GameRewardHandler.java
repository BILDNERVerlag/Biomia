package de.biomia.spigot.minigames;

import de.biomia.spigot.events.game.GameDeathEvent;
import de.biomia.spigot.events.game.GameEndEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameRewardHandler implements Listener {
    @EventHandler
    public void onKill(GameKillEvent e) {
        if (e.isFinalKill())
            GameRewards.KILL.giveReward(e.getOfflineBiomiaPlayer(), e.getMode().getInstance());
    }

    @EventHandler
    public void onWin(GameEndEvent e) {
        e.getWinner().forEach(each -> {
            GameRewards.WIN.giveReward(each, e.getMode().getInstance());
            if (each.getTeam().lives(each))
                GameRewards.PLAYED.giveReward(each, e.getMode().getInstance());
        });
    }

    @EventHandler
    public void onPlayed(GameDeathEvent e) {
        if (e.isFinalDeath())
            GameRewards.PLAYED.giveReward(e.getOfflineBiomiaPlayer(), e.getMode().getInstance());
    }
}
