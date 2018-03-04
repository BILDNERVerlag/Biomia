package de.biomia.spigot.server.quests.QuestConditions;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.quests.general.QuestPlayer;

class CoinConditions {

    public boolean hasAtLeast(QuestPlayer qp, int amount) {
        return (Biomia.getBiomiaPlayer(qp.getPlayer()).getCoins() >= amount);
    }

    public boolean hasLessThan(QuestPlayer qp, int amount) {
        return (Biomia.getBiomiaPlayer(qp.getPlayer()).getCoins() < amount);
    }

}
