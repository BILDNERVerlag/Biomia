package de.biomia.spigot.server.quests.band1;

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

public class Intro implements Listener {

    /*
     * Das hier ist keine "echte" Quest. Lediglich ein NPC, mit dem man sich
     * unterhalten kann und der einem ein bisschen Starthilfe auf dem Questserver
     * gibt, einem sagt, wie man Bugs reportet etc.
     */

    private final NPC aaron;
    private final Quest q;
    private DialogMessage startDialog;

    public Intro() {
        q = Biomia.getQuestManager().registerNewQuest("Intro", 99);
        aaron = q.createNPC(EntityType.PLAYER, "DerAaron");
        Location locAaron = new Location(Bukkit.getWorld("Quests"), -35, 68, -204, 30, 5);
        aaron.spawn(locAaron);

        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (aaron.equals(e.getNPC())) {
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());
            if (qp.getDialog() == null) {
                qp.setDialog(startDialog);
                qp.getDialog().execute(qp);
            }
        }
    }

    private void initDialog() {
        startDialog = new DialogMessage(q, aaron).setInhalt("Willkommen auf dem QuestServer von Biomia!");
        startDialog.setNext(
                "Bitte denk daran, dass der Server sich noch im Aufbau befindet und Quests sich in der Zukunft stets ver00e4ndern k\u00f6nnen.\n" +
                        "Ansonsten, spring einfach 00fcber den Bach hinter mir und halte dich rechts, du solltest das Dorf schon sehen k00f6nnen, bestimmt hat dort jemand Aufgaben f00fcr dich! ",
                0, aaron)
                .setNext(
                        "Ach, am Rande... Falls du Verbesserungsvorschl00e4ge hast, sprich ruhig das BIOMIA-Team an und verwende die \u00A7c/report\u00A7b-Funktion,"
                                + " falls du auf Fehler triffst - oder Spieler, die sich unangemessen verhalten!",
                        0, aaron);
    }

}
