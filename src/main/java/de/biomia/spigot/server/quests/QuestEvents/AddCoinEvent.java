package de.biomia.spigot.server.quests.QuestEvents;

import de.biomia.spigot.BiomiaPlayer;

public class AddCoinEvent implements Event {

    private final int coins;

    public AddCoinEvent(int coins) {
        this.coins = coins;
    }

    private void addCoins(BiomiaPlayer qp, int coins) {
        qp.addCoins(coins, true);
    }

    @Override
    public void executeEvent(BiomiaPlayer qp) {
        addCoins(qp, coins);
    }

}
