package de.biomia.spigot.minigames;

import de.biomia.spigot.events.game.GameDeathEvent;
import de.biomia.spigot.events.game.GameEndEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

class GameRewardHandler implements Listener {

    @EventHandler
    public void onKill(GameKillEvent e) {
        if (e.isFinalKill())
            e.getOfflineBiomiaPlayer().addCoins(GameRewards.KILL.getReward(e.getOfflineBiomiaPlayer(), e.getMode().getInstance()), true);
    }

    @EventHandler
    public void onEnd(GameEndEvent e) {
        e.getWinner().forEach(each -> {
            int coins = GameRewards.WIN.getReward(each, e.getMode().getInstance());
            if (each.getTeam().lives(each))
                coins += GameRewards.PLAYED.getReward(each, e.getMode().getInstance());
            each.addCoins(coins, true);
        });
    }

    @EventHandler
    public void onPlayed(GameDeathEvent e) {
        if (e.isFinalDeath())
            e.getOfflineBiomiaPlayer().addCoins(GameRewards.PLAYED.getReward(e.getOfflineBiomiaPlayer(), e.getMode().getInstance()), true);
    }
}
