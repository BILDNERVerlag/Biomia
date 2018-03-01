package de.biomia.server.quests.QuestConditions;

import de.biomia.server.quests.general.QuestPlayer;

class QuestPercentageCondition {

	public boolean hasAtLeast(QuestPlayer qp, int mindestProzentZahl, int band) {
		return (qp.getQuestPercentage(band) >= mindestProzentZahl);
	}
	
}
