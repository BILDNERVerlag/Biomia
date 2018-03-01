package de.biomia.server.quests.QuestEvents;

import de.biomia.Biomia;
import de.biomia.server.quests.general.QuestPlayer;

public class TakeCoinEvent implements Event {

    private final int coins;

    public TakeCoinEvent(int coins) {
        this.coins = coins;
    }

    @Override
    public void executeEvent(QuestPlayer qp) {
        Biomia.getBiomiaPlayer(qp.getPlayer()).takeCoins(coins);
    }

}
