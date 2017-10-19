package de.biomiaAPI.QuestEvents;

import de.biomiaAPI.Quests.QuestPlayer;

public abstract interface Event {
	
	public void executeEvent(QuestPlayer qp);

}
