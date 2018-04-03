package de.biomia.spigot.server.quests.QuestEvents;

import de.biomia.spigot.BiomiaPlayer;

public class TakeCoinEvent implements Event {

    private final int coins;

    public TakeCoinEvent(int coins) {
        this.coins = coins;
    }

    @Override
    public void executeEvent(BiomiaPlayer qp) {
        qp.takeCoins(coins);
    }

}
