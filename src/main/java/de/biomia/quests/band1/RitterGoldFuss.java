package de.biomia.quests.band1;

import de.biomia.api.Biomia;
import de.biomia.quests.QuestConditions.ItemConditions;
import de.biomia.quests.QuestEvents.AddCoinEvent;
import de.biomia.quests.QuestEvents.GiveItemEvent;
import de.biomia.quests.QuestEvents.TakeItemEvent;
import de.biomia.quests.general.DialogMessage;
import de.biomia.quests.general.Quest;
import de.biomia.quests.general.QuestPlayer;
import de.biomia.quests.general.States;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.main.Main;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;
import net.citizensnpcs.api.trait.trait.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RitterGoldFuss implements Listener {

	private final Quest q = Biomia.QuestManager().registerNewQuest("Goldfu\u00df1", 1);
	HashMap<UUID, Quest> hm = new HashMap<>();
	private final NPC goldfuss;

    private DialogMessage startDialogArmor;
    private DialogMessage startDialogNoArmor;
    private DialogMessage comeBackWRottenFlesh;
    private DialogMessage comeBackWORottenFlesh;
    private DialogMessage nachQuest;

	public RitterGoldFuss() {
		q.setInfoText(
				"In der N\u00e4he des Dorfes triffst du einen Ritter mit golden gl\u00e4nzenden Latschen. Er m\u00f6chte, dass du ihm im Kampf gegen die Kreaturen der Nacht unterst\u00fctzt und ihm als Beweis 32 St\u00fcck verdorbenes Fleisch pr\u00e4sentierst.");
		q.setDisplayName("Ritter Goldfu\u00df");
		goldfuss = q.createNPC(EntityType.PLAYER, "Ritter Goldfu\u00df");
		Location loc = new Location(Bukkit.getWorld("Quests"), 156, 70, -318, -130, 0);
		goldfuss.spawn(loc);

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			// do nothing
		}

		goldfuss.addTrait(Inventory.class);
		goldfuss.addTrait(Equipment.class);
		Equipment e = (goldfuss.getTrait(Equipment.class));
		e.set(EquipmentSlot.BOOTS, ItemCreator.itemCreate(Material.GOLD_BOOTS));
		e.set(EquipmentSlot.LEGGINGS, ItemCreator.itemCreate(Material.IRON_LEGGINGS));
		e.set(EquipmentSlot.CHESTPLATE, ItemCreator.itemCreate(Material.IRON_CHESTPLATE));
		e.set(EquipmentSlot.HELMET, ItemCreator.itemCreate(Material.IRON_HELMET));
		e.set(EquipmentSlot.HAND, ItemCreator.itemCreate(Material.IRON_SWORD));

		initDialog();
	}

	@EventHandler
	public void onInteract(NPCRightClickEvent e) {
		if (goldfuss.equals(e.getNPC())) {

			QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

			if (qp.getDialog() == null) {
				States state = qp.getState(q);
				if (state != null) {
					switch (state) {
					case STATUS1:
						if (ItemConditions.hasItemInInventory(qp, Material.ROTTEN_FLESH, 32)) {
							qp.setDialog(comeBackWRottenFlesh);
						} else {
							qp.setDialog(comeBackWORottenFlesh);
						}
						break;
					default:
						break;
					}
				} else if (qp.hasFinished(q)) {
					qp.setDialog(nachQuest);
				} else {
					if (!ItemConditions.inFullArmor(qp))
						qp.setDialog(startDialogNoArmor);
					else
						qp.setDialog(startDialogArmor);

				}

				qp.getDialog().execute(qp);
			}
		}
	}

	private void initDialog() {
		// start dl
		// with Armor
		startDialogArmor = new DialogMessage(q, goldfuss).setInhalt(
				"Seid gegr\u00fc\u00dft, tapferer Recke! W\u00e4rt Ihr so freundlich mir mit eurer Hilfe beizustehen?");
		startDialogArmor.setFortsetzung("Um was geht es denn?").setFortsetzung("Nein, keine Zeit.");
		startDialogArmor.setNext(
				"Mein Name ist Ritter Goldfu\u00df und es ist meine Aufgabe diese Lande von der Monsterplage zu befreien! Seid Ihr dabei geht mit auf Monsterjagd?",
				0, goldfuss).setFortsetzung("Monster jagen? Na logo!").setFortsetzung("Lieber nicht.");
		startDialogArmor.setNext(
				"Verstehe. Besiegt als erstes die Angst, die euch auf Eurem Weg begleitet. Kommt wieder, wenn Euch das gelungen ist.",
				1, goldfuss);

		startDialogArmor.getNext(0).setNext(
				"Na, dann macht Euch an die Arbeit! Bringt mir einen halben Stack verrottetes Fleisch!", 0, goldfuss);
		startDialogArmor.getNext(0).setNext(
				"Kommt wieder, wenn Ihr es Euch anders \u00fcberlegt. Die Monster laufen Euch schon nicht davon.", 1,
				goldfuss);

		startDialogArmor.getNext(0).getNext(0).addPlayerToQuest();

		// without Armor

		startDialogNoArmor = new DialogMessage(q, goldfuss).setInhalt(
				"Was wollt ihr von mir? Ihr seid meine Zeit nicht wert, habt ja nicht einmal eine R\u00fcstung an!");

		// wo flesh
		comeBackWORottenFlesh = new DialogMessage(q, goldfuss).setInhalt(
				"Sieht ganz so aus, als h\u00e4ttet Ihr noch nicht genug Zombies erledigt! Mindestens einen halben Stack verrotetes Fleisch will ich haben, los, los!");

		// w flesh
		comeBackWRottenFlesh = new DialogMessage(q, goldfuss)
				.setInhalt("Oh, das habt Ihr gut gemacht! Nehmt das hier als Belohnung!")
				.setNext("Recke, ich bin mir sicher, dass sich unsere Wege noch \u00f6fter kreuzen werden! Auf dann!",
						0, goldfuss);
		comeBackWRottenFlesh.addEvent(new TakeItemEvent(Material.ROTTEN_FLESH, 32));

		ItemStack stack = ItemCreator.itemCreate(Material.LEATHER_BOOTS, "Ritter Goldfu\u00df' alte Latschen");
		stack.addEnchantment(Enchantment.PROTECTION_FALL, 4);

		comeBackWRottenFlesh.addEvent(new GiveItemEvent(stack)).addEvent(new AddCoinEvent(1000)).finish();
		// nach quest
		nachQuest = new DialogMessage(q, goldfuss).setInhalt("Danke nochmal f\u00fcr die Hilfe!");
	}
}
