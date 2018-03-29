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
        Location locAaron = new Location(Bukkit.getWorld("BiomiaWelt"), -35, 68, -204, 30, 5);
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
                "Im nord\u00f6stlich von hier liegenden Dorf findest du verschiedene NPCs, die mit Aufgaben auf dich warten. Bitte "
                        + "denk daran, dass der Server sich noch im Aufbau befindet und es sein kann, dass manche coins noch nicht ganz "
                        + "funktionieren oder in sp\u00fcteren Updates ver\u00fcndert werden.",
                0, aaron)
                .setNext(
                        "Falls du Verbesserungsvorschl\u00fcge hast, \u00A7cwende dich gerne an das Team\u00A76! Verwende bitte die \u00A7c/report \u00A76Funktion,"
                                + " falls du auf Fehler triffst - oder Spieler, die sich unangemessen verhalten!",
                        0, aaron);
    }

}
