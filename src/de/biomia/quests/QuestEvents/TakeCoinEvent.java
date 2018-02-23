package de.biomia.quests.QuestEvents;

import de.biomiaAPI.Biomia;
import de.biomia.quests.general.QuestPlayer;

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
