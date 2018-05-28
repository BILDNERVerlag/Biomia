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

public class AufDerSucheNachGlueck implements Listener {
    private final Quest q = Biomia.getQuestManager().registerNewQuest("AufDerSucheNachGl00fcck", 1);
    private final NPC npc;
    private DialogMessage startDialog;
    private DialogMessage vorQuest;
    private DialogMessage withoutRabbitFoot;
    private DialogMessage withRabbitFoot;
    private DialogMessage nachQuest;

    public AufDerSucheNachGlueck() {
        q.setInfoText(
                "Lato ist vom Ungl00fcck verfolgt. Er m00f6chte dies 00e4ndern und bittet dich darum, ihm eine Hasenpfote zu beschaffen.");
        q.setDisplayName("Suche nach Gl00fcck");
        npc = q.createNPC(EntityType.PLAYER, "Lato");
        Location loc = new Location(Bukkit.getWorld("Quests"), 139, 64, -235.5, 17, 0);
        npc.spawn(loc);
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
                            if (ItemConditions.hasItemInInventory(qp, Material.RABBIT_FOOT, 1))
                                qp.setDialog(withRabbitFoot);
                            else
                                qp.setDialog(withoutRabbitFoot);
                            break;
                        case START:
                            qp.setDialog(vorQuest);
                            break;
                        case ENDE:
                            qp.setDialog(nachQuest);
                            break;
                        default:
                            break;
                    }
                } else
                    qp.setDialog(startDialog);
            } else
                qp.setDialog(startDialog);

            qp.getDialog().execute(qp);
        }
    }

    private void initDialog() {
        // start dl
        startDialog = new DialogMessage(q, npc).setInhalt(
                "Hallo, mein Freund! Kennst du auch solche Tage, an denen nichts klappt? Mir geht es schon l00e4nger so. Einfach nervig. Ich habe schon alles versucht, aber nichts hilft. Meine letzte Hoffnung ist eine Hasenpfote. Die soll angeblich die schlimmste Pechstr00e4hne verfliegen lassen. Willst du mir helfen?");
        startDialog.setFortsetzung("Ja, mache ich!").setFortsetzung("Nein.")
                .setNext("Vielen Dank! Wei00dft du denn 00fcberhaupt, wie man eine Hasenpfote bekommt?", 0, npc);
        startDialog.setNext("Bitte hilf mir doch!", 1, npc);
        startDialog.getNext(0).setFortsetzung("Ja, ich wei00df.").setFortsetzung("Nein.")
                .setNext("Das ist gut. Wenn du mir die Hasenpfote bringst, bekommst du auch eine Belohnung von mir.", 0,
                        npc)
                .addPlayerToQuest();
        startDialog.getNext(0).setNext(
                "Du musst nur einen Hasen erledigen. Wenn du Gl00fcck hast, bekommst du dann die Hasenpfote. Aber pass auf! Hasen sind sehr schnell! Du sollst auch nicht leer ausgehen. Wenn du es schaffst, bekommst du von mir ein Geschenk.",
                1, npc).addPlayerToQuest();

        // without rabbit foot
        withoutRabbitFoot = new DialogMessage(q, npc).setInhalt(
                "Oh, schade. Du hast sie noch nicht. Ich hoffe, ich habe dich nicht mit meinem Pech angesteckt. Versuch es bitte weiter!");

        // with rabbit foot
        withRabbitFoot = new DialogMessage(q, npc).setInhalt(
                "Unglaublich! Vielen Dank! Die Hasenpfote werde ich stets bei mir tragen. Nun wird alles wieder gut werden. Als Belohnung gebe ich dir meine Angel.");
        withRabbitFoot.addEvent(new TakeItemEvent(Material.RABBIT_FOOT, 1));

        withRabbitFoot.addEvent(new AddCoinEvent(150));
        withRabbitFoot.addEvent(new GiveItemEvent(Material.FISHING_ROD, 1));

        withRabbitFoot.finish();

        // vor quest
        vorQuest = new DialogMessage(q, npc).setInhalt("Da bist du da wieder! Hast du es dir 00fcberlegt?");
        vorQuest.setFortsetzung("Ja, ich mach es!").setFortsetzung("Nein.")
                .setNext("Vielen Dank! Wei00dft du denn 00fcberhaupt, wie man eine Hasenpfote bekommt?", 0, npc);
        vorQuest.setNext("Bitte hilf mir doch!", 1, npc);
        vorQuest.getNext(1).updatePlayerState(States.START);
        vorQuest.getNext(0).setFortsetzung("Ja, ich wei00df").setFortsetzung("Nein").setNext(
                "Das ist gut. Wenn du mir die Hasenpfote bringst, bekommst du auch eine Belohnung von mir.", 0, npc);
        vorQuest.getNext(0).setNext(
                "Du musst nur einen Hasen erledigen. Wenn du Gl00fcck hast, bekommst du dann die Hasenpfote. Aber pass auf! Hasen sind sehr schnell! Du sollst auch nicht leer ausgehen. Wenn du es schaffst, bekommst du von mir ein Geschenk.",
                1, npc).addPlayerToQuest();
        nachQuest = new DialogMessage(q, npc).setInhalt("Danke dir.");
    }
}
