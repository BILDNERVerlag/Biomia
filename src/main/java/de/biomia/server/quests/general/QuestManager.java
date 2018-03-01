package de.biomia.server.quests.general;

import java.util.ArrayList;

public class QuestManager {

    private final ArrayList<Quest> quests = new ArrayList<>();

    public Quest getQuest(int questID) {
        for (Quest q : quests) {
            if (q.getQuestID() == questID) {
                return q;
            }
        }
        return null;
    }

    public Quest getQuest(String questName) {
        for (Quest q : quests) {
            if (q.getQuestName().equals(questName)) {
                return q;
            }
        }
        return null;
    }

    public Quest registerNewQuest(String questName, int bandNummer) {
        return new Quest(questName, bandNummer);

    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

}
