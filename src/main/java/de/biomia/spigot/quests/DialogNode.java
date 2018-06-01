package de.biomia.spigot.quests;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.server.quests.QuestEvents.Event;
import de.biomia.universal.Messages;
import lombok.Getter;

import java.util.ArrayList;

public class DialogNode {

    private final String message;
    private final ArrayList<Event> events;
    private final DialogNodeConnector dnc;
    @Getter
    private final String option;

    public DialogNode(String message, String option, DialogNodeConnector dnc) {
        this.events = new ArrayList<>();
        this.message = message;
        this.dnc = dnc;
        this.option = option;
    }

    public DialogNode(String message, DialogNodeConnector dnc) {
        this(message, null, dnc);
    }

    public void execute(BiomiaPlayer bp) {
        bp.sendMessage(Messages.format(message));
        events.forEach(e -> e.executeEvent(bp));
        dnc.execute(bp);
    }

    public void registerEvent(Event event) {
        events.add(event);
    }

}
