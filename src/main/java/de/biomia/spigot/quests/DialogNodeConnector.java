package de.biomia.spigot.quests;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.universal.Messages;

import java.util.ArrayList;

public class DialogNodeConnector {

    ArrayList<DialogNode> nodes;

    public DialogNodeConnector() {
        nodes = new ArrayList<>();
    }

    public void execute(BiomiaPlayer bp) {
        //show options
        if (nodes.size() == 1) {
            nodes.get(0).execute(bp);
            return;
        }
        for (int i = 0; i < nodes.size(); i++) {
            DialogNode node = nodes.get(i);
            bp.sendMessage(Messages.format("%s. %s%s", i, Messages.COLOR_AUX, node.getOption()));
        }
        bp.setDnc(this);
    }

}
