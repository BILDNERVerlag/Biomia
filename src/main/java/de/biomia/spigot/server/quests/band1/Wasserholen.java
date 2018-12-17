package de.biomia.spigot.server.quests.band1;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.server.quests.QuestEvents.AddCoinEvent;
import de.biomia.spigot.server.quests.QuestEvents.Event;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.spigot.server.quests.Quests;
import de.biomia.spigot.server.quests.general.DialogMessage;
import de.biomia.spigot.server.quests.general.Quest;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.biomia.spigot.server.quests.general.States;
import de.biomia.universal.Time;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Wasserholen implements Listener {
    private final Quest q = Biomia.getQuestManager().registerNewQuest("Wasserholen", 1);
    private final NPC elsa;

    private DialogMessage dialog_Start;
    private DialogMessage withoutWater;
    private DialogMessage withWater;
    private DialogMessage nachQuest;

    public Wasserholen() {
        q.setInfoText(
                "Elsa bittet dich nicht darum, im nächsten Schneeköniginnenwettbewerb für sie abzustimmen, sondern möchte lediglich, dass du ihr drei volle Eimer Wasser bringst.");

        elsa = q.createNPC(EntityType.PLAYER, "Elsa");
        Location loc = new Location(Bukkit.getWorld("Quests"), 104.5, 73, -268.5, 180, 0);
        elsa.spawn(loc);

        q.setRepeatable(true);
        q.setCooldown(1, Time.Tage);
        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (elsa.equals(e.getNPC())) {
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    if (state == States.STATUS1) {
                        if (ItemConditions.hasItemInInventory(qp, Material.WATER_BUCKET, 3))
                            qp.setDialog(withWater);
                        else
                            qp.setDialog(withoutWater);
                    }
                } else {
                    Quests.restartQuestIfTimeOver(qp, q, dialog_Start, nachQuest);
                }
                qp.getDialog().execute(qp);
            }
        }
    }

    private void initDialog() {
        Event wasserErlaubt = qp -> qp.getQuestPlayer().getMineableBlocks().add(Material.WATER_BUCKET);
        Event wasserVerbot = qp -> qp.getQuestPlayer().getMineableBlocks().remove(Material.WATER_BUCKET);

        // start dl
        dialog_Start = new DialogMessage(q, elsa).setInhalt("Brian und Falto sind schon wieder verschwunden."
                + " Ich muss diese Unruhestifter finden und hab keine Zeit um Wasser zu holen."
                + " Du wirst das für mich erledigen."
                + " Hier hast du ein paar Eimer. Die müssen alle bis zum Rand voll werden!");
        dialog_Start.addPlayerToQuest();
        dialog_Start.updatePlayerState(States.STATUS1);
        dialog_Start.addEvent(new GiveItemEvent(Material.BUCKET, 3)).addEvent(wasserErlaubt);

        // wo water
        withoutWater = new DialogMessage(q, elsa).setInhalt("Bitte hole mir Wasser, ich brauche es dringend!");

        // w water
        withWater = new DialogMessage(q, elsa).setInhalt(
                "Die beiden Jungs sind noch immer verschwunden. Na warte, wenn sie nach Hause kommen, werde ich ihnen dieses Wasser über den Kopf schütten! Danke für deine Mühe, als Belohnung darfst du einen der Eimer behalten.");
        withWater.addEvent(new TakeItemEvent(Material.WATER_BUCKET, 3)).addEvent(new GiveItemEvent(Material.BUCKET, 1))
                .addEvent(new AddCoinEvent(150)).addEvent(wasserVerbot).finish();

        // nach quest
        nachQuest = new DialogMessage(q, elsa).setInhalt(
                "Die beiden Jungs sind noch immer verschwunden. Angeblich hat sie jemand bei den Schweinegehegen gesehen. Ich will gar nicht wissen, was sie jetzt schon wieder aushecken!");
    }
}
