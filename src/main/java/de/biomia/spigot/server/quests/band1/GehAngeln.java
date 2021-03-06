package de.biomia.spigot.server.quests.band1;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.server.quests.QuestEvents.AddCoinEvent;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.spigot.server.quests.general.DialogMessage;
import de.biomia.spigot.server.quests.general.Quest;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.biomia.spigot.server.quests.general.States;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GehAngeln implements Listener {

    private final Quest q = Biomia.getQuestManager().registerNewQuest("GehAngeln", 1);
    private final NPC pari;
    private DialogMessage startDialog;
    private DialogMessage whileQuesting;
    private DialogMessage afterQuesting;
    private DialogMessage nachQuest;

    public GehAngeln() {
        q.setInfoText(
                "Der begeisterte Angler Pari möchte gerne seine Familie mit gutem Fisch zum Abendessen überraschen, ist jedoch zu beschäftigt, "
                        + "um die Fische selbst zu holen. Dreimal darfst du raten, wer die 10 Fische an seiner Stelle holen soll.");
        q.setDisplayName("Geh Angeln!");

        pari = q.createNPC(EntityType.PLAYER, "Pari");

        Location loc_pari = new Location(Bukkit.getWorld("Quests"), 109.5, 68, -310.5, -85, 0);
        pari.spawn(loc_pari);
        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (pari.equals(e.getNPC())) {

            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    if (state == States.STATUS1) {
                        if (ItemConditions.hasItemInInventory(qp, Material.RAW_FISH, 10)) {
                            qp.setDialog(afterQuesting);
                        } else {
                            qp.setDialog(whileQuesting);
                        }
                    }
                } else if (qp.hasFinished(q)) {
                    qp.setDialog(nachQuest);
                } else {
                    qp.setDialog(startDialog);

                }

                qp.getDialog().execute(qp);
            }
        }
    }

    private void initDialog() {
        // start dl
        startDialog = new DialogMessage(q, pari).setInhalt(
                "Wie geht es dir? Schönes Wetter, nicht wahr? Ideal zum Angeln. Ich würde gerne selber gehen, aber ich muss noch so viel erledigen. Und Angeln dauert immer so lange. Kannst du mir zehn rohen Fisch besorgen?");
        startDialog.setFortsetzung("Dafür habe ich keine Zeit.").setFortsetzung("Ich versuche mein Glück!");
        startDialog.setNext("Schade. Komm wieder, wenn du es dir anders überlegst.", 0, pari);
        startDialog.setNext("Na dann los!", 1, pari);
        startDialog.getNext(1).addPlayerToQuest();

        // while questing
        whileQuesting = new DialogMessage(q, pari).setInhalt(
                "Es würe nett, wenn du dich beeilen könntest. Ich brauche die Fische wirklich dringend!");

        // after questing
        afterQuesting = new DialogMessage(q, pari).setInhalt(
                "Endlich! Nun kann die ganze Familie zum Essen kommen! Hier, die hab ich vor einiger Zeit aus dem Wasser gezogen! Aber ich weiß nicht, was ich damit anfangen soll. Vielleicht kannst du sie gebrauchen.");

        afterQuesting.addEvent(new AddCoinEvent(150))
                .addEvent(new TakeItemEvent(Material.RAW_FISH, 10))
                .addEvent(new GiveItemEvent(Material.BONE, 14));

        afterQuesting.finish();

        // nach quest
        nachQuest = new DialogMessage(q, pari).setInhalt("Danke für die Hilfe damals!");
    }
}
