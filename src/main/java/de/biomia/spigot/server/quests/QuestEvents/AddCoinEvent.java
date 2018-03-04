package de.biomia.spigot.server.quests.QuestEvents;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.quests.general.QuestPlayer;

public class AddCoinEvent implements Event {

    private final int coins;

    public AddCoinEvent(int coins) {
        this.coins = coins;
    }

    private void addCoins(QuestPlayer qp, int coins) {
        Biomia.getBiomiaPlayer(qp.getPlayer()).addCoins(coins, true);
    }

    @Override
    public void executeEvent(QuestPlayer qp) {
        addCoins(qp, coins);
    }

}
