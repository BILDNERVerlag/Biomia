package de.biomia.spigot.server.quests.band1;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.manager.ActionBar;
import de.biomia.spigot.messages.manager.Title;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.spigot.server.quests.general.*;
import de.biomia.spigot.tools.ItemCreator;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReiteDasSchwein implements Listener {

    private static final ArrayList<UUID> currentPigs = new ArrayList<>();
    private static final Quest q = Biomia.getQuestManager().registerNewQuest("ReiteDasSchwein", 1);
    private static final HashMap<QuestPlayer, Boolean> onEndLoc = new HashMap<>();
    private static final HashMap<QuestPlayer, BukkitTask> thread = new HashMap<>();
    // DL
    private static DialogMessage nichtGeschafft;
    private static DialogMessage startDialog;
    private static DialogMessage nachQuest;
    private static DialogMessage geschafft;
    private static DialogMessage schummler;
    private static NPC brian;
    private static NPC falto;

    private final Location endLoc = new Location(Bukkit.getWorld("BiomiaWelt"), 303, 64, -157);
    private final Location startLoc = new Location(Bukkit.getWorld("BiomiaWelt"), 146, 64, -147);

    public ReiteDasSchwein() {

        q.setDisplayName("Reite das Schwein!");
        q.setInfoText(
                "Das stets Schabernack treibende Duo aus Brian und Falto fordert dich zu einem Schweinerennen heraus.");
        q.setRemoveOnReload(true);

        Location loc_brian = new Location(Bukkit.getWorld("BiomiaWelt"), 133, 70, -150, -152, 0);
        Location loc_falto = new Location(Bukkit.getWorld("BiomiaWelt"), 135, 70, -149, -151, 0);

        brian = q.createNPC(EntityType.PLAYER, "Brian");
        falto = q.createNPC(EntityType.PLAYER, "Falto");
        brian.spawn(loc_brian);
        falto.spawn(loc_falto);

        for (NPC n : q.getNpcs()) {
            // give a carrot stick to both npcs
            n.addTrait(Equipment.class);
            n.addTrait(Inventory.class);
            Equipment e = (n.getTrait(Equipment.class));
            e.set(EquipmentSlot.HAND, ItemCreator.itemCreate(Material.CARROT_STICK));
        }

        // create two pigs and equip them with saddles
        NPC faltopig = q.createNPC(EntityType.PIG, "Fabo");
        NPC brianpig = q.createNPC(EntityType.PIG, "Bribo");
        brianpig.spawn(loc_brian.add(1, 0, 0));
        faltopig.spawn(loc_falto.add(1, 0, 0));
        ((Pig) brianpig.getEntity()).setSaddle(true);
        ((Pig) faltopig.getEntity()).setSaddle(true);

        q.setRepeatable(true);
        q.setCooldown(1, TIME.MINUTEN);

        initDialog();
    }

    // methods and stuff for the race

    private static void setPigSpeed(Pig h, double speed) {
        // use about 2.25 for normalish speed
        AttributeInstance attributes = ((CraftLivingEntity) h).getHandle()
                .getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        attributes.setValue(speed);
    }

    private static void timer(QuestPlayer qp, Entity pig) {

        currentPigs.add(pig.getUniqueId());
        onEndLoc.put(qp, false);
        ((Pig) pig).setSaddle(true);
        setPigSpeed((Pig) pig, 0);

        BukkitTask th = new BukkitRunnable() {
            int countdown = 3;
            int zeitRennen = 160;

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                switch (countdown) {
                    case 3:
                        Title.sendTitel("3..", qp.getPlayer());
                        countdown--;
                        return;
                    case 2:
                        Title.sendTitel("2..", qp.getPlayer());
                        countdown--;
                        return;
                    case 1:
                        Title.sendTitel("1..", qp.getPlayer());
                        countdown--;
                        return;
                    case 0:
                        Title.sendTitel("GO!", qp.getPlayer());
                        setPigSpeed((Pig) pig, 0.2);
                        countdown--;
                        return;
                    default:
                        break;
                }

                if (onEndLoc.get(qp) != null) {
                    if (onEndLoc.get(qp)) {
                        onEndLoc.remove(qp);
                        // qp.finish(q);
                        if (!qp.hasFinished(q)) {
                            Biomia.getBiomiaPlayer(qp.getPlayer()).addCoins(zeitRennen * 15, true);
                            qp.setDialog(geschafft);
                            qp.getDialog().execute(qp);
                        }
                        thread.get(qp).cancel();
                        thread.remove(qp);

                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e1) {
                            // do nothing
                        }

                        qp.getPlayer().teleport(brian.getEntity().getLocation().add(2, 0, 0));
                    }
                }
                ActionBar.sendActionBar("\u00A7c" + zeitRennen, qp.getPlayer());
                zeitRennen--;

                if (zeitRennen == -1) {
                    qp.getPlayer().teleport(brian.getEntity().getLocation().add(2, 0, 0));
                    qp.setDialog(nichtGeschafft);
                    qp.getDialog().execute(qp);
                    thread.get(qp).cancel();
                    thread.remove(qp);
                }

            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);

        thread.put(qp, th);

    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            QuestPlayer qp = Biomia.getQuestPlayer(p);
            if (qp.isInQuest(q)) {

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (!p.isInsideVehicle()) {
                            thread.get(qp).cancel();
                            thread.remove(qp);

                            onEndLoc.remove(qp);
                            qp.getPlayer().teleport(brian.getEntity().getLocation().add(2, 0, 0));
                            qp.getPlayer().getWorld().getChunkAt(qp.getPlayer().getLocation());
                            qp.setDialog(schummler);
                            qp.getDialog().execute(qp);
                        }
                    }
                }.runTaskLater(Main.getPlugin(), 20);
            }

        }
        if (!e.getDismounted().isDead())
            if (currentPigs.contains(e.getDismounted().getUniqueId())) {
                currentPigs.remove(e.getDismounted().getUniqueId());
                e.getDismounted().remove();
            }
    }

    @EventHandler
    public void isOnLocation(PlayerMoveEvent e) {
        QuestPlayer qp = Biomia.getQuestPlayer(e.getPlayer());
        if (qp.isInQuest(q))
            if (onEndLoc.get(qp) != null)
                if (e.getTo().distanceSquared(endLoc) <= 3) {
                    onEndLoc.remove(qp);
                    onEndLoc.put(qp, true);
                }
    }

    @EventHandler
    public void onInteract(NPCRightClickEvent e) {
        if (brian.equals(e.getNPC()) || falto.equals(e.getNPC())) {

            QuestPlayer qp = Biomia.getQuestPlayer(e.getClicker());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state == null && !qp.hasFinished(q)) {
                    qp.setDialog(startDialog);
                    qp.getDialog().execute(qp);
                } else if (qp.hasFinished(q)) {
                    if (qp.checkCooldown(q)) {
                        qp.unfinish(q);
                        qp.setDialog(startDialog);
                    } else {
                        qp.setDialog(nachQuest);
                    }
                    qp.getDialog().execute(qp);
                }
            }
        }
    }

    private void initDialog() {
        startDialog = new DialogMessage(q, brian).setInhalt("Was wir hier machen? Wir reiten auf Schweinen.");
        startDialog.setNext("Das macht total Spa\u00df.", 0, falto);
        startDialog.getNext(0).setNext("Willst du auch mal? Bist du mutig genug dazu?", 0, brian);

        startDialog.getNext(0).getNext(0).setFortsetzung("Nein danke, das ist mir viel zu bl\u00f6d.")
                .setFortsetzung("Ich habe keine Angst!");

        startDialog.getNext(0).getNext(0).setNext("Von wegen, da hat jemand die Hosen voll!", 0, brian);
        startDialog.getNext(0).getNext(0)
                .setNext("Hier, nimm den Sattel und die Rute. Setz  dich auf das Schwein und reite los!", 1, brian);
        startDialog.getNext(0).getNext(0).getNext(1)
                .setNext("Wenn du es in weniger als 150 Sekunden schaffst, bist du einer von uns!", 0, falto);
        startDialog.getNext(0).getNext(0).getNext(1).addPlayerToQuest();
        startDialog.getNext(0).getNext(0).getNext(1).addEvent(new GiveItemEvent(Material.CARROT_STICK, 1))
                .addEvent(qp -> {
                    Entity pig = startLoc.getWorld().spawnEntity(startLoc, EntityType.PIG);
                    pig.addPassenger(qp.getPlayer());
                    ReiteDasSchwein.timer(qp, pig);
                });

        // geschafft
        geschafft = new DialogMessage(q, brian)
                .setInhalt("Hm.. nicht schlecht! Du hast es tats\u00e4chlich geschafft!");
        geschafft.finish();

        // nicht geschafft
        nichtGeschafft = new DialogMessage(q, brian).setInhalt("Du warst leider viel zu langsam!");
        nichtGeschafft.addEvent(new TakeItemEvent(Material.CARROT_STICK, 1));
        nichtGeschafft.removePlayerFromQuest();

        // nach quest
        nachQuest = new DialogMessage(q, falto).setInhalt(
                "Das solltest du wirklich mal wieder machen! Ich bin sicher, du kriegst das noch schneller hin!");

        // schummler
        schummler = new DialogMessage(q, falto).setInhalt(
                "Hey, du wolltest schummeln! Ich habs genau gesehen! W\u00e4hrend dem Rennen absteigen geht ja mal gar nicht!");
        schummler.addEvent(new TakeItemEvent(Material.CARROT_STICK, 1));
        schummler.removePlayerFromQuest();

    }
}