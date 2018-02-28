package de.biomia.quests.band1;

import de.biomia.quests.Quests;
import de.biomia.api.Biomia;
import de.biomia.quests.QuestConditions.ItemConditions;
import de.biomia.quests.QuestEvents.TakeItemEvent;
import de.biomia.quests.general.*;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Forsthilfe implements Listener {

	private final Material itemZuBesorgen = Material.SAPLING;
	private final String questName = "Forsthilfe";

    private DialogMessage startDialog;
    private DialogMessage comeBackWithItem;
    private DialogMessage comeBackWithoutItem;
    private DialogMessage nachQuest;

	private final Quest q = Biomia.QuestManager().registerNewQuest(questName, 1);
	private final NPC npc;

	public Forsthilfe() {
		String npcName = "Forsen";
		q.setInfoText(npcName
				+ ", der F\u00f6rster, m\u00f6chte, dass du ihm dabei hilfst, den Wald wieder auszuweiten. Er bittet dich deshalb darum, ihm einige Setzlinge zu besorgen. Zehn sollten gen\u00fcgen.");
		npc = q.createNPC(EntityType.PLAYER, npcName);
		Location npcLoc = new Location(Bukkit.getWorld("Quests"), 118.305, 71, -300, 92, 28);
		npc.spawn(npcLoc);

		q.setRepeatable(true);
		q.setCooldown(24, TIME.STUNDEN);

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
							qp.setDialog(comeBackWithItem);
						else
							qp.setDialog(comeBackWithoutItem);
						break;
					default:
						break;
					}
				} else
					Quests.restartQuestIfTimeOver(qp, q, startDialog, nachQuest);

				qp.getDialog().execute(qp);
			}
		}
	}

	private void initDialog() {
		// start dl
		startDialog = new DialogMessage(q, npc);
		startDialog
				.setInhalt("Oh, hallo! Bist du neu hier in der Gegend? Ich bin " + npc.getName()
						+ ", der F\u00f6rster! Sag, k\u00f6nntest du mir einen Gefallen tun?")
				.setFortsetzung("Um was gehts denn?").setFortsetzung("Ein ander Mal!");
		startDialog.setNext(
				"Nunja, die Setzlinge werden langsam knapp! K\u00f6nntest du mir, sagen wir.. 10 St\u00fcck bringen?",
				0, npc);
		startDialog.setNext("Naja, verstehe schon. Melde dich, wenn du Zeit hast.", 1, npc);
		startDialog.getNext(0).setFortsetzung("Kein Problem!").setFortsetzung("Keine Zeit!");
		startDialog.getNext(0).setNext(
				"Vielen, vielen Dank! Ich hoffe, das macht dir nicht zu viel aus, du bist wahrscheinlich ja eh \u00f6fter mal im Holzfarmgebiet unterwegs, nehm ich an...",
				0, npc).setFortsetzung("Bis dann!").setFortsetzung("Holzfarmgebiet?").addPlayerToQuest();
		startDialog.getNext(0).setNext(
				"Kein Problem, das nehme ich dir nicht \u00fcbel! Aber falls du es dir doch noch einmal anders \u00fcberlegen solltest, melde dich ruhig bei mir!",
				1, npc);
		startDialog.getNext(0).getNext(0).setNext("Wir sehen uns!", 0, npc);
		startDialog.getNext(0).getNext(0).setNext(
				"Oh, das kennst du nicht? Das ist ein kleines Gebiet im Osten des Dorfes, wo es erlaubt ist, B\u00fcume zu f\u00fcllen. Ich dachte, wenn du nach Setzlingen suchst, bist du dort richtig aufgehoben. Also, viel Gl\u00fcck!",
				1, npc);

		// without item
		comeBackWithoutItem = new DialogMessage(q, npc)
				.setInhalt("Du hast die 10 Setzlinge noch nicht? Kein Problem, lass dir Zeit. Es eilt nicht.");

		// with item
		comeBackWithItem = new DialogMessage(q, npc)
				.setInhalt("Du hast zehn St\u00fcck Setzlinge? Kann ich die haben? Das ist ja wunderbar! Vielen Dank!");
		comeBackWithItem.addEvent(new TakeItemEvent(Material.SAPLING, 10));

		// nach quest
		nachQuest = new DialogMessage(q, npc).setInhalt(
				"Dank meiner unerm\u00fcdlichen Arbeit, gehen dir nie die B\u00fcume im Holzfarmgebiet aus! Ist das nicht wunderbar?");
	}

}
