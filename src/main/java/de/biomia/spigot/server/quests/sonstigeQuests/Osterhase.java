package de.biomia.spigot.server.quests.sonstigeQuests;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.quests.general.DialogMessage;
import de.biomia.spigot.server.quests.general.Quest;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class Osterhase implements Listener {

    /*
     * Das hier ist keine "echte" Quest. Lediglich ein NPC, mit dem man sich
     * unterhalten kann und der einem ein bisschen Starthilfe auf dem Questserver
     * gibt, einem sagt, wie man Bugs reportet etc.
     */

    private final NPC osterhase;
    private final Quest q;
    HashMap<UUID, Quest> hm = new HashMap<>();
    private DialogMessage startDialog;

    public Osterhase() {
        Bukkit.broadcastMessage("%%%%Ich bin der Osterman");
        q = Biomia.getQuestManager().registerNewQuest("Osterhase", 99);
        osterhase = q.createNPC(EntityType.RABBIT, "Osterhase");
        Location locAaron = new Location(Bukkit.getWorld("LobbyBiomia"), 534.5,71,226.5, -145, 0);
        osterhase.spawn(locAaron);
        Bukkit.broadcastMessage("%%%%%Spawned");

        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (osterhase.equals(e.getNPC())) {
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());
            if (qp.getDialog() == null) {
                qp.setDialog(startDialog);
                qp.getDialog().execute(qp);
            }
        }
    }

    private void initDialog() {
        startDialog = new DialogMessage(q, osterhase).setInhalt("Willkommen auf dem LobbyServer von Biomia!");
        startDialog.setNext("Hey ich bin der Ostermann, schau was ich an Ostern kann.",0,osterhase);
        /*
        heyhoe, ich bin der osterman
        schau was ich an ostern can

        "ich hab alle meine ostereier verloren"
        "geschenke abholen"
        "smalltalken"
         */
    }

}
