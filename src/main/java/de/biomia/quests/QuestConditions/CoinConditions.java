package de.biomia.quests.QuestConditions;

import de.biomia.api.Biomia;
import de.biomia.quests.general.QuestPlayer;

class CoinConditions {

	public boolean hasAtLeast(QuestPlayer qp, int amount) {
		return (Biomia.getBiomiaPlayer(qp.getPlayer()).getCoins() >= amount);
	}
	
	public boolean hasLessThan(QuestPlayer qp, int amount) {
		return (Biomia.getBiomiaPlayer(qp.getPlayer()).getCoins() < amount);
	}

}
