package de.biomia.spigot.server.quests.sonstigeQuests;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.quests.QuestEvents.Event;
import de.biomia.spigot.server.quests.general.DialogMessage;
import de.biomia.spigot.server.quests.general.Quest;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.biomia.spigot.specialEvents.easterEvent.EasterEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Osterhase implements Listener {

    /*
     * Das hier ist keine "echte" Quest. Lediglich ein NPC, mit dem man sich
     * unterhalten kann und der einem ein bisschen Starthilfe auf dem Questserver
     * gibt, einem sagt, wie man Bugs reportet etc.
     */

    private final NPC osterhase;
    private final Quest q;
    private DialogMessage startDialog;

    public Osterhase() {
        q = Biomia.getQuestManager().registerNewQuest("Osterhase", 99);
        osterhase = q.createNPC(EntityType.RABBIT, "\u00A7cOsterhase");
        Location locAaron = new Location(Bukkit.getWorld("LobbyBiomia"), 534.5, 71, 226.5, -145, 0);
        osterhase.spawn(locAaron);

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
        startDialog = new DialogMessage(q, osterhase).setInhalt("Huiuiui, alle Eier verloren, so ein Ungl00dcck..\n" +
                "Oh, hast du etwa ein paar davon gefunden..?");
        startDialog.setFortsetzung("Wer bist du?").setFortsetzung("Ja, hab ich!").setFortsetzung("Nein, leider nicht.");
        startDialog.setNext("Ich bin der Osterhase! Aber ich habe fast alle meine sch00f6nen weissblauen Eier verloren... Bring sie mir wieder, aber nur genau solche wie die hier, die um mich herumliegen!", 0, osterhase)
                .setNext("Ach, und diese ganz besonderen, regenbogenfarbigen Eier gibt es auch noch.. Aber ich glaube nicht, dass du davon welche findest!", 0, osterhase);
        startDialog.setNext("Ach, tats00e4chlich..? Komm nochmal, sobald du mehr hast!", 1, osterhase).addEvent(giveRewardEvent);
        startDialog.setNext("Oh, schade...", 2, osterhase).setNext("Komm doch wieder, sobald du welche hast!", 0, osterhase);
        /*
        "ich hab alle meine ostereier verloren"
        "geschenke abholen"
        "smalltalken"
         */
    }

    // anonyme klasse, da erstens platzsparender und zweitens ermoeglicht referenz
// auf das ghost-objekt (das eigl ja nicht im event ist)
    private final Event giveRewardEvent = qp0 -> EasterEvent.giveReward(qp0.getBiomiaPlayer());

}
