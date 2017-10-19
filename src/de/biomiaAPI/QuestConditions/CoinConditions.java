package de.biomiaAPI.QuestConditions;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.Quests.QuestPlayer;

public class CoinConditions {

	public boolean hasAtLeast(QuestPlayer qp, int amount) {
		return (Biomia.getBiomiaPlayer(qp.getPlayer()).getCoins() >= amount);
	}
	
	public boolean hasLessThan(QuestPlayer qp, int amount) {
		return (Biomia.getBiomiaPlayer(qp.getPlayer()).getCoins() < amount);
	}

}
