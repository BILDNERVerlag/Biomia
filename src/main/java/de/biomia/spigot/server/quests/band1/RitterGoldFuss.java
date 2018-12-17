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
import de.biomia.spigot.tools.ItemCreator;
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

import java.util.concurrent.TimeUnit;

public class RitterGoldFuss implements Listener {

    private final Quest q = Biomia.getQuestManager().registerNewQuest("Goldfuß1", 1);
    private final NPC goldfuss;
    private DialogMessage startDialogArmor;
    private DialogMessage startDialogNoArmor;
    private DialogMessage comeBackWRottenFlesh;
    private DialogMessage comeBackWORottenFlesh;
    private DialogMessage nachQuest;

    public RitterGoldFuss() {
        q.setInfoText(
                "In der Nähe des Dorfes triffst du einen Ritter mit golden glänzenden Latschen. Er möchte, dass du ihm im Kampf gegen die Kreaturen der Nacht unterstützt und ihm als Beweis 32 Stück verdorbenes Fleisch präsentierst.");
        q.setDisplayName("Ritter Goldfuß");
        goldfuss = q.createNPC(EntityType.PLAYER, "Ritter Goldfuß");
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
                    if (state == States.STATUS1) {
                        if (ItemConditions.hasItemInInventory(qp, Material.ROTTEN_FLESH, 32)) {
                            qp.setDialog(comeBackWRottenFlesh);
                        } else {
                            qp.setDialog(comeBackWORottenFlesh);
                        }
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
                "Seid gegrüßt, tapferer Recke! Wärt Ihr so freundlich mir mit eurer Hilfe beizustehen?");
        startDialogArmor.setFortsetzung("Um was geht es denn?").setFortsetzung("Nein, keine Zeit.");
        startDialogArmor.setNext(
                "Mein Name ist Ritter Goldfuß und es ist meine Aufgabe diese Lande von der Monsterplage zu befreien! Seid Ihr dabei geht mit auf Monsterjagd?",
                0, goldfuss).setFortsetzung("Monster jagen? Na logo!").setFortsetzung("Lieber nicht.");
        startDialogArmor.setNext(
                "Verstehe. Besiegt als erstes die Angst, die euch auf Eurem Weg begleitet. Kommt wieder, wenn Euch das gelungen ist.",
                1, goldfuss);

        startDialogArmor.getNext(0).setNext(
                "Na, dann macht Euch an die Arbeit! Bringt mir einen halben Stack verrottetes Fleisch!", 0, goldfuss);
        startDialogArmor.getNext(0).setNext(
                "Kommt wieder, wenn Ihr es Euch anders überlegt. Die Monster laufen Euch schon nicht davon.", 1,
                goldfuss);

        startDialogArmor.getNext(0).getNext(0).addPlayerToQuest();

        // without Armor

        startDialogNoArmor = new DialogMessage(q, goldfuss).setInhalt(
                "Was wollt ihr von mir? Ihr seid meine Zeit nicht wert, habt ja nicht einmal eine Rüstung an!");

        // wo flesh
        comeBackWORottenFlesh = new DialogMessage(q, goldfuss).setInhalt(
                "Sieht ganz so aus, als hättet Ihr noch nicht genug Zombies erledigt! Mindestens einen halben Stack verrotetes Fleisch will ich haben, los, los!");

        // w flesh
        comeBackWRottenFlesh = new DialogMessage(q, goldfuss)
                .setInhalt("Oh, das habt Ihr gut gemacht! Nehmt das hier als Belohnung!")
                .setNext("Recke, ich bin mir sicher, dass sich unsere Wege noch öfter kreuzen werden! Auf dann!",
                        0, goldfuss);
        comeBackWRottenFlesh.addEvent(new TakeItemEvent(Material.ROTTEN_FLESH, 32));

        ItemStack stack = ItemCreator.itemCreate(Material.LEATHER_BOOTS, "Ritter Goldfuß' alte Latschen");
        stack.addEnchantment(Enchantment.PROTECTION_FALL, 4);

        comeBackWRottenFlesh.addEvent(new GiveItemEvent(stack)).addEvent(new AddCoinEvent(1000)).finish();
        // nach quest
        nachQuest = new DialogMessage(q, goldfuss).setInhalt("Danke nochmal für die Hilfe!");
    }
}
