package de.biomia.quests.band1;

import de.biomia.api.Biomia;
import de.biomia.quests.QuestConditions.ItemConditions;
import de.biomia.quests.QuestEvents.AddCoinEvent;
import de.biomia.quests.QuestEvents.GiveItemEvent;
import de.biomia.quests.QuestEvents.TakeItemEvent;
import de.biomia.quests.general.*;
import de.biomia.quests.Quests;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class WirFeiernEinFest implements Listener {
	private final Quest q = Biomia.getQuestManager().registerNewQuest("WirFeiernEinFest", 1);
	private final NPC habil;

    private DialogMessage startDialog;
    private DialogMessage comeBackWithFireworks;
    private DialogMessage comeBackWithoutFireworks;
    private DialogMessage nachQuest;

	public WirFeiernEinFest() {
		q.setInfoText(
				"Habil ist an der Organisation eines gro\u00dfen Dorffestes beteiligt, und obwohl er f\u00fcr das Feuerwerk zust\u00fcndig ist, hat er noch keine Raketen. Vielleicht kannst du welche auftreiben, drei St\u00fcck sollten gen\u00fcgen.");
		q.setDisplayName("Wir feiern ein Fest!");
		habil = q.createNPC(EntityType.PLAYER, "Habil");
		Location loc = new Location(Bukkit.getWorld("Quests"), 111, 72, -278, 0, 0);
		habil.spawn(loc);

		q.setRepeatable(true);
		q.setCooldown(24, TIME.STUNDEN);

		initDialog();
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		if (habil.getEntity().equals(e.getRightClicked())) {
			QuestPlayer qp = Biomia.getQuestPlayer(e.getPlayer());

			if (qp.getDialog() == null) {
				States state = qp.getState(q);
				if (state != null) {
					switch (state) {
					case STATUS1:
						if (ItemConditions.hasItemInInventory(qp, Material.FIREWORK, 3))
							qp.setDialog(comeBackWithFireworks);
						else
							qp.setDialog(comeBackWithoutFireworks);
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

		startDialog = new DialogMessage(q, habil).setInhalt(
				"Hallo! Hast du schon geh\u00f6rt? Wir feiern gerade, dass unsere letzte Ernte so gut ausgefallen ist. Leider fehlt nocht etwas, was die Feier perfekt machen w\u00fcrde.");
		startDialog.setFortsetzung("Kann ich euch helfen?").setFortsetzung("Kein Interesse! Tsch\u00fcss!");
		startDialog.setNext("K\u00f6nntest du f\u00fcr uns ein paar Feuerwerksraketen bauen? Ich wollte schon immer mal"
				+ " welche davon sehen, wie sie bunte Lichter an den Himmel zaubern. "
				+ "Bisher hat es leider nie geklappt, weil sich niemand traut, gegen so viele Creeper "
				+ "anzutreten. Nur wenn man sie im Kampf besiegt, bekommt man genug Schwarzpulver"
				+ " daf\u00fcr. K\u00f6nntest du mir vielleicht, sagen wir, drei St\u00fcck besorgen? Also sei vorsichtig, wenn du dich auf den Weg machst! Wenn du die drei"
				+ " Feuerwerksraketen hast, bring sie zu mir!", 0, habil);

		startDialog.setNext("Oh, okay... Ciao!", 1, habil);
		startDialog.getNext(0).addPlayerToQuest();

		// comeback wo
		comeBackWithoutFireworks = new DialogMessage(q, habil)
				.setInhalt("Na, traust du dich nicht an die Creeper ran?");

		// comeback w
		comeBackWithFireworks = new DialogMessage(q, habil).setInhalt("Unglaublich! Du musst ein wahrlich"
				+ " guter K\u00fcmpfer sein, wenn du so viele Creeper besiegt hast! Vielen Dank! Hier, "
				+ "als kleines Dankesch\u00f6n schenke ich dir mein Feuerzeug!" + "Wir freuen uns schon!");
		comeBackWithFireworks.addEvent(new TakeItemEvent(Material.FIREWORK, 3));
		comeBackWithFireworks.addEvent(new GiveItemEvent(Material.FLINT_AND_STEEL, 1));
		comeBackWithFireworks.addEvent(new AddCoinEvent(300));

		// if (habil != null) {
		// Location loc = Bukkit.getEntity(habil.getUniqueId()).getLocation().add(0, 3,
		// 0);
		//
		// comeBackWithFireworks.addEvent(new SummonEntity(loc, EntityType.FIREWORK,
		// 3));
		// comeBackWithFireworks.finish();
		// }

		// nach quest
		nachQuest = new DialogMessage(q, habil).setInhalt("Gro\u00dfartiges Feuerwerk war das.");
	}
}
