package de.biomia.spigot.server.quests.QuestEvents;

import de.biomia.spigot.BiomiaPlayer;

public class AddCoinEvent implements Event {

    private final int coins;

    public AddCoinEvent(int coins) {
        this.coins = coins;
    }

    private void addCoins(BiomiaPlayer bp, int coins) {
        bp.addCoins(coins, true);
    }

    @Override
    public void executeEvent(BiomiaPlayer bp) {
        addCoins(bp, coins);
    }

}
