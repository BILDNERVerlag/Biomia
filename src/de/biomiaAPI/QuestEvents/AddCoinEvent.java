package de.biomiaAPI.QuestEvents;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.Quests.QuestPlayer;

public class AddCoinEvent implements Event{
	
	int coins;
	
	public AddCoinEvent(int coins) {
		this.coins = coins;
	}

	public void addCoins(QuestPlayer qp, int coins) {
		Biomia.getBiomiaPlayer(qp.getPlayer()).addCoins(coins);
	}

	@Override
	public void executeEvent(QuestPlayer qp) {
		addCoins(qp,coins);	
	}

}
