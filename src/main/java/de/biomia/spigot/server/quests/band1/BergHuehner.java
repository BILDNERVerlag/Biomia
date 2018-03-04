package de.biomia.spigot.server.quests.band1;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.server.quests.QuestEvents.AddCoinEvent;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.spigot.server.quests.Quests;
import de.biomia.spigot.server.quests.general.*;
import de.biomia.spigot.messages.QuestItemNames;
import de.biomia.spigot.tools.ItemCreator;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class BergHuehner implements Listener {
    private final Quest q = Biomia.getQuestManager().registerNewQuest("BergHuehner", 1);
    private final HashMap<QuestPlayer, Boolean> hasEggs = new HashMap<>();
    private final Location zielLoc = new Location(Bukkit.getWorld("Quests"), 216, 158, -162);
    private final NPC tom;
    HashMap<UUID, Quest> hm = new HashMap<>();
    private DialogMessage startDialog;
    private DialogMessage comeBackWithEggs;
    private DialogMessage comeBackWithoutEggs;
    private DialogMessage nachQuest;

    public BergHuehner() {
        q.setInfoText(
                "An den Ausl\u00e4ufern des Berges im S\u00fcden des Dorfes triffst du auf Tom. Der w\u00fcrde gern einen bestimmten Kuchen backen, braucht daf\u00fcr"
                        + " jedoch spezielle Eier von H\u00fchnern an der Spitze des Berges. Dir f\u00e4llt die Aufgabe zu, sie unversehrt nach unten zu bringen.");
        q.setDisplayName("Bergh\u00fchner");
        q.setRemoveOnReload(true);
        q.setRepeatable(true);
        q.setCooldown(1, TIME.TAGE);

        tom = q.createNPC(EntityType.PLAYER, "Tom");
        Location npcLoc = new Location(Bukkit.getWorld("Quests"), 181, 72, -220, 140, 0);
        tom.spawn(npcLoc);
        initDialog();
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (tom.equals(e.getNPC())) {
            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());
            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    switch (state) {
                    case STATUS1:
                        if (ItemConditions.hasItemInInventory(qp, Material.EGG, 3, QuestItemNames.specialEgg)) {
                            qp.setDialog(comeBackWithEggs);
                            hasEggs.remove(qp);
                        } else {
                            qp.setDialog(comeBackWithoutEggs);
                        }
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

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Biomia.getQuestPlayer(e.getPlayer()).isInQuest(q)) {
            QuestPlayer qp = Biomia.getQuestPlayer(e.getPlayer());
            if (!hasEggs.containsKey(qp)) {
                if (qp.getPlayer().getLocation().distance(zielLoc) <= 10) {
                    new GiveItemEvent(Material.EGG, QuestItemNames.specialEgg, 3).executeEvent(qp);
                    if (qp.getPlayer().getInventory().getChestplate() == null)
                        qp.getPlayer().getInventory()
                                .setChestplate(ItemCreator.itemCreate(Material.ELYTRA, QuestItemNames.twoMinuteElytra));
                    hasEggs.put(qp, true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (qp.getPlayer().getInventory() != null)
                                if (ItemConditions.hasItemInInventory(Biomia.getQuestPlayer(e.getPlayer()),
                                        Material.ELYTRA, 1, QuestItemNames.twoMinuteElytra)
                                        || ItemConditions.hasItemOnArmor(Biomia.getQuestPlayer(e.getPlayer()),
                                        Material.ELYTRA, 1, QuestItemNames.twoMinuteElytra))
                                    new TakeItemEvent(Material.ELYTRA, QuestItemNames.twoMinuteElytra, 1).executeEvent(qp);
                        }
                    }.runTaskLater(Main.getPlugin(), 120 * 20);
                }
            }
        }
    }

    private void initDialog() {
        // start dl
        startDialog = new DialogMessage(q, tom).setInhalt(
                "Die Bergh\u00fchner dort oben legen ganz besondere Eier. Kannst du hinauf klettern und mir drei davon bringen? Ich traue mich nie so weit hoch!");
        startDialog.setFortsetzung("Gerne!").setFortsetzung("Keine Zeit!");
        startDialog.setNext("Beeil dich!", 0, tom);
        startDialog.setNext("Schade, vielleicht ein anderes Mal.", 1, tom);
        startDialog.getNext(0).addPlayerToQuest();

        // without eggs
        comeBackWithoutEggs = new DialogMessage(q, tom).setInhalt("Bitte hol mir die Eier vom Berg!");

        // with eggs
        comeBackWithEggs = new DialogMessage(q, tom);
        comeBackWithEggs.setInhalt("Vielen Dank, ich mache mich gleich an die Arbeit!");
        comeBackWithEggs.addEvent(new TakeItemEvent(Material.EGG, QuestItemNames.specialEgg, 3));
        comeBackWithEggs.addEvent(new TakeItemEvent(Material.ELYTRA, QuestItemNames.twoMinuteElytra, 1));
        comeBackWithEggs.setNext("...", 0, tom).setNext("...", 0, tom).setNext(
                "Hier, fertig! Am liebsten w\u00fcrde ich jeden Tag einen Kuchen backen. Komm doch morgen wieder!", 0,
                tom);

        comeBackWithEggs.addEvent(new AddCoinEvent(150));
        comeBackWithEggs.getNext(0).getNext(0).getNext(0).addEvent(new GiveItemEvent(Material.PUMPKIN_PIE, 16))
                .finish();

        // nach quest
        nachQuest = new DialogMessage(q, tom)
                .setInhalt("Am liebsten w\u00fcrde ich jeden Tag einen Kuchen backen. Komm doch morgen wieder.");
    }

}