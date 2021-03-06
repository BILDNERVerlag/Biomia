package de.biomia.spigot.server.quests.band1;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.server.quests.QuestEvents.AddCoinEvent;
import de.biomia.spigot.server.quests.QuestEvents.Event;
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

public class Geheimnis implements Listener {

    private final Quest q = Biomia.getQuestManager().registerNewQuest("Geheimnis", 1);
    private final NPC nana;
    private DialogMessage startDialog;
    private DialogMessage comeBackWStroh;
    private DialogMessage comeBackWOStroh;
    private DialogMessage nachQuest;

    public Geheimnis() {
        q.setInfoText(
                "Nana hätte im Austausch gegen Informationen gerne einen Strohballen. über das 'warum' kannst du nur spekulieren.");
        nana = q.createNPC(EntityType.PLAYER, "Nana");
        Location loc = new Location(Bukkit.getWorld("Quests"), 130, 65, -326, 0, 0);
        nana.spawn(loc);
        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (nana.equals(e.getNPC())) {

            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    if (state == States.STATUS1) {
                        if (ItemConditions.hasItemInInventory(qp, Material.HAY_BLOCK, 1))
                            qp.setDialog(comeBackWStroh);
                        else
                            qp.setDialog(comeBackWOStroh);
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
        // event declaration
        Event wheatMineableEvent = qp -> {

            qp.getQuestPlayer().addMineableBlock(Material.CROPS);
            qp.getQuestPlayer().getBuildableBlocks().add(Material.SEEDS);
            qp.getQuestPlayer().getBuildableBlocks().add(Material.CROPS);
        };

        // start dl
        startDialog = new DialogMessage(q, nana)
                .setInhalt("Ein Fremder! Geh aufs Feld außerhalb des Dorfes und bring mir einen Strohballen!");
        startDialog.setFortsetzung("Okay, ich mache es.").setFortsetzung("Nein, keine Chance");
        startDialog.setNext("Sehr gut! Ihr Unwissenden seid so leicht zu überzeugen. Wie praktisch.", 0, nana);
        startDialog.setNext("Gemeinheit! Na gut, wenn du es machst, dann verrate ich dir ein Geheimnis.", 1, nana)
                .setFortsetzung("Na gut...").setFortsetzung("Nein!");

        startDialog.getNext(1).setNext(
                "Dann los! Worauf wartest du? Hier hast du Samen. Ich kenne mich mit Feldarbeit nicht aus, wenn du Wasser dafür brauchst dann geh zu Elsa, die hat immer einen Eimer übrig.",
                0, nana);
        startDialog.getNext(1).setNext("Ich mag dich nicht", 1, nana);

        startDialog.getNext(0).addEvent(new GiveItemEvent(Material.SEEDS, 9)).addEvent(wheatMineableEvent)
                .addPlayerToQuest();

        startDialog.getNext(1).getNext(0).addEvent(new GiveItemEvent(Material.SEEDS, 9)).addEvent(wheatMineableEvent)
                .addPlayerToQuest();

        // come back wo stroh
        comeBackWOStroh = new DialogMessage(q, nana).setInhalt(
                "Wo bleibt mein Stroh? Ich brauche einen ganzen Ballen um den Schaden beim Fallen… ach das geht dich nichts an! Bring es mir einfach!");

        // come back w stroh
        comeBackWStroh = new DialogMessage(q, nana).setInhalt(
                "Sehr gut jetzt kann ich aus großer Höhe… ich meine mein Vater wird stolz auf mich sein, weil ich so fleißig auf dem Feld gearbeitet habe. Was willst du noch? Verschwinde!");
        comeBackWStroh.addEvent(new TakeItemEvent(Material.HAY_BLOCK, 1))

                .addEvent(new AddCoinEvent(200)).addEvent(qp -> {
            qp.getQuestPlayer().getMineableBlocks().remove(Material.CROPS);
            qp.getQuestPlayer().getBuildableBlocks().remove(Material.SEEDS);
            qp.getQuestPlayer().getBuildableBlocks().remove(Material.CROPS);
        }).finish();
        comeBackWStroh.setFortsetzung("Ok.").setFortsetzung("Nein!").setNext(
                "Meinetwegen, für deine Mühe verrate ich dir ein Geheimnis. Dein Freund war hier, Ian. Meine Schwester Nene wird ihn töten.",
                1, nana);

        // nach quest
        nachQuest = new DialogMessage(q, nana)
                .setInhalt("Dein Freund war hier, Ian. Meine Schwester Nene wird ihn töten.");
    }
}
