package de.biomia.quests.general;

import java.util.ArrayList;

public interface QuestManager {

	ArrayList<Quest> quests = new ArrayList<>();

	default ArrayList<Quest> getQuests() {
		return quests;
	}

	Quest getQuest(int questID);

	Quest getQuest(String questName);

	Quest registerNewQuest(String questName, int Band);

}
