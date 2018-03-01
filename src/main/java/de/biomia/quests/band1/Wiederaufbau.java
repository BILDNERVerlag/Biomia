package de.biomia.quests.band1;

import de.biomia.api.Biomia;
import de.biomia.quests.QuestConditions.ItemConditions;
import de.biomia.quests.QuestEvents.AddCoinEvent;
import de.biomia.quests.QuestEvents.TakeItemEvent;
import de.biomia.quests.general.DialogMessage;
import de.biomia.quests.general.Quest;
import de.biomia.quests.general.QuestPlayer;
import de.biomia.quests.general.States;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Wiederaufbau implements Listener {

    private final Material itemZuBesorgen = Material.COBBLESTONE;
    private final String questName = "Wiederaufbau";

    private final Quest q = Biomia.getQuestManager().registerNewQuest(questName, 1);
    private final NPC npc;

    private DialogMessage startDialog;
    private DialogMessage comeBackWithItem;
    private DialogMessage comeBackWithoutItem;
    private DialogMessage nachQuest;

    public Wiederaufbau() {
        q.setInfoText(
                "Der pflichtbewusste B\u00fcrger Jonathan sorgt sich, dass das Dorf nicht gut genug gegen Monsterangriffe ger\u00fcstet ist. Er "
                        + "m\u00f6chte die Verteidigungslinien st\u00e4rken und braucht dazu 32 Bruchstein.");
        String npcName = "Jonathan";
        npc = q.createNPC(EntityType.PLAYER, npcName);
        Location npcLoc = new Location(Bukkit.getWorld("Quests"), 96.5, 73, -268.5, 160, 0);
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
                        int itemMenge = 32;
                        if (ItemConditions.hasItemInInventory(qp, itemZuBesorgen, itemMenge))
                            qp.setDialog(comeBackWithItem);
                        else
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
        startDialog = new DialogMessage(q, npc);
        startDialog.setInhalt(
                "Hmm.. Es kommt mir fast so vor, als w\u00fcrden die Zombies und Skelette von Tag zu Tag weniger werden... Vielleicht ist jetzt der richtige Zeitpunkt, um ein bisschen mit dem Wiederaufbau unseres Dorfes anzufangen?");
        startDialog.setFortsetzung("Wiederaufbau? War hier etwa mal mehr?").setFortsetzung("Klingt zu anstrengend.");
        startDialog.setNext(
                "Was f\u00fcr eine Frage! Erst vor ein paar Jahren noch haben sich von hier auf Felder erstreckt, so weit das Auge nur reichte! Und naja, es war auch einfach ein bisschen mehr los. Damals wohnten noch mehr Leute hier.",
                0, npc);
        startDialog.setNext("Wie bitte? Daf\u00fcr wirst du dir ja wohl wirklich Zeit nehmen k\u00f6nnen... Grrmpf...",
                1, npc);
        startDialog.getNext(0).setFortsetzung("Was genau hast du jetzt vor?")
                .setFortsetzung("Brauchst du daf\u00fcr Geld?");
        startDialog.getNext(0).setNext(
                "Naja, ich m\u00f6chte zumindest mal damit anfangen, unsere Felder wieder auf Vordermann zu bringen! K\u00f6nntest du, sagen wir.. 32 Bruchstein besorgen?",
                0, npc);
        startDialog.getNext(0).setNext(
                "Was? Nein, \u00e4h, also schon - aber nein, schon gut. Wichtiger ist brauchbares Baumaterial. K\u00f6nntest du mir - sagen wir mal - zwei Stacks Bruchstein bringen?",
                1, npc);
        startDialog.getNext(0).getNext(0).setFortsetzung("Jap, mache ich!").setFortsetzung("Ein ander Mal vielleicht!");
        startDialog.getNext(0).getNext(1).setFortsetzung("Jap, mache ich!").setFortsetzung("Ein ander Mal vielleicht!");
        startDialog.getNext(0).getNext(0).setNext("Super! Komm wieder, wenn du alles hast!", 0, npc).addPlayerToQuest();
        startDialog.getNext(0).getNext(0).setNext("Oh... Schade. Aber ich verstehe schon. Bis dann!", 1, npc);
        startDialog.getNext(0).getNext(1).setNext("Super! Komm wieder, wenn du alles hast!", 0, npc).addPlayerToQuest();
        startDialog.getNext(0).getNext(1).setNext("Oh... Schade. Aber ich verstehe schon. Bis dann!", 1, npc);

        // comeback wo
        comeBackWithoutItem = new DialogMessage(q, npc).setInhalt(
                "Bitte beeil dich mit dem Bruchstein! Der Winter naht und die N\u00e4chte sind kalt und voller Schrecken!");

        // comeback w
        comeBackWithItem = new DialogMessage(q, npc)
                .setInhalt("So! Das hat ja auch wirklich lange genug gedauert! Vielen Dank, gut gemacht!");
        comeBackWithItem.addEvent(new AddCoinEvent(150)).addEvent(new TakeItemEvent(Material.COBBLESTONE, 32));
        comeBackWithItem.finish();

        // nach quest
        nachQuest = new DialogMessage(q, npc).setInhalt(
                "Vielen, vielen Dank nochmal f\u00fcr die Hilfe mit all den Steinen! Das hat uns allen wirklich den Allerwertesten gerettet!");
    }

}
