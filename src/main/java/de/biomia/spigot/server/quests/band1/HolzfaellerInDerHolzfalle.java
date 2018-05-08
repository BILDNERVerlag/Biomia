package de.biomia.spigot.server.quests.band1;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.server.quests.QuestEvents.AddCoinEvent;
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

public class HolzfaellerInDerHolzfalle implements Listener {

    private final Material log = Material.LOG;

    private final String questName = "InDerHolzfalle";

    private final Quest q = Biomia.getQuestManager().registerNewQuest(questName, 1);
    private final NPC npc;
    private DialogMessage startDialog;
    private DialogMessage comeBackWithItem;
    private DialogMessage comeBackWithoutItem;
    private DialogMessage nachQuest;

    public HolzfaellerInDerHolzfalle() {
        q.setInfoText(
                "Ein Forscher ist der Ansicht, dass es zu viele Bäume in der Gegend gibt. Du sollst ein paar davon fällen und ihm dann 64 Holzstücke der selben Sorte als Beweis zeigen.");
        q.setDisplayName("Kein Brett vorm Kopf");
        String npcName = "Logan";
        npc = q.createNPC(EntityType.PLAYER, npcName);
        Location npcLoc = new Location(Bukkit.getWorld("Quests"), 84.5, 70, -281.5, -25, 0);
        npc.spawn(npcLoc);
        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (npc.equals(e.getNPC())) {

            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    switch (state) {
                        case STATUS1:
                            int itemMenge = 64;
                            if (ItemConditions.hasItemInInventory(qp, log, itemMenge)) {
                                qp.setDialog(comeBackWithItem);
                            } else if (ItemConditions.hasItemInInventory(qp, Material.LOG_2, itemMenge)) {
                                qp.setDialog(comeBackWithItem);
                            } else
                                qp.setDialog(comeBackWithoutItem);
                            break;
                        default:
                            break;
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
        startDialog = new DialogMessage(q, npc);
        startDialog.setInhalt("Oh nein, oh nein... Wer hätte so etwas gedacht?");
        startDialog.setFortsetzung("Was ist denn los?").setFortsetzung("(Da halte ich mich am besten raus!)");
        startDialog.setNext(
                "Was los ist? Willst du das wirklich wissen? Wir haben ein gewaltiges Problem, das ist los! Es gibt zu viele Bäume!",
                0, npc);
        startDialog.setNext("Mir wird schon noch irgendwas einfallen... ", 1, npc);
        startDialog.getNext(0).setFortsetzung("Kann ich helfen?").setFortsetzung("Wie kann das sein?");
        startDialog.getNext(0).setNext(
                "Hilf-äh-wie bitte? Du würdest mir helfen? Natürlich! Fäll einfach ein paar Bäume, und dann komm mit 64 Baumstämmen von der selben Sorte wieder und du kannst dir bei mir eine Belohnung abholen!",
                0, npc);
        startDialog.getNext(0).setNext(
                "Nunja, ich habe komplizierte Berechnungen angestellt, die Anzahl der Kühe durch unsere Höhe über dem Meeresspiegel multipliziert.. Wie auch immer. Hilfst du mir, Bäume zu fällen? Wenn du 64 Blöcke fällst, sollte das vorerst genügen.",
                1, npc);
        startDialog.getNext(0).getNext(0).setFortsetzung("Jap, mache ich!").setFortsetzung("Ein ander Mal vielleicht!");
        startDialog.getNext(0).getNext(1).setFortsetzung("Jap, mache ich!").setFortsetzung("Ein ander Mal vielleicht!");
        startDialog.getNext(0).getNext(0).setNext(
                "Super! Komm wieder, wenn du alles hast! Ach ja - und bitte leg dich auf eine bestimmte Holzsorte fest. Bei so vielen unterschiedlichen Sorten fällt mir das Nachzählen immer so schwer...",
                0, npc).addPlayerToQuest();
        startDialog.getNext(0).getNext(0).setNext("Oh... Schade. Aber ich verstehe schon. Bis dann!", 1, npc);
        startDialog.getNext(0).getNext(1).setNext(
                "Super! Komm wieder, wenn du alles hast! Ach ja - und bitte leg dich auf eine bestimmte Holzsorte fest. Bei so vielen unterschiedlichen Sorten fällt mir das Nachzählen immer so schwer...",
                0, npc).addPlayerToQuest();
        startDialog.getNext(0).getNext(1).setNext("Oh... Schade. Aber ich verstehe schon. Bis dann!", 1, npc);

        // comeback wo
        comeBackWithoutItem = new DialogMessage(q, npc)
                .setInhalt("Bitte beeil dich mit dem Holz! Du willst doch nicht, dass hier alles verurwaldet!");

        // comeback w
        comeBackWithItem = new DialogMessage(q, npc).setInhalt(
                "So! Du hast es also tatsächlich hinter dich gebracht! Großartig! Vielen Dank, dankeschön!");
        comeBackWithItem.addEvent(new AddCoinEvent(250));
        comeBackWithItem.finish();

        // nach quest
        nachQuest = new DialogMessage(q, npc).setInhalt(
                "Oh, du bists! Danke dir nochmal für die Hilfe mit dem Holz! Ich dachte schon, es käme alle Hilfe zu spät!");
    }

}
