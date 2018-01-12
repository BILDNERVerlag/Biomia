package de.biomiaAPI.QuestEvents;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.Quests.QuestPlayer;

class TakeCoinEvent implements Event {

	private final int coins;

	public TakeCoinEvent(int coins) {
		this.coins = coins;
	}

	@Override
	public void executeEvent(QuestPlayer qp) {
		Biomia.getBiomiaPlayer(qp.getPlayer()).takeCoins(coins);
	}

}
