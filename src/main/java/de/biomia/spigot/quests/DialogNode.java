package de.biomia.spigot.quests;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.server.quests.QuestEvents.Event;
import de.biomia.universal.Messages;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

import java.util.ArrayList;

public class DialogNode {

    private final String message;
    private final ArrayList<Event> events;
    @Getter
    private final DialogNodeConnector dnc;
    @Getter
    @Setter
    private Entity npc;
    @Getter
    private final String option;

    public DialogNode(String option, String message) {
        this(option, message, null);
    }

    public DialogNode(String message, Entity npc) {
        this(null, message, npc);
    }

    public DialogNode(String option, String message, Entity npc) {
        this.events = new ArrayList<>();
        this.message = message;
        this.option = option;
        this.npc = npc;
        dnc = new DialogNodeConnector();
    }

    public DialogNode addNode(String option, String message) {
        return dnc.addNode(option, message, null);
    }

    public DialogNode addNode(String option, String message, Entity entity) {
        return dnc.addNode(option, message, entity);
    }

    public void execute(BiomiaPlayer bp) {
        bp.setDnc(null);
        String message = (npc == null) ? Messages.format(this.message) : Messages.format(npc.getName() + ": " + this.message);
        bp.sendMessage(message);
        events.forEach(e -> e.executeEvent(bp));
        dnc.execute(bp);
    }

    public void addEvent(Event event) {
        events.add(event);
    }

}
