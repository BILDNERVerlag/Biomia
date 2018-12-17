package de.biomia.spigot.quests;

import de.biomia.spigot.server.quests.QuestEvents.AddCoinEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

class Wasserholen extends Quest {

    public Wasserholen() {
        super(Band.Band1, "Wasserholen");
        Entity elsa = registerNpc("Elsa", EntityType.VILLAGER, 104.5, 73, -268.5);
        DialogNode startNode = new DialogNode("Hallo Ian! Willst du mir bei etwas helfen?", elsa);
        setStartNode(startNode);

        startNode.addNode("Ja", "Hier, nimm 50 Biomia Coins.").addEvent(new AddCoinEvent(50));
        startNode.addNode("Nein", "Menno.", elsa);
        DialogNode option3 = startNode.addNode("Vielleicht", "Was soll das heißen?");

        option3.addNode("Option 1", "x");
        option3.addNode("Option 2", "y", elsa);
    }
}
