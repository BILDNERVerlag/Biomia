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

public class BlumenFuerDieGeliebte implements Listener {
    private final Quest q = Biomia.getQuestManager().registerNewQuest("BlumenGeliebte", 1);
    private final NPC roman;
    private final NPC julchen;
    private DialogMessage startDialogRoman;
    private DialogMessage startDialogJulchen;
    private DialogMessage nachQuestJulchen;
    private DialogMessage nachQuestRoman;
    private DialogMessage comeBackWRose;
    private DialogMessage comeBackWORose;
    private DialogMessage mehrAufmerksamkeit;
    private DialogMessage julchenRose;
    private DialogMessage schonGebracht;
    private DialogMessage nachJulchenKeineRose;
    private DialogMessage nachJulchenMitRose;
    private DialogMessage platzhalter;

    public BlumenFuerDieGeliebte() {
        q.setInfoText(
                "Roman ist Hals über Kopf in Julchen verliebt. Weil er zu schüchtern ist, um sie selbst anzusprechen, sollst du für seinen Schwarm eine rote Rose besorgen.");
        roman = q.createNPC(EntityType.PLAYER, "Roman");
        julchen = q.createNPC(EntityType.PLAYER, "Julchen");
        Location locRoman = new Location(Bukkit.getWorld("Quests"), 126, 70, -311, -22, 0);
        roman.spawn(locRoman);
        Location locJulchen = new Location(Bukkit.getWorld("Quests"), 133.5, 71, -295.5, 90, 0);
        julchen.spawn(locJulchen);
        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (roman.equals(e.getNPC())) {
            // ROMAN
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    switch (state) {
                        case STATUS1:
                            if (ItemConditions.hasItemInInventory(qp, Material.RED_ROSE, 1)) {
                                qp.setDialog(comeBackWRose);
                                qp.updateState(q, States.STATUS2);
                            } else
                                qp.setDialog(comeBackWORose);
                            break;

                        case STATUS2:
                            qp.setDialog(schonGebracht);
                            break;
                        case STATUS3:
                            if (ItemConditions.hasItemInInventory(qp, Material.RED_ROSE, 1)) {
                                qp.setDialog(nachJulchenMitRose);
                            } else
                                qp.setDialog(nachJulchenKeineRose);
                            break;
                        default:
                            break;
                    }
                } else if (!qp.hasFinished(q)) {
                    qp.setDialog(startDialogRoman);
                } else {
                    qp.setDialog(nachQuestRoman);
                }
                qp.getDialog().execute(qp);
            }
        } else if (julchen.equals(e.getNPC())) {
            // JULCHEN
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    switch (state) {
                        case STATUS1:
                            qp.setDialog(mehrAufmerksamkeit);
                            break;
                        case STATUS2:
                            if (ItemConditions.hasItemInInventory(qp, Material.RED_ROSE, 1)) {
                                qp.setDialog(julchenRose);
                                qp.updateState(q, States.STATUS3);
                            } else
                                qp.setDialog(mehrAufmerksamkeit);
                            break;
                        case STATUS3:
                            qp.setDialog(platzhalter);
                            break;
                        default:
                            break;
                    }
                } else if (qp.hasFinished(q)) {
                    qp.setDialog(nachQuestJulchen);
                } else {
                    qp.setDialog(startDialogJulchen);

                }
                qp.getDialog().execute(qp);
            }

        }
    }

    private void initDialog() {
        // schon gebracht
        schonGebracht = new DialogMessage(q, roman)
                .setInhalt("Hast du sie ihr schon gebracht? Nein? Dann mach mal hinne!");

        // nach julchen ohne rose
        nachJulchenKeineRose = new DialogMessage(q, roman)
                .setInhalt("Du hast ihr die Rose gegeben? Super! Hier ist deine Belohnung! Und, was hat sie gesagt?")
                .setFortsetzung("Sie hat ihr suuuper gefallen.").setFortsetzung("Also, naja...");
        nachJulchenKeineRose.setNext("Oh? Das ist ja großartig! Vielen, vielen Dank!", 0, roman);
        nachJulchenKeineRose.setNext("Oh... Ich verstehe schon... Naja, danke trotzdem.", 1, roman);

        Event e = qp -> qp.getQuestPlayer().getMineableBlocks().remove(Material.RED_ROSE);

        nachJulchenKeineRose.getNext(0).addEvent(e).finish();
        nachJulchenKeineRose.getNext(1).addEvent(e).finish();

        // nach julchen mit rose
        nachJulchenMitRose = new DialogMessage(q, roman)
                .setInhalt("Oh, du hast sie noch? Hast du dich etwa nicht getraut, sie ihr zu geben?")
                .setFortsetzung("Sie hat gesagt, sie hätte lieber Schmuck und Edelsteine.");
        nachJulchenMitRose.setNext("...", 0, roman).setNext("...", 0, roman)
                .setNext("Ich danke dir dennoch. Und ich hab dir eine Belohnung versprochen. Hier, nimm.", 0, roman)

                .addEvent(new GiveItemEvent(Material.LEATHER_CHESTPLATE, 1)).addEvent(new AddCoinEvent(150))

                .finish();

        // start dl
        // ROMAN
        startDialogRoman = new DialogMessage(q, roman)
                .setInhalt("Hey, du! Kannst du... mir bei einer Kleinigkeit helfen?")
                .setFortsetzung("Klar, um was gehts?").setFortsetzung("Nein, keine Zeit");
        startDialogRoman.setNext(
                "Naja, es geht um mein Julchen. Sie ist immer so abweisend zu mir, deshalb möchte ich ihr Herz mit einer roten Rose erobern!",
                0, roman).setFortsetzung("Lass mich raten, ich soll dir so eine besorgen?")
                .setFortsetzung("Gute Idee, aber ohne mich!");
        startDialogRoman.setNext("Oh... Naja, in Ordnung...", 1, roman);

        startDialogRoman.getNext(0).setNext(
                "Du meldest dich freiwillig? Wunderbar! Dann beeil dich und komm schnell mit einer Rose zurück!",
                0, roman);
        startDialogRoman.getNext(0).setNext("Ach menno, so wird sie doch nie etwas mit mir unternehmen wollen...", 1,
                roman);

        startDialogRoman.getNext(0).getNext(0).addEvent(qp -> qp.getQuestPlayer().getMineableBlocks().add(Material.RED_ROSE))
                .addPlayerToQuest();

        // JULCHEN
        String aufWiedersehen = "Auf Wiedersehen!";
        String werIstRoman = "Ach, nur einer aus dem Dorf. Ich hab seit Längerem das Gefühl, dass er ein Auge auf mich geworfen hat, aber er hat bisher noch nichts unternommen. Diese übermäßige Schüchternheit führt auf jeden Fall zu nichts...";

        startDialogJulchen = new DialogMessage(q, julchen)
                .setInhalt("Ich hätte sooo gern ein paar hübsche Geschenke...")
                .setFortsetzung("Bekommst du denn von niemandem welche?")
                .setFortsetzung("Jedenfalls nicht von mir! Sorry!");
        startDialogJulchen.setNext(
                "Naja, nur eben zum Geburtstag und so weiter... Aber ich hätte eben gerne einfach so mal welche. Dann überraschen sie einen ja auch umso mehr.",
                0, julchen).setFortsetzung("Was hättest du gerne?").setFortsetzung("Viel Glück dabei! Ciao!");
        startDialogJulchen
                .setNext("Das hab ich eh nicht erwartet! Dieser faule Sack Roman sollte sich lieber mal aufraffen!", 1,
                        julchen)
                .setFortsetzung("Soll ich ihm das ausrichten?").setFortsetzung("Roman? Wer ist Roman?")
                .setFortsetzung("Nicht meine Angelegenheit. Bis bald!");

        // was hättest du gern?
        startDialogJulchen.getNext(0).setNext(
                "Ach, ich weiß auch nicht... Irgendwas romantisches natürlich... Egal, darum geht es ja nicht, stimmts?",
                0, julchen).setFortsetzung("Stimmt! Viel Glück noch, ciao!")
                .setFortsetzung("Wer ist eigentlich dieser Roman?");

        startDialogJulchen.getNext(0).setNext(aufWiedersehen, 1, julchen);
        startDialogJulchen.getNext(0).getNext(0).setNext(aufWiedersehen, 0, julchen);

        startDialogJulchen.getNext(1).setNext("Was? Natürlich nicht! Er soll von sich aus auf mich zukommen!", 0,
                julchen);
        startDialogJulchen.getNext(1).setNext(werIstRoman, 1, julchen);
        startDialogJulchen.getNext(1).setNext(aufWiedersehen, 2, julchen);

        startDialogJulchen.getNext(0).getNext(0).setNext(werIstRoman, 1, julchen);

        // ohne rose
        comeBackWORose = new DialogMessage(q, roman).setInhalt(
                "Wo bleibt die Rose für mein Julchen? Mach schneller, es geht um Liebe und Tod! Also.. hauptsächlich um ersteres.");

        // mit rose
        comeBackWRose = new DialogMessage(q, roman).setInhalt(
                "Du hast die Rose? Sehr gut! Jetzt musst du sie ihr nur noch vorbeibringen und sagen sie ist von mir!");
        comeBackWRose.setFortsetzung("Wie bitte?").setNext(
                "Naja, ich traue mich nicht, also musst du das machen! Wenn du fertig bist, kannst du dir bei mir eine Belohnung abholen!",
                0, roman);

        // mehr aufmerksamkeit
        mehrAufmerksamkeit = new DialogMessage(q, julchen).setInhalt(
                "Ach, dieser Roman... Wenn er mir doch nur etwas mehr Aufmerksamkeit schenken würde. Versteht er nicht, dass ich Schmuck und Geschenke will?");

        // julchenRose
        julchenRose = new DialogMessage(q, julchen).setInhalt("Oh, eine Rose? Für mich?");
        julchenRose.setFortsetzung("Ja, die ist von Roman, nur für dich!")
                .setFortsetzung("Ja, hab ich extra für dich gepflückt!");
        julchenRose.setNext(
                "Dieser Holzkopf! Was soll ich denn mit einer Blume? Ich hätte viel lieber Gold oder Edelsteine! Hier, bring ihm seine Rose wieder!",
                0, julchen);
        julchenRose.setNext(
                "Oh, wofür das? Nicht so wichtig, das ist richtig lieb von dir! Hier, du bekommst auch ein paar Kekse von mir!",
                1, julchen);
        julchenRose.getNext(1).addEvent(new GiveItemEvent(Material.COOKIE, "Keks mit Blümchen drauf", 16));
        julchenRose.getNext(1).addEvent(new TakeItemEvent(Material.RED_ROSE, 1));

        // nachQuest
        nachQuestRoman = new DialogMessage(q, roman).setInhalt("Danke für die Hilfe noch!");
        nachQuestJulchen = new DialogMessage(q, roman).setInhalt("Danke für alles!");

        // platzhalter
        platzhalter = new DialogMessage(q, julchen).setInhalt("XXX PLATZHALTER XXX");

    }
}
