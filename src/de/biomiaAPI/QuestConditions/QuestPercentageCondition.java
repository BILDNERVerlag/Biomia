package de.biomiaAPI.QuestConditions;

import de.biomiaAPI.Quests.QuestPlayer;

class QuestPercentageCondition {

	public boolean hasAtLeast(QuestPlayer qp, int mindestProzentZahl, int band) {
		return (qp.getQuestPercentage(band) >= mindestProzentZahl);
	}
	
}
