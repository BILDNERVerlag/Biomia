package de.biomia.quests.band1;

import de.biomia.api.Biomia;
import de.biomia.quests.QuestConditions.ItemConditions;
import de.biomia.quests.QuestEvents.AddCoinEvent;
import de.biomia.quests.QuestEvents.TakeItemEvent;
import de.biomia.quests.general.DialogMessage;
import de.biomia.quests.general.Quest;
import de.biomia.quests.general.QuestPlayer;
import de.biomia.quests.general.States;
import de.biomia.api.main.Main;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class FinteMitTinte implements Listener {

	private final Material itemZuBesorgen = Material.INK_SACK;
	private final String questName = "FinteMitTinte";

	private final Quest q = Biomia.QuestManager().registerNewQuest(questName, 1);
	HashMap<UUID, Quest> hm = new HashMap<>();
	private final NPC npc;

    private DialogMessage startDialog;
    private DialogMessage comeWithItem;
    private DialogMessage comeWithoutItem;
    private DialogMessage nachQuest;

	public FinteMitTinte() {
		q.setInfoText(
				"Franz ist kreativ. Franz redet gerne in Reimen. Franz ist Autor. Franz hat keine Tinte. Hol sie ihm. Zehn Tintens\u00e4ckchen um genau zu sein.");
		q.setDisplayName("Des Schreibers Crux");
		String npcName = "Frank Kazka";
		npc = q.createNPC(EntityType.PLAYER, npcName);
		Location npcLoc = new Location(Bukkit.getWorld("Quests"), 142.5, 67, -249, 0, 0);
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
						if (ItemConditions.hasItemInInventory(qp, itemZuBesorgen, 10))
							qp.setDialog(comeWithItem);
						else
							qp.setDialog(comeWithoutItem);
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
		startDialog.setInhalt("(Schweigt dich an)");
		startDialog.setFortsetzung("\u00c4h, hallo..? Nichts zu sagen?").setFortsetzung("(Davongehen)");
		startDialog.setNext(
				"Hm? Oh, pardon! Mir war nicht bewusst, dass... Wie auch immer. Wo Worte selten sind, da haben sie am meisten Gewicht, wei\u00dft du?",				0, npc);
		startDialog.setNext("(Blickt dir schweigend nach)", 1, npc);
		startDialog.getNext(0).setFortsetzung("...okay? Kann man dir irgendwie helfen?")
				.setFortsetzung("Aaalles klar, na daaann bis bald!");
		startDialog.getNext(0).setNext(
				"Helfen kann man mir allerdings! Wei\u00dft du, ich m\u00f6chte ein neues Buch verfassen, aber es scheint, mir ist die Tinte ausgegangen! Magst du mir vielleicht zehn Tintenbeutel besorgen?",
				0, npc);
		startDialog.getNext(0).setNext("Auf Wiedersehen!", 1, npc);
		startDialog.getNext(0).getNext(0).setFortsetzung("Jap, mache ich!").setFortsetzung("Ein ander Mal vielleicht!");
		startDialog.getNext(0).getNext(0)
				.setNext("Das klingt ja gro\u00dfartig! Komm einfach wieder zu mir, wenn du alle zehn hast!", 0, npc)
				.addPlayerToQuest();
		startDialog.getNext(0).getNext(0)
				.setNext("Auweh, auweh... Vielleicht sollte ich einfach Dreck statt Tinte benutzen...", 1, npc);

		// come back without item
		comeWithoutItem = new DialogMessage(q, npc).setInhalt(
				"Gut Ding soll Weile haben, so sagt man zumindest... Auf meine Tinte will ich dennoch nicht viel l\u00e4nger warten! Falls du bisher noch nicht genug gefunden hast, solltest du dich vielleicht an die Tintenfische im Flu\u00df wenden, die haben bestimmt ein paar hilfreiche Infos f\u00fcr dich!");

		// come back with item
		comeWithItem = new DialogMessage(q, npc)
				.setInhalt("Meine Tinte ist da! Gro\u00dfartig! Vielen Dank! Hier, nimm das!");
		comeWithItem.addEvent(new TakeItemEvent(Material.INK_SACK, 10));
		comeWithItem.addEvent(new AddCoinEvent(250));
		comeWithItem.finish();

		// nach quest
		nachQuest = new DialogMessage(q, npc).setInhalt(
				"Oh, du bists! Danke dir nochmal f\u00fcr die Hilfe mit dem Holz! Ich dachte schon, es sei aus mit meiner Tischlerei!");
	}

}