package de.biomia.quests.band1;

import de.biomiaAPI.Biomia;
import de.biomia.quests.QuestEvents.AddCoinEvent;
import de.biomia.quests.QuestEvents.Event;
import de.biomia.quests.QuestEvents.GiveItemIfNotInInventoryEvent;
import de.biomia.quests.QuestEvents.TakeCoinEvent;
import de.biomia.quests.general.DialogMessage;
import de.biomia.quests.general.Quest;
import de.biomia.quests.general.QuestPlayer;
import de.biomia.quests.general.States;
import de.biomiaAPI.main.Main;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Allgemeinwissen implements Listener {

	private final Quest q = Biomia.QuestManager().registerNewQuest("Allgemeinwissen", 1);
	HashMap<UUID, Quest> hm = new HashMap<>();
	private final NPC herbert, ghost;

    private final Location locGhost;
    private final Event ghostSpawnEvent = new Event() {
		// anonyme klasse, da erstens platzsparender und zweitens ermoeglicht referenz
		// auf das ghost-objekt (das eigl ja nicht im event ist)
		@Override
		public void executeEvent(QuestPlayer arg0) {
			ghost.spawn(locGhost);
		}
	};
    private DialogMessage startDialog;
    private DialogMessage nachQuest;
    private DialogMessage inQuest;
    private DialogMessage afterGhost;
    private DialogMessage status2;

    private ItemStack oldBook;
    private DialogMessage status3;
    private DialogMessage status4;

	private final Event ghostDespawnEvent = new Event() {
		@Override
		public void executeEvent(QuestPlayer qp0) {
			ghost.despawn();
		}
	};

	public Allgemeinwissen() {
		q.setInfoText(
				"Herbert m\u00f6chte, dass du f\u00fcr ihn ein verlorenes Familienerbst\u00fcck wiederbeschaffst. Er vertraut dir ein altes Buch an, mit dessen Hilfe du ein R\u00e4tsel l\u00f6sen sollst.");

		herbert = q.createNPC(EntityType.PLAYER, "Herbert");
        Location locHerbert = new Location(Bukkit.getWorld("Quests"), 101.5, 70, -304.5, 95, 0);
		herbert.spawn(locHerbert);

		ghost = q.createNPC(EntityType.PLAYER, "Geist");

		locGhost = new Location(Bukkit.getWorld("Quests"), 99.5, 70, -305.5, -22, 0);
		initBook();
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
	}

	@EventHandler
	public void onInteract(NPCRightClickEvent e) {
		if (herbert.equals(e.getNPC())) {

			QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

			if (qp.getDialog() == null) {
				States state = qp.getState(q);
				if (state != null) {
					switch (state) {
					case STATUS1:
						qp.setDialog(inQuest);
						break;
					case STATUS2:
						qp.setDialog(status2);
						break;
					case STATUS3:
						qp.setDialog(status3);
						break;
					case STATUS4:
						qp.setDialog(status4);
						break;
					default:
						qp.setDialog(status4);
						break;
					}
				} else if (qp.hasFinished(q)) {
					qp.setDialog(nachQuest);
				} else {
					qp.setDialog(startDialog);
				}

				qp.getDialog().execute(qp);
			}
		} else if (ghost.equals(e.getNPC())) {

			QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

			if (qp.getDialog() == null) {
				States state = qp.getState(q);
				if (state != null) {
					switch (state) {
					case STATUS1:
						break;
					case STATUS2:
						qp.setDialog(afterGhost);
						break;
					case STATUS3:
						break;
					default:
						break;
					}
				}
				qp.getDialog().execute(qp);
			}
		}
	}

	private void initBook() {

		ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
		bookMeta.setTitle("Staubiges Buch");
		bookMeta.setAuthor("Horbort, der Mathematiker");

		List<String> pages = new ArrayList<>();
		pages.add(
				"Mein Ziel ist es, auf so viele Fragen wie nur m\u00f6glich, eindeutige Antworten zu finden. nat\u00fcrlich sind manche dieser Fragen interessanter als andere - und ich bin noch weit davon entfernt, alle Fragen, die sich mir stellen, beantworten zu k\u00f6nnen.");
		pages.add(
				"Ein Buch \u00fcber jene Fragen zu verfassen, auf die ich die Antwort zu geben nicht im Stande bin, w\u00e4re allerdings sowohl f\u00fcr mich als auch f\u00fcr den Leser nicht mehr als Zeitverschwendung. Also will ich mich in diesem Buch auf die Details konzentrieren,");
		pages.add(
				"die sich mit Sicherheit bestimmen lassen. Auf den nachfolgenden Seiten folgt eine Auflistung, dieser Fakten rund ums Dorf:");
		pages.add(
				"Anzahl schwarzer Woll-Laternen:\n\nAnzahl H\u00e4user im Dorf:\n\nAnzahl Felder im Dorf:\n\nTiefe d. Brunnens:");
		pages.add("§o(Du bl\u00e4tterst um, §odoch hier ist keine §oSeite mehr. §oSeltsam.)");

		bookMeta.setPages(pages);
		writtenBook.setItemMeta(bookMeta);
		oldBook = writtenBook;

	}

	public void initDialog() {

		// startDL
		startDialog = new DialogMessage(q, herbert)
				.setInhalt("He, du da! K\u00f6nntest du mir bei etwas behilflich sein?").setFortsetzung("Worum geht's?")
				.setFortsetzung("Keine Zeit!");
		startDialog.setNext("Ein altes Familienerbst\u00fcck. K\u00f6nntest du's mir holen?", 0, herbert)
				.setFortsetzung("Ich bin jederzeit bereit!").setFortsetzung("Ein ander Mal vielleicht!");
		startDialog.setNext("Oh, verstehe schon. Komm wieder, falls du es dir anders \u00fcberlegst.", 1, herbert);
		startDialog.getNext(0).setNext(
				"Na, dann los gehts! Dann muss ich dir ja nur noch erz\u00e4hlen, um was es eigentlich geht! Har har!",
				0, herbert).setFortsetzung("Ich bitte darum.").setFortsetzung("Fass dich kurz.");
		startDialog.getNext(0).getNext(0).addPlayerToQuest();
		startDialog.getNext(0).setNext("Komm einfach wieder, falls du es dir anders \u00fcberlegt hast!", 1, herbert);
		startDialog.getNext(0).getNext(0).setNext(
				"Aaalso, wo fange ich an? ... Mein Gro\u00dfvater war ein intelligenter Mann. Sehr intelligent. Hat sich insbesondere sehr f\u00fcr Zahlen interessiert."
						+ "Jedenfalls sagt man, dass er einen gro\u00dfen Schatz besa\u00df - Ich vermute er hat ihn vor seinem Tod irgendwo versteckt. Aber wei\u00dft du, er hat mir dieses Buch hinterlassen... Ich glaube, es soll dabei helfen, den Schatz zu finden. Die "
						+ "Sache ist nur... Es ist nicht komplett. Es fehlt die letzte Seite.",
				0, herbert).addEvent(new GiveItemIfNotInInventoryEvent(oldBook));
		startDialog.getNext(0).getNext(0).setNext(
				"Es geht um ein Erbst\u00fcck, von dem keiner wei\u00df, wo es steckt. Ich glaube, man kann es finden, wenn man das Geheimnis dieses Buches l\u00f6st.",
				1, herbert).addEvent(new GiveItemIfNotInInventoryEvent(oldBook));

		// inQuest
		inQuest = new DialogMessage(q, herbert)
				.setInhalt("Na, wie sieht's aus? Hast du das Geheimnis des Buches schon gel\u00f6st?")
				.setFortsetzung("Nein, leider nicht...").setFortsetzung("Ich glaube schon!")
				.setFortsetzung("Ich habs verloren.");

		// Noch nicht gel\u00f6st ->
		inQuest.setNext(
				"Oh, das ist nat\u00fcrlich.. Hm.. Du packst das schon! Steck einfach noch ein bisschen mehr Zeit rein!",
				0, herbert);

		// Gel\u00f6st ->
		inQuest.setNext("Was? Das ist ja gro\u00dfartig! Was hast du herausge-", 1, herbert)
				.setNext("Wie was? Ist das ein G-Geist?", 0, herbert).addEvent(ghostSpawnEvent)
				.updatePlayerState(States.STATUS2);

		// Ich habs verloren ->
		inQuest.setNext("WAS? Das Buch war ein Erbst\u00fcck meines GROSSVATERS! WIE KONNTEST DU NUR?", 2, herbert)
				.setNext("...", 0, herbert).setNext("...", 0, herbert)
				.setNext(
						"Nur ein Scherz. Ich hab dir nat\u00fcrlich nicht das Original gegeben, sondern nur eine Kopie. Hier hast du noch eine. Das kostet dich aber 30 M\u00fcnzen. Selbst schuld.",
						0, herbert)
				.addEvent(new TakeCoinEvent(30)).addEvent(new GiveItemIfNotInInventoryEvent(oldBook));

		// nachQuest
		nachQuest = new DialogMessage(q, herbert).setInhalt(
				"Vielen Dank nochmal! Klar, wir haben keinen Schatz gefunden, aber das Abenteuer war mindestens genauso gut!");

		// afterGhost
		afterGhost = new DialogMessage(q, ghost).setInhalt(
				"Nanu, hat wohl doch noch jemand etwas mit meinem B\u00fcchlein anzufangen gewusst? Hei\u00dft das, du hast die richtigen Antworten?")
				.setFortsetzung("Jap, allerdings!").setFortsetzung("Ich glaube nicht...")
				.setFortsetzung("Antworten? Worauf?");
		afterGhost.setNext("Dann hoffe ich, du bist bereit, mir deine Antworten zu nennen?", 0, ghost)
				.setFortsetzung("Das bin ich!").setFortsetzung("Ich brauche noch etwas Zeit.");
		afterGhost.setNext("M\u00f6chtest du trotzdem dein bestes versuchen?", 1, ghost)
				.setFortsetzung("Auf jeden Fall!").setFortsetzung("Ich brauche noch etwas Zeit.");
		afterGhost.setNext("Vielleicht solltest du nochmal einen Blick in das Buch werfen...", 2, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);

		String wvLaternen = "Nun, wieviele schwarze Laternen hast du gez\u00e4hlt?";
		afterGhost.getNext(0).setNext(wvLaternen, 0, ghost).setFortsetzung("2").setFortsetzung("3").setFortsetzung("4")
				.setFortsetzung("5");
		afterGhost.getNext(0).getNext(0)
				.setNext("Das ist korrekt! Weiter gehts! Wieviele H\u00e4user gibt es hier im Dorf?", 0, ghost)
				.setFortsetzung("9").setFortsetzung("11").setFortsetzung("13").setFortsetzung("15");
		afterGhost.getNext(0).getNext(0).setNext("Leider falsch.", 1, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);
		afterGhost.getNext(0).getNext(0).setNext("Leider falsch.", 2, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);
		afterGhost.getNext(0).getNext(0).setNext("Leider falsch.", 3, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);

		afterGhost.getNext(0).getNext(0).getNext(0).setNext("Leider falsch.", 0, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);
		afterGhost.getNext(0).getNext(0).getNext(0).setNext(
				"Das ist korrekt! N\u00e4chste Frage. Wieviele Felder, an denen man Saatgut anbauen kann, gibt es im Dorf?",
				1, ghost).setFortsetzung("2").setFortsetzung("3").setFortsetzung("4").setFortsetzung("5");
		afterGhost.getNext(0).getNext(0).getNext(0).setNext("Leider falsch.", 2, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);
		afterGhost.getNext(0).getNext(0).getNext(0).setNext("Leider falsch.", 3, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);

		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).setNext("Leider falsch.", 0, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);
		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1)
				.setNext("Das ist korrekt! Letzte Frage! Wieviele Bl\u00f6cke tief ist der Dorfbrunnen?", 1, ghost)
				.setFortsetzung("2").setFortsetzung("4").setFortsetzung("6").setFortsetzung("8");
		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).setNext("Leider falsch.", 2, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);
		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).setNext("Leider falsch.", 3, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);

		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).getNext(1)
				.setNext("Das ist korrekt! Wunderbar, du hast alle R\u00e4tsel gel\u00f6st!", 0, ghost);
		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).getNext(1).setNext("Leider falsch.", 1, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);
		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).getNext(1).setNext("Leider falsch.", 2, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);
		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).getNext(1).setNext("Leider falsch.", 3, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);

		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).getNext(1).getNext(0)
				.setFortsetzung("Was ist jetzt mit dem Schatz?").setFortsetzung("Und weiter..?");
		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).getNext(1).getNext(0)
				.setNext("Schatz? Harharhar, mein gr\u00f6\u00dfter Schatz war nat\u00fcrlich stets mein Enkel!", 0,
						ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS4);
		afterGhost.getNext(0).getNext(0).getNext(0).getNext(1).getNext(1).getNext(0).setNext(
				"Du bist auf den Schatz auf, nicht wahr? Nunja, er steht neben dir. Mein Enkel ist mit Sicherheit einer meiner gr\u00f6\u00dften Erfolge.",
				1, ghost).addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS4);

		afterGhost.getNext(1).setNext(wvLaternen, 0, ghost).setFortsetzung("2").setFortsetzung("3").setFortsetzung("4")
				.setFortsetzung("5");
		afterGhost.getNext(1).getNext(0)
				.setNext("Das ist korrekt! Weiter gehts! Wieviele H\u00e4user gibt es hier im Dorf?", 0, ghost)
				.setFortsetzung("9").setFortsetzung("11").setFortsetzung("13").setFortsetzung("15");
		afterGhost.getNext(1).getNext(0).setNext("Leider falsch.", 1, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);
		afterGhost.getNext(1).getNext(0).setNext("Leider falsch.", 2, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);
		afterGhost.getNext(1).getNext(0).setNext("Leider falsch.", 3, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);

		afterGhost.getNext(1).getNext(0).getNext(0).setNext("Leider falsch.", 0, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);
		afterGhost.getNext(1).getNext(0).getNext(0).setNext(
				"Das ist korrekt! N\u00e4chste Frage. Wieviele Felder, an denen man Saatgut anbauen kann, gibt es im Dorf?",
				1, ghost).setFortsetzung("2").setFortsetzung("3").setFortsetzung("4").setFortsetzung("5");
		afterGhost.getNext(1).getNext(0).getNext(0).setNext("Leider falsch.", 2, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);
		afterGhost.getNext(1).getNext(0).getNext(0).setNext("Leider falsch.", 3, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);

		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).setNext("Leider falsch.", 0, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);
		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1)
				.setNext("Das ist korrekt! Letzte Frage! Wieviele Bl\u00f6cke tief ist der Dorfbrunnen?", 1, ghost)
				.setFortsetzung("2").setFortsetzung("4").setFortsetzung("6").setFortsetzung("8");
		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).setNext("Leider falsch.", 2, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);
		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).setNext("Leider falsch.", 3, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);

		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).getNext(1)
				.setNext("Das ist korrekt! Wunderbar, du hast alle R\u00e4tsel gel\u00f6st!", 0, ghost);
		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).getNext(1).setNext("Leider falsch.", 1, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);
		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).getNext(1).setNext("Leider falsch.", 2, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);
		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).getNext(1).setNext("Leider falsch.", 3, ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS3);

		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).getNext(1).getNext(0)
				.setFortsetzung("Was ist jetzt mit dem Schatz?").setFortsetzung("Und weiter..?");
		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).getNext(1).getNext(0)
				.setNext("Schatz? Harharhar, mein gr\u00f6\u00dfter Schatz war nat\u00fcrlich stets mein Enkel!", 0,
						ghost)
				.addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS4);
		afterGhost.getNext(1).getNext(0).getNext(0).getNext(1).getNext(1).getNext(0).setNext(
				"Du bist auf den Schatz auf, nicht wahr? Nunja, er steht neben dir. Mein Enkel ist mit Sicherheit einer meiner gr\u00f6\u00dften Erfolge.",
				1, ghost).addEvent(ghostDespawnEvent).updatePlayerState(States.STATUS4);

		String braucheMehrZeit = "Komm wieder, sobald du dir deiner Antworten sicherer bist!";
		afterGhost.getNext(0).setNext(braucheMehrZeit, 1, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);
		afterGhost.getNext(1).setNext(braucheMehrZeit, 1, ghost).addEvent(ghostDespawnEvent)
				.updatePlayerState(States.STATUS3);

		// status2
		status2 = new DialogMessage(q, herbert).setInhalt("Ist das tats\u00e4chlich der Geist meines Gro\u00dfvaters?");

		// status3
		status3 = new DialogMessage(q, herbert).setInhalt("M\u00f6chtest du es noch einmal versuchen?")
				.setFortsetzung("Jap!").setFortsetzung("Im Moment nicht.");
		status3.setNext("Na dann viel Gl\u00fcck!", 0, herbert).addEvent(ghostSpawnEvent)
				.updatePlayerState(States.STATUS2);
		status3.setNext("Komm bald wieder!", 1, herbert);

		// status4
		status4 = new DialogMessage(q, herbert).setInhalt(
				"Naja, sieht so aus, als g\u00e4be es doch keinen Schatz... Zumindest nicht wirklich... Vielleicht kannst du aber mit etwas hiervon was anfangen? Was ist dir lieber?")
				.setFortsetzung("M\u00fcnzgeld bitte!").setFortsetzung("Ich helfe gern kostenlos!");
		status4.setNext("Harharhar, das dachte ich mir schon! Hier ist deine Belohnung! Vielen Dank nochmal!", 0,
				herbert).addEvent(new AddCoinEvent(300)).finish();
		status4.setNext("Oh? Na, wenn du das sagst! Vielen Dank nochmal!", 1, herbert).finish();
	}
}
