package de.biomia.spigot.server.quests.QuestConditions;

import de.biomia.spigot.server.quests.general.QuestPlayer;

class QuestPercentageCondition {

    public boolean hasAtLeast(QuestPlayer qp, int mindestProzentZahl, int band) {
        return (qp.getQuestPercentage(band) >= mindestProzentZahl);
    }

}
