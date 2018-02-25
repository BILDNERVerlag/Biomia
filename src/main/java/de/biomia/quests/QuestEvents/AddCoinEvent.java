package de.biomia.quests.QuestEvents;

import de.biomia.api.Biomia;
import de.biomia.quests.general.QuestPlayer;

public class AddCoinEvent implements Event{
	
	private final int coins;
	
	public AddCoinEvent(int coins) {
		this.coins = coins;
	}

	private void addCoins(QuestPlayer qp, int coins) {
		Biomia.getBiomiaPlayer(qp.getPlayer()).addCoins(coins, true);
	}

	@Override
	public void executeEvent(QuestPlayer qp) {
		addCoins(qp,coins);	
	}

}
