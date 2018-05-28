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
                "Nahe der Holzfarm triffst du einen Ritter mit golden gl00e4nzendem Helm. Er m00f6chte, dass du ihm im Kampf gegen die Kreaturen der Nacht unterst00fctzt und ihm als beweis 32 Knochen pr00e4sentierst.");
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
                "Oh, ein tapferer Krieger, das sehe ich Euch gleich an! Sagt, habt Ihr ein paar H00fcnde 00fcbrig, um mir mit diesen widerw00fcrtigen Monstern zur Hand zu gehen?");
        startDialogArmor.setFortsetzung("Worum geht es genau?").setFortsetzung("Im Moment nicht.");
        startDialogArmor.setNext(
                "In diesem Gebiet treiben sich viele Skelette herum, mehr als gew00f6hnlich. Aus diesem Grund habe ich hier mein Lager aufgeschlagen und hoffe schon bald, dem ein oder "
                        + "anderen zu begegnen. In der Tat ein nicht zu vernachl00fcssigendes Problem, f00fcr das ich aber nun eine L00f6sung gefunden zu haben meine. N00fcmlich Euch, wie klingt das?",
                0, goldhelm).setFortsetzung("Monsterjagd! Los gehts!").setFortsetzung("Sorry, nicht interessiert.");
        startDialogArmor.setNext(
                "Nun, wer bin ich, Euch vorzuschreiben, was Ihr zu tun und lassen h00fcttet... Also viel Gl00fcck dabei weiterhin. Aber... falls Ihr Euch schlie00dflich doch noch"
                        + " umentscheiden solltet, meldet Euch einfach bei mir! Ihr wisst, wo Ihr mich findet!",
                1, goldhelm);

        startDialogArmor.getNext(0).setNext(
                "Ich w00fcrde sagen, bringt mir 32 St00fcck Skelettknochen, und ich verspreche euch, ich lege Euch eine gro00dfartige Belohnung zurecht, sowahr ich Ritter Goldhelm hei00dfe!",
                0, goldhelm).setFortsetzung("Dann bis dann!").setFortsetzung("Ist das euer echter Name?")
                .addPlayerToQuest();
        startDialogArmor.getNext(0).setNext(
                "Kommt wieder, falls Ihr es Euch anders 00fcberlegt. Die Monster laufen mit Sicherheit nicht davon.",
                1, goldhelm);

        startDialogArmor.getNext(0).getNext(0).setNext(
                "Ich setze gro00dfes Vertrauen in Euch! ... Au00dferdem schlie00dfen wir familienintern immer Wetten ab, also legt Euch ins Zeug.",
                0, goldhelm);
        startDialogArmor.getNext(0).getNext(0).setNext(
                "Was? Oh. Ja, klar. Vorname Goldfu00df, Nachname Ritter. Was dachtest du? Und jetzt k00fcmmer dich lieber um die Skelette!",
                1, goldhelm);

        // without Armor
        startDialogNoArmor = new DialogMessage(q, goldhelm).setInhalt(
                "Tut mir Leid, solange ihr keine komplette R00fcstung tragt, wei00df ich nicht, wie ich Euch ernst nehmen soll... Ach, und Leder z00fchlt nicht. Ein wahrer Krieger tr00fcgt mindestens Eisen.");

        // wo bones
        comeBackWithOutBones = new DialogMessage(q, goldhelm).setInhalt(
                "Das sind noch nicht genug Knochen! Sieht ganz so aus, als m00fcsstet Ihr Euch, was diese Knochenm00fcnnchen angeht, etwas mehr ins Zeug legen. Etwa 32 Knochen sollten genug sein. Los los!");

        // w bones
        comeBackWithBones = new DialogMessage(q, goldhelm).setInhalt(
                "Das ging ja schneller als erwartet - ich habe Euch offenbar nicht falsch eingesch00fctzt! Nehmt das hier als Belohnung!");
        comeBackWithBones.setNext(
                "Bis zum n00fcchsten Mal - und - falls Ihr einen meiner Br00fcder trefft, richtet ihr sch00f6ne Gr00fc00dfe aus? Vielen Dank!",
                0, goldhelm);
        comeBackWithBones.addEvent(new TakeItemEvent(Material.BONE, 32));

        ItemStack stack = ItemCreator.itemCreate(Material.LEATHER_HELMET, "Ritter Goldhelms alte M00fctze");
        stack.addEnchantment(Enchantment.OXYGEN, 2);

        comeBackWithBones.addEvent(new GiveItemEvent(stack)).addEvent(new GiveItemEvent(Material.GOLD_INGOT, 1))
                .addEvent(new AddCoinEvent(150)).finish();

        // nach Quest
        nachQuest = new DialogMessage(q, goldhelm).setInhalt("Danke nochmal f00fcr die Hilfe!");
    }
}
