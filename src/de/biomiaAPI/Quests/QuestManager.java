package de.biomiaAPI.Quests;

import java.util.ArrayList;

@SuppressWarnings("SameReturnValue")
public interface QuestManager {

	ArrayList<Quest> quests = new ArrayList<>();

	ArrayList<Quest> getQuests();

	Quest getQuest(String quest);

	Quest registerNewQuest(String questName, int Band);

}
