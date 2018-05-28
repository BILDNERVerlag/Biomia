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

public class RitterGoldhelm implements Listener {

    private final Quest q = Biomia.getQuestManager().registerNewQuest("Goldhelm1", 1);
    private final NPC goldhelm;
    private DialogMessage startDialogArmor;
    private DialogMessage startDialogNoArmor;
    private DialogMessage comeBackWithBones;
    private DialogMessage comeBackWithOutBones;
    private DialogMessage nachQuest;

    public RitterGoldhelm() {
        q.setInfoText(
                "Nahe der Holzfarm triffst du einen Ritter mit golden glänzendem Helm. Er möchte, dass du ihm im Kampf gegen die Kreaturen der Nacht unterstützt und ihm als beweis 32 Knochen präsentierst.");
        q.setDisplayName("Ritter Goldhelm");
        goldhelm = q.createNPC(EntityType.PLAYER, "Ritter Goldhelm");
        Location loc = new Location(Bukkit.getWorld("Quests"), 326.9, 63, -164.5, 142, 5);
        goldhelm.spawn(loc);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e1) {
            // do nothing
        }

        goldhelm.addTrait(Inventory.class);
        goldhelm.addTrait(Equipment.class);
        Equipment e = (goldhelm.getTrait(Equipment.class));
        e.set(EquipmentSlot.BOOTS, ItemCreator.itemCreate(Material.IRON_BOOTS));
        e.set(EquipmentSlot.LEGGINGS, ItemCreator.itemCreate(Material.IRON_LEGGINGS));
        e.set(EquipmentSlot.CHESTPLATE, ItemCreator.itemCreate(Material.IRON_CHESTPLATE));
        e.set(EquipmentSlot.HELMET, ItemCreator.itemCreate(Material.GOLD_HELMET));
        e.set(EquipmentSlot.HAND, ItemCreator.itemCreate(Material.IRON_SWORD));

        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (goldhelm.equals(e.getNPC())) {

            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    switch (state) {
                        case STATUS1:
                            if (ItemConditions.hasItemInInventory(qp, Material.BONE, 32)) {
                                qp.setDialog(comeBackWithBones);
                            } else {
                                qp.setDialog(comeBackWithOutBones);
                            }
                            break;
                        default:
                            break;
                    }
                } else if (qp.hasFinished(q)) {
                    qp.setDialog(nachQuest);
                } else {
                    if (ItemConditions.inFullArmorOfMaterial(qp, "iron")
                            || ItemConditions.inFullArmorOfMaterial(qp, "gold")
                            || ItemConditions.inFullArmorOfMaterial(qp, "diamond"))
                        qp.setDialog(startDialogArmor);
                    else
                        qp.setDialog(startDialogNoArmor);

                }

                qp.getDialog().execute(qp);
            }
        }
    }

    private void initDialog() {

        // start dl
        // with Armor
        startDialogArmor = new DialogMessage(q, goldhelm).setInhalt(
                "Oh, ein tapferer Krieger, das sehe ich Euch gleich an! Sagt, habt Ihr ein paar Hünde übrig, um mir mit diesen widerwürtigen Monstern zur Hand zu gehen?");
        startDialogArmor.setFortsetzung("Worum geht es genau?").setFortsetzung("Im Moment nicht.");
        startDialogArmor.setNext(
                "In diesem Gebiet treiben sich viele Skelette herum, mehr als gewöhnlich. Aus diesem Grund habe ich hier mein Lager aufgeschlagen und hoffe schon bald, dem ein oder "
                        + "anderen zu begegnen. In der Tat ein nicht zu vernachlüssigendes Problem, für das ich aber nun eine Lösung gefunden zu haben meine. Nümlich Euch, wie klingt das?",
                0, goldhelm).setFortsetzung("Monsterjagd! Los gehts!").setFortsetzung("Sorry, nicht interessiert.");
        startDialogArmor.setNext(
                "Nun, wer bin ich, Euch vorzuschreiben, was Ihr zu tun und lassen hüttet... Also viel Glück dabei weiterhin. Aber... falls Ihr Euch schließlich doch noch"
                        + " umentscheiden solltet, meldet Euch einfach bei mir! Ihr wisst, wo Ihr mich findet!",
                1, goldhelm);

        startDialogArmor.getNext(0).setNext(
                "Ich würde sagen, bringt mir 32 Stück Skelettknochen, und ich verspreche euch, ich lege Euch eine großartige Belohnung zurecht, sowahr ich Ritter Goldhelm heiße!",
                0, goldhelm).setFortsetzung("Dann bis dann!").setFortsetzung("Ist das euer echter Name?")
                .addPlayerToQuest();
        startDialogArmor.getNext(0).setNext(
                "Kommt wieder, falls Ihr es Euch anders überlegt. Die Monster laufen mit Sicherheit nicht davon.",
                1, goldhelm);

        startDialogArmor.getNext(0).getNext(0).setNext(
                "Ich setze großes Vertrauen in Euch! ... Außerdem schließen wir familienintern immer Wetten ab, also legt Euch ins Zeug.",
                0, goldhelm);
        startDialogArmor.getNext(0).getNext(0).setNext(
                "Was? Oh. Ja, klar. Vorname Goldfuß, Nachname Ritter. Was dachtest du? Und jetzt kümmer dich lieber um die Skelette!",
                1, goldhelm);

        // without Armor
        startDialogNoArmor = new DialogMessage(q, goldhelm).setInhalt(
                "Tut mir Leid, solange ihr keine komplette Rüstung tragt, weiß ich nicht, wie ich Euch ernst nehmen soll... Ach, und Leder zühlt nicht. Ein wahrer Krieger trügt mindestens Eisen.");

        // wo bones
        comeBackWithOutBones = new DialogMessage(q, goldhelm).setInhalt(
                "Das sind noch nicht genug Knochen! Sieht ganz so aus, als müsstet Ihr Euch, was diese Knochenmünnchen angeht, etwas mehr ins Zeug legen. Etwa 32 Knochen sollten genug sein. Los los!");

        // w bones
        comeBackWithBones = new DialogMessage(q, goldhelm).setInhalt(
                "Das ging ja schneller als erwartet - ich habe Euch offenbar nicht falsch eingeschützt! Nehmt das hier als Belohnung!");
        comeBackWithBones.setNext(
                "Bis zum nüchsten Mal - und - falls Ihr einen meiner Brüder trefft, richtet ihr schöne Grüße aus? Vielen Dank!",
                0, goldhelm);
        comeBackWithBones.addEvent(new TakeItemEvent(Material.BONE, 32));

        ItemStack stack = ItemCreator.itemCreate(Material.LEATHER_HELMET, "Ritter Goldhelms alte Mütze");
        stack.addEnchantment(Enchantment.OXYGEN, 2);

        comeBackWithBones.addEvent(new GiveItemEvent(stack)).addEvent(new GiveItemEvent(Material.GOLD_INGOT, 1))
                .addEvent(new AddCoinEvent(150)).finish();

        // nach Quest
        nachQuest = new DialogMessage(q, goldhelm).setInhalt("Danke nochmal für die Hilfe!");
    }
}
