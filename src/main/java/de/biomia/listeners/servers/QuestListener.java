package de.biomia.listeners.servers;

import de.biomia.Biomia;
import de.biomia.BiomiaPlayer;
import de.biomia.Main;
import de.biomia.messages.manager.ActionBar;
import de.biomia.messages.manager.Scoreboards;
import de.biomia.server.quests.QuestConditions.ItemConditions;
import de.biomia.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.server.quests.general.Quest;
import de.biomia.server.quests.general.QuestPlayer;
import de.biomia.messages.QuestItemNames;
import de.biomia.messages.QuestMessages;
import de.biomia.tools.InventorySave;
import de.biomia.tools.QuestItems;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class QuestListener extends BiomiaListener {

    private static final Location holzfarmLoc = new Location(Bukkit.getWorld("Quests"), 283, 69, -234);
    private static final Location cobblefarmLoc = new Location(Bukkit.getWorld("Quests"), 128, 71, -187);

    @EventHandler
    public static void playerInteract(PlayerInteractEvent event) {
        QuestPlayer qp = Biomia.getQuestPlayer(event.getPlayer());
        if (event.getAction() == Action.PHYSICAL) {
            if (qp.getDialog() != null) {
                event.setCancelled(true);
            }
        } else if (event.hasItem() && event.getItem().getType() == Material.EGG && event.getItem().hasItemMeta()
                && event.getItem().getItemMeta().getDisplayName().equals(QuestItemNames.specialEgg)) {
            event.setCancelled(true);
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                Material material = event.getItem().getType();
                if (material == Material.BUCKET || material == Material.WATER_BUCKET
                        || material == Material.LAVA_BUCKET) {
                    if (!qp.isInQuest(Biomia.getQuestManager().getQuest("Wasserholen")))
                        event.setCancelled(true);
                    // TODO: buildable blocks mit buckets?
                    if (event.getItem().getType() != null && event.getItem().getType() == Material.WATER_BUCKET
                            || event.getItem().getType() == Material.LAVA_BUCKET) {
                        // Platzieren canceln
                        event.setCancelled(true);
                    } else if (event.getItem().getType() != null && event.getItem().getType() == Material.BUCKET) {
                        // Nehmen canceln
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        InventorySave.saveInventory(e.getPlayer(), "QuestServer");
    }

    @EventHandler
    public void onJoin_(PlayerJoinEvent e) {
        InventorySave.setInventory(e.getPlayer(), "QuestServer");

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        QuestItems.giveQuestItems(bp);

        QuestPlayer qp = bp.getQuestPlayer();
        for (Quest quest : Biomia.getQuestManager().getQuests()) {
            if (quest.getRemoveOnReload())
                if (quest.getActivePlayerBiomiaIDs().contains(qp.getBiomiaPlayer().getBiomiaPlayerID()))
                    qp.rmFromQuest(quest);
        }
        Scoreboards.setTabList(e.getPlayer());

        if (ItemConditions.hasItemInInventory(qp, Material.ELYTRA, 1, QuestItemNames.twoMinuteElytra)
                || ItemConditions.hasItemOnArmor(qp, Material.ELYTRA, 1, QuestItemNames.twoMinuteElytra)) {
            TakeItemEvent ie = new TakeItemEvent(Material.ELYTRA, QuestItemNames.twoMinuteElytra, 1);
            ie.executeEvent(qp);
        }

        qp.updateBook();
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {

        ItemStack i = e.getItemDrop().getItemStack();
        if (i.getType() == Material.EGG && i.getItemMeta().getDisplayName().equals(QuestItemNames.specialEgg)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak_(BlockBreakEvent e) {

        if (!Biomia.getQuestPlayer(e.getPlayer()).getMineableBlocks().contains(e.getBlock().getType())) {
            e.setCancelled(true);
        }

        if (e.getBlock().getLocation().distance(holzfarmLoc) <= 40) {
            // HOLZFARM
            Block b = e.getBlock();
            Material material = b.getType();
            if (material == Material.LOG || material == Material.LOG_2 || material == Material.LEAVES
                    || material == Material.LEAVES_2) {

                Location loc = e.getBlock().getLocation();
                @SuppressWarnings("deprecation")
                byte data = b.getData();
                e.setCancelled(false);

                replace(60 * 5, loc, data, material);
            }
        } else if (e.getBlock().getLocation().distance(cobblefarmLoc) <= 30) {
            // STEINFARM
            Block b = e.getBlock();
            Material material = b.getType();
            if (material == Material.COBBLESTONE || material == Material.STONE || material == Material.DIRT
                    || material == Material.GRAVEL || material == Material.GRASS || material == Material.COAL_ORE) {

                Location loc = e.getBlock().getLocation();
                @SuppressWarnings("deprecation")
                byte data = b.getData();
                e.setCancelled(false);

                replace(60 * 5, loc, data, material);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace_(BlockPlaceEvent e) {
        if (!Biomia.getQuestPlayer(e.getPlayer()).getBuildableBlocks().contains(e.getBlock().getType()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onLeaveBreak(LeavesDecayEvent e) {
        if (e.getBlock().getLocation().distance(new Location(Bukkit.getWorld("Quests"), 283, 69, -234)) <= 40) {

            Location loc = e.getBlock().getLocation();
            Block b = e.getBlock();
            @SuppressWarnings("deprecation")
            byte data = b.getData();
            Material material = b.getType();
            e.setCancelled(false);

            new BukkitRunnable() {
                @SuppressWarnings("deprecation")
                @Override
                public void run() {
                    loc.getBlock().setType(material);
                    loc.getBlock().setData(data);
                    loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 3);
                }
            }.runTaskLater(Main.getPlugin(), 20 * 5);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getTo().distance(holzfarmLoc) <= 40 && e.getFrom().distance(holzfarmLoc) > 40) {
            ActionBar.sendActionBar(QuestMessages.woodFarmEnter, e.getPlayer());
        } else if (e.getTo().distance(holzfarmLoc) > 40 && e.getFrom().distance(holzfarmLoc) <= 40) {
            ActionBar.sendActionBar(QuestMessages.woodFarmLeave, e.getPlayer());
        } else if (e.getTo().distance(cobblefarmLoc) <= 30 && e.getFrom().distance(cobblefarmLoc) > 30) {
            ActionBar.sendActionBar(QuestMessages.cobbleFarmEnter, e.getPlayer());
        } else if (e.getTo().distance(cobblefarmLoc) > 30 && e.getFrom().distance(cobblefarmLoc) <= 30) {
            ActionBar.sendActionBar(QuestMessages.cobbleFarmLeave, e.getPlayer());
        }
    }

    private void replace(int durationInSeconds, Location loc, byte data, Material material) {
        new BukkitRunnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                loc.getBlock().setType(material);
                // noinspection deprecation
                loc.getBlock().setData(data);
                loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 3);
            }
        }.runTaskLater(Main.getPlugin(), 20 * durationInSeconds);
    }

}
