package de.biomia.quests.general;

import java.util.ArrayList;

public class QuestManager {

    ArrayList<Quest> quests = new ArrayList<>();

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
        Quest q = new Quest(questName, bandNummer);
        quests.add(q);
        q.registerQuestIfnotExist();
        return q;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

}
