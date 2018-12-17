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

public class Kuerbissuche implements Listener {
    private final Quest q = Biomia.getQuestManager().registerNewQuest("Kürbissuche", 1);
    private final NPC korbinian;
    private DialogMessage startDialog;
    private DialogMessage comeBackWPumpkin;
    private DialogMessage comeBackWOPumpkin;
    private DialogMessage nachQuest;

    public Kuerbissuche() {
        q.setInfoText(
                "Korbinian ist bereits voll mit den Vorbereitungen für das nächste Halloween-Event beschäftigt und benötigt einen Kürbis, um eine gruselige Laterne daraus zu machen.");
        korbinian = q.createNPC(EntityType.PLAYER, "Korbinian");
        Location loc = new Location(Bukkit.getWorld("Quests"), 97.5, 70, -305.5, -60, 0);
        korbinian.spawn(loc);
        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (korbinian.equals(e.getNPC())) {

            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    if (state == States.STATUS1) {
                        if (ItemConditions.hasItemInInventory(qp, Material.PUMPKIN, 1))
                            qp.setDialog(comeBackWPumpkin);
                        else
                            qp.setDialog(comeBackWOPumpkin);
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
        startDialog = new DialogMessage(q, korbinian)
                .setInhalt("He, du da! Kannst du einen Kürbis für mich auftreiben?!");
        startDialog.setFortsetzung("Okay, ich mache es.").setFortsetzung("Was springt für mich dabei raus?");
        startDialog.setNext(
                "Das wird super! Ich kann es kaum abwarten, meine eigene Kürbislaterne in den Händen zu halten!",
                0, korbinian);
        startDialog.setNext("Naja, irgendwas werde ich schon finden, das ich dir als Belohnung andrehen kann, nicht?",
                1, korbinian).setFortsetzung("Na gut...").setFortsetzung("Nein!");

        startDialog.getNext(1).setNext("Dann los, los!", 0, korbinian);
        startDialog.getNext(1).setNext("Ich mag dich nicht.", 1, korbinian);

        startDialog.getNext(0).addEvent(qp -> {
            qp.getQuestPlayer().addMineableBlock(Material.PUMPKIN);
            qp.getQuestPlayer().getBuildableBlocks().add(Material.PUMPKIN_SEEDS);
            qp.getQuestPlayer().getBuildableBlocks().add(Material.PUMPKIN_STEM);
        }).addPlayerToQuest();

        startDialog.getNext(1).getNext(0).addPlayerToQuest();

        // wo pumpkin
        comeBackWOPumpkin = new DialogMessage(q, korbinian).setInhalt("Wo bleibt mein Kürbis?");

        // w pumpkin
        comeBackWPumpkin = new DialogMessage(q, korbinian)
                .setInhalt("Oh, was für ein Prachtexexmplar! Sehr gut, jetzt verschwinde!");
        comeBackWPumpkin.addEvent(new TakeItemEvent(Material.PUMPKIN, 1));
        comeBackWPumpkin.addEvent(qp -> {
            qp.getQuestPlayer().removeMineableBlock(Material.PUMPKIN);
            qp.getQuestPlayer().getBuildableBlocks().remove(Material.PUMPKIN_SEEDS);
            qp.getQuestPlayer().getBuildableBlocks().remove(Material.PUMPKIN_STEM);
        }).finish();
        comeBackWPumpkin.setFortsetzung("Okay...").setFortsetzung("Hey, nicht so schnell!")
                .setNext("Hm? Ach. Meinetwegen, hier ist deine Belohnung.", 1, korbinian);
        comeBackWPumpkin.getNext(1).addEvent(new GiveItemEvent(Material.PUMPKIN_SEEDS, 10))
                .addEvent(new AddCoinEvent(150));

        // nach quest
        nachQuest = new DialogMessage(q, korbinian).setInhalt(
                "Ich bin so stolz auf den großartigen Kürbis, den du mir besorgt hast. Danke nochmal!");
    }
}
