package de.biomiaAPI.QuestEvents;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.Quests.QuestPlayer;

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
