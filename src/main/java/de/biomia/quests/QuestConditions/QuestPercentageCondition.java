package de.biomia.quests.QuestConditions;

import de.biomia.quests.general.QuestPlayer;

class QuestPercentageCondition {

	public boolean hasAtLeast(QuestPlayer qp, int mindestProzentZahl, int band) {
		return (qp.getQuestPercentage(band) >= mindestProzentZahl);
	}
	
}
