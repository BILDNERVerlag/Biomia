package de.biomia.quests.listeners;

import de.biomia.quests.messages.ItemNames;
import de.biomia.quests.messages.Messages;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.quests.QuestConditions.ItemConditions;
import de.biomia.quests.QuestEvents.TakeItemEvent;
import de.biomia.quests.general.Quest;
import de.biomia.quests.general.QuestPlayer;
import de.biomia.api.main.Main;
import de.biomia.api.messages.ActionBar;
import de.biomia.api.messages.Scoreboards;
import de.biomia.api.pex.Rank;
import de.biomia.api.tools.InventorySave;
import de.biomia.api.tools.QuestItems;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class QuestListener implements Listener {

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
                && event.getItem().getItemMeta().getDisplayName().equals(ItemNames.specialEgg)) {
            event.setCancelled(true);
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                Material material = event.getItem().getType();
                if (material == Material.BUCKET || material == Material.WATER_BUCKET
                        || material == Material.LAVA_BUCKET) {
                    if (!qp.isInQuest(Biomia.QuestManager().getQuest("Wasserholen")))
                        event.setCancelled(true);
                    // TODO Eimer-Verhalten auf dem Questserver nochmal auschecken - buildable
                    // blocks mit buckets
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
    public void onDrop(PlayerDropItemEvent e) {

        ItemStack i = e.getItemDrop().getItemStack();
        if (i.getType() == Material.EGG && i.getItemMeta().getDisplayName().equals(ItemNames.specialEgg)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {

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
            ActionBar.sendActionBar(Messages.woodFarmEnter, e.getPlayer());
        } else if (e.getTo().distance(holzfarmLoc) > 40 && e.getFrom().distance(holzfarmLoc) <= 40) {
            ActionBar.sendActionBar(Messages.woodFarmLeave, e.getPlayer());
        } else if (e.getTo().distance(cobblefarmLoc) <= 30 && e.getFrom().distance(cobblefarmLoc) > 30) {
            ActionBar.sendActionBar(Messages.cobbleFarmEnter, e.getPlayer());
        } else if (e.getTo().distance(cobblefarmLoc) > 30 && e.getFrom().distance(cobblefarmLoc) <= 30) {
            ActionBar.sendActionBar(Messages.cobbleFarmLeave, e.getPlayer());
        }
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent e) {
        InventorySave.saveInventory(e.getPlayer(), "QuestServer");
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent e) {
        InventorySave.setInventory(e.getPlayer(), "QuestServer");

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        QuestItems.giveQuestItems(bp);

        QuestPlayer qp = bp.getQuestPlayer();
        for (Quest quest : Biomia.QuestManager().getQuests()) {
            if (quest.getRemoveOnReload())
                if (quest.getActivePlayerBiomiaIDs().contains(qp.getBiomiaPlayer().getBiomiaPlayerID()))
                    qp.rmFromQuest(quest);
        }
        Scoreboards.setTabList(e.getPlayer());

        if (ItemConditions.hasItemInInventory(qp, Material.ELYTRA, 1, ItemNames.twoMinuteElytra)
                || ItemConditions.hasItemOnArmor(qp, Material.ELYTRA, 1, ItemNames.twoMinuteElytra)) {
            TakeItemEvent ie = new TakeItemEvent(Material.ELYTRA, ItemNames.twoMinuteElytra, 1);
            ie.executeEvent(qp);
        }

        qp.updateBook();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String msg = e.getMessage();
        String format;
        String group = Rank.getPrefix(p);

        if (p.hasPermission("biomia.coloredchat")) {
            msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());
            format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "\u00A77: \u00A7f" + msg;
            e.setFormat(format);
        } else {
            format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "\u00A77: \u00A7f" + msg;
            e.setFormat(format);
        }
    }

}
