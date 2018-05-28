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

public class StillePost implements Listener {
    private final Quest q = Biomia.getQuestManager().registerNewQuest("StillePost", 1);
    private final NPC lorena;
    private final NPC franky;
    private final NPC elisabeth;
    private final NPC mario;
    private final String compassName = "\u00A7bLorena's Kompass";
    private DialogMessage startDialogLorena;
    private DialogMessage startDialogFranky;
    private DialogMessage startDialogElisabeth;
    private DialogMessage startDialogMario;
    private DialogMessage nachQuestLorena;
    private DialogMessage nachQuestFranky;
    private DialogMessage nachQuestElisabeth;
    private DialogMessage nachQuestMario;
    private DialogMessage inQuestFranky;
    private DialogMessage inQuestElisabeth;
    private DialogMessage inQuestMario;
    private DialogMessage comeBackWCompass;
    private DialogMessage comeBackWOCompass;

    /*
     * script
     *
     * 0: hey, 1 hat sich von mir einen kompass ausgeliehen, bring ihn zur00fcck
     * 1: sorry, hab ich 2 gegeben 2: sorry, hab ich 3 verkauft 3: klar kannst dus
     * haben, du musst nur XYZ
     */

    public StillePost() {
        q.setDisplayName("Stille Post");
        q.setInfoText(
                "In der N00fche des Fischerhauses triffst du auf Lorena. Sie bittet dich darum, ihren abhanden gekommenen Kompass zur00fcckzubringen.");

        lorena = q.createNPC(EntityType.PLAYER, "Lorena");
        franky = q.createNPC(EntityType.PLAYER, "Franky");
        elisabeth = q.createNPC(EntityType.PLAYER, "Elisabeth");
        mario = q.createNPC(EntityType.PLAYER, "Mario");

        Location locLorena = new Location(Bukkit.getWorld("Quests"), 174, 63, -281, 60, 0);
        lorena.spawn(locLorena);
        Location locFranky = new Location(Bukkit.getWorld("Quests"), 107.5, 70, -292, -155, 0);
        franky.spawn(locFranky);
        Location locElisa = new Location(Bukkit.getWorld("Quests"), 121.5, 73, -280.5, 0, 0);
        elisabeth.spawn(locElisa);
        Location locMario = new Location(Bukkit.getWorld("Quests"), 83.7, 70, -267.5, 180, 0);
        mario.spawn(locMario);
        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {

        if (lorena.equals(e.getNPC())) {
            // NPC0
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    if (ItemConditions.hasItemInInventory(qp, Material.COMPASS, 1, compassName)) {
                        qp.setDialog(comeBackWCompass);
                    } else
                        qp.setDialog(comeBackWOCompass);
                } else if (qp.hasFinished(q)) {
                    qp.setDialog(nachQuestLorena);
                } else {
                    qp.setDialog(startDialogLorena);
                }
                qp.getDialog().execute(qp);
            }
        } else if (franky.equals(e.getNPC())) {
            // NPC1
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    switch (state) {
                        case STATUS1:
                            if (!ItemConditions.hasItemInInventory(qp, Material.COMPASS, 1, compassName)) {
                                qp.setDialog(inQuestFranky);
                                qp.updateState(q, States.STATUS2);
                            } else
                                qp.setDialog(startDialogFranky);
                            break;
                        case STATUS2:
                            qp.setDialog(startDialogFranky);
                            break;
                        case STATUS3:
                            qp.setDialog(startDialogFranky);
                            break;
                        default:
                            break;
                    }
                } else if (qp.hasFinished(q)) {
                    qp.setDialog(nachQuestFranky);
                } else
                    qp.setDialog(startDialogFranky);

                qp.getDialog().execute(qp);
            }

        } else if (elisabeth.equals(e.getNPC())) {
            // NPC2
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    switch (state) {
                        case STATUS1:
                            qp.setDialog(startDialogElisabeth);
                            break;
                        case STATUS2:
                            qp.setDialog(inQuestElisabeth);
                            qp.updateState(q, States.STATUS3);
                            break;
                        case STATUS3:
                            qp.setDialog(startDialogElisabeth);
                            break;
                        default:
                            break;
                    }
                } else if (qp.hasFinished(q)) {
                    qp.setDialog(nachQuestElisabeth);
                } else {
                    qp.setDialog(startDialogElisabeth);

                }

                qp.getDialog().execute(qp);
            }

        } else if (mario.equals(e.getNPC())) {
            // NPC3
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    switch (state) {
                        case STATUS1:
                            qp.setDialog(startDialogMario);
                            break;
                        case STATUS2:
                            qp.setDialog(startDialogMario);
                            break;
                        case STATUS3:
                            qp.setDialog(inQuestMario);
                            qp.updateState(q, States.STATUS1);
                            break;
                        default:
                            break;
                    }
                } else if (qp.hasFinished(q)) {
                    qp.setDialog(nachQuestMario);
                } else {
                    qp.setDialog(startDialogMario);

                }

                qp.getDialog().execute(qp);
            }

        }
    }

    private void initDialog() {
        // start dl
        startDialogLorena = new DialogMessage(q, lorena).setInhalt("Hey, 00fchm...");
        startDialogLorena.setNext("Ich will ja wirklich nicht aufdringlich sein oder so...", 0, lorena)
                .setNext("Aber k00f6nntest du mir bei etwas behilflich sein?", 0, lorena)
                .setFortsetzung("Um was geht's?").setFortsetzung("Kein Interesse.")
                .setNext(
                        "Ein altes Familienerbst00fcck. Der Kompass meines Grossvaters! Ich habe ihn jemandem geliehen - und derjenige hat ihn bis heute nicht zur00fcckgebracht.",
                        0, lorena)
                .setFortsetzung("Ich hole ihn dir zur00fcck!").setFortsetzung("Da mische ich mich nicht ein.");

        startDialogLorena.getNext(0).getNext(0)
                .setNext("Oh, schade. Komm wieder, falls du es dir anders 00fcberlegst...", 1, lorena);
        startDialogLorena.getNext(0).getNext(0).getNext(0).setNext("Das ist gro00dfartig! Den Kompass hat "
                + franky.getName() + ", den findet man meistens in der Dorfschmiede! Viel Erfolg!", 0, lorena);
        startDialogLorena.getNext(0).getNext(0).getNext(0).getNext(0).addPlayerToQuest();
        startDialogLorena.getNext(0).getNext(0).getNext(0)
                .setNext("Oh, schade. Komm wieder, falls du es dir anders 00fcberlegst...", 1, lorena);

        startDialogFranky = new DialogMessage(q, franky).setInhalt(
                "Brian und Falto, diese Bengel. Erst vor ein paar Tagen haben sie mein ganzes Haus in Spinnenf00fcden eingewickelt!");
        startDialogElisabeth = new DialogMessage(q, elisabeth).setInhalt("Was macht ein Creeper im Sommer?");
        startDialogElisabeth.setNext("Er sprengt den Rasen.", 0, elisabeth);
        startDialogMario = new DialogMessage(q, mario).setInhalt("Treffen sich zwei Skelette.");
        startDialogMario.setNext("Beide tot.", 0, mario);

        // inQuest
        inQuestFranky = new DialogMessage(q, franky)
                .setInhalt("Ach, du suchst den Kompass von " + lorena.getName() + "? Naja, 00fchm...");
        inQuestFranky.setFortsetzung("Was soll 'Naja, 00fchm...' hei00dfen?")
                .setFortsetzung("Du hast ihn nicht mehr, stimmts?");
        inQuestFranky.setNext("Ich wei00df, wie das jetzt vielleicht klingt, aber ich habe ihn " + elisabeth.getName()
                + " weitergeliehen, als sie damals auf eine kleine Expedition gehen wollte... Wenn du den Kompass zur00fcckhaben willst, "
                + elisabeth.getName() + " wohnt direkt gegen00fcber!", 0, franky);
        inQuestFranky.setNext("Ja. Richtig. Ich hab ihn " + elisabeth.getName()
                + " geliehen, als er damals aus eine kleine Expedition gehen wollte... Wenn du den Kompass zur00fcckhaben willst, "
                + elisabeth.getName() + " wohnt direkt gegen00fcber!", 1, franky);

        inQuestElisabeth = new DialogMessage(q, elisabeth).setInhalt("Wie, was, wo? Der Kompass, den ich von "
                + franky.getName() + " bekommen habe? Ich dachte das war ein Geschenk. Den habe ich erst vor kurzem an "
                + mario.getName()
                + " verkauft. Du wei00dft schon, der wohnt ganz am Dorfrand in einer der kleinen H00fctten dort.");

        inQuestMario = new DialogMessage(q, mario).setInhalt("Du willst den Kompass? Klar kannst du den haben.")
                .setFortsetzung("Wie? So einfach?").setFortsetzung("Vielen Dank, ich muss los!");
        inQuestMario.addEvent(new GiveItemEvent(Material.COMPASS, compassName, 1));
        inQuestMario.setNext("Klar, was soll ich mit einem verfluchten Kompass?", 0, mario);
        inQuestMario.setNext(
                "Vielen Dank nochmal, ciao! Richte " + lorena.getName() + " sch00f6ne Gr00fc00dfe von mir aus!",
                1, mario);
        inQuestMario.getNext(0).setFortsetzung("Der Kompass ist verflucht?").setFortsetzung("Danke jedenfalls.");
        inQuestMario.getNext(0).setNext(
                "Oh Gott, nein, nicht wirklich. Denke ich. Nur ist er auf meiner letzten Expedition stehengeblieben - und ein nicht "
                        + "funktionierender Kompass ist, zumindest wenn man auf ihn angewiesen ist, wirklich nicht erheblich besser als ein verfluchter. Bis demn00fcchst!",
                0, mario);
        inQuestMario.getNext(0).setNext("Keine Ursache. Wir sehen uns.", 1, mario);

        // nach quest
        nachQuestLorena = new DialogMessage(q, lorena).setInhalt(
                "Vielen, vielen Dank nochmal f00fcr die Hilfe mit dem Kompass! Ich w00fcsste nicht, was ich ohne dich getan h00e4tte!");
        nachQuestFranky = startDialogFranky;
        nachQuestElisabeth = startDialogElisabeth;
        nachQuestMario = startDialogMario;

        // comeback w or wo compass
        comeBackWCompass = new DialogMessage(q, lorena).setInhalt(
                "Oh, vielen, vielen Dank! Ich will gar nicht wissen, wie viele Nerven dich das gekostet hat!");
        comeBackWCompass.addEvent(new TakeItemEvent(Material.COMPASS, compassName, 1));
        comeBackWCompass.addEvent(new AddCoinEvent(150));

        comeBackWCompass.finish();
        comeBackWOCompass = new DialogMessage(q, lorena)
                .setInhalt("Hast du schon in der Schmiede bei Franky wegen dem Kompass nachgefragt?");
    }
}