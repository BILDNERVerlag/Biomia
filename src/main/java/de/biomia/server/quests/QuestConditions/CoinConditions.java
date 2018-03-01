package de.biomia.server.quests.QuestConditions;

import de.biomia.Biomia;
import de.biomia.server.quests.general.QuestPlayer;

class CoinConditions {

    public boolean hasAtLeast(QuestPlayer qp, int amount) {
        return (Biomia.getBiomiaPlayer(qp.getPlayer()).getCoins() >= amount);
    }

    public boolean hasLessThan(QuestPlayer qp, int amount) {
        return (Biomia.getBiomiaPlayer(qp.getPlayer()).getCoins() < amount);
    }

}
