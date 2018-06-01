package de.biomia.spigot.quests;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.universal.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.UUID;

public class DialogNodeConnector {

    private final ArrayList<DialogNode> nodes;
    private final UUID uuid;

    public DialogNodeConnector() {
        nodes = new ArrayList<>();
        uuid = UUID.randomUUID();
    }

    public void execute(BiomiaPlayer bp) {
        //show options
        if (nodes.size() == 1) {
            nodes.get(0).execute(bp);
            return;
        }
        for (int i = 0; i < nodes.size(); i++) {
            DialogNode node = nodes.get(i);
            TextComponent component = new TextComponent(Messages.format("%s. %s%s", i, Messages.COLOR_AUX, node.getOption()));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/q %s_%s", uuid.toString(), i)));
            bp.getPlayer().spigot().sendMessage(component);
        }
        bp.setDnc(this);
    }

    public void execute(BiomiaPlayer bp, int index, UUID uuid) {
        if (this.uuid.equals(uuid) && index < nodes.size()){
            nodes.get(index).execute(bp);
        }
    }

}
