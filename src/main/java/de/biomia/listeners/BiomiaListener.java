package de.biomia.listeners;

import de.biomia.Biomia;
import de.biomia.BiomiaPlayer;
import de.biomia.general.cosmetics.Cosmetic;
import de.biomia.general.cosmetics.CosmeticPetItem;
import de.biomia.general.reportsystem.PlayerReport;
import de.biomia.general.reportsystem.ReportManager;
import de.biomia.general.reportsystem.ReportSQL;
import de.biomia.messages.Messages;
import de.biomia.messages.manager.HeaderAndFooter;
import de.biomia.tools.BackToLobby;
import de.biomia.tools.PlayerToServerConnector;
import de.biomia.tools.RankManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

/**
 * The Listener of the actual Server have to extend this class
 * a few listeners can be overridden
 */
abstract class BiomiaListener implements Listener {

    @EventHandler
    protected final void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getItem() != null) {
                if (e.getItem().isSimilar(BackToLobby.getBackToLobbyItem()))
                    PlayerToServerConnector.connectToRandom(e.getPlayer(), "Lobby");
            }
        }
        if (e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock().getType() == Material.SOIL) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    protected final void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            if (Cosmetic.getMainInventory().equals(e.getClickedInventory()) && Cosmetic.openGroupInventory(Biomia.getBiomiaPlayer((Player) e.getWhoClicked()), e.getCurrentItem().getItemMeta().getDisplayName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    protected final void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (RankManager.getRank(e.getPlayer()).equals("default")) {
            RankManager.setRank(e.getPlayer(), "UnregSpieler");
        }
        HeaderAndFooter.sendHeaderAndFooter(e.getPlayer(), "\n\u00A7cBIO\u00A7bMIA\n",
                "\u00A7cWebsite: \u00A7bwww.biomia.de\n\u00A7cTS: \u00A7bts.biomia.de");
        Cosmetic.load(bp);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    protected final void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        for (Cosmetic.Group g : Cosmetic.getGroups().keySet())
            Cosmetic.getGroups().get(g).remove(bp);
        Biomia.removeBiomiaPlayer(e.getPlayer());
    }

    @EventHandler
    protected final void onBlockBreak(BlockBreakEvent e) {
        if (!Biomia.getBiomiaPlayer(e.getPlayer()).canBuild())
            e.setCancelled(true);
    }

    @EventHandler
    protected final void onBlockSet(BlockPlaceEvent e) {
        if (!Biomia.getBiomiaPlayer(e.getPlayer()).canBuild())
            e.setCancelled(true);
    }

    @EventHandler
    protected final void damageToPlayer(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!Biomia.getBiomiaPlayer((Player) e.getEntity()).canGetDamage()) {
                e.setCancelled(true);
            }
        } else if (CosmeticPetItem.isPet(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    protected final void damageByPlayer(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();

            if (!Biomia.getBiomiaPlayer(p).canDamageEntitys()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    protected final void onCommand(PlayerCommandPreprocessEvent pe) {

        String cmd = pe.getMessage().split(" ")[0];

        if (cmd.equalsIgnoreCase("/rl") || cmd.equalsIgnoreCase("/reload")) {
            pe.setCancelled(true);
            if (pe.getPlayer().hasPermission("biomia.reload")) {
                Bukkit.broadcastMessage(Messages.PREFIX + "\u00A77Alle Serverdateien\u00A7c werden \u00A77von \u00A7b" + pe.getPlayer().getDisplayName() + " \u00A7cneu geladen!");
                Bukkit.reload();
                Bukkit.broadcastMessage(Messages.PREFIX + "\u00A77Alle Serverdateien \u00A7cwurden \u00A77erfolgreich von \u00A7b" + pe.getPlayer().getDisplayName() + " \u00A77neu geladen!");
            } else
                pe.getPlayer().sendMessage(Messages.NO_PERM);
        } else if (cmd.equalsIgnoreCase("/gamemode")) {
            pe.setCancelled(true);
            pe.getPlayer().sendMessage("/gm <GameMode> [Spieler]");
        }
    }

    @EventHandler
    protected final void onInteractEntity(PlayerInteractAtEntityEvent e) {
        if (CosmeticPetItem.isOwner(Biomia.getBiomiaPlayer(e.getPlayer()), e.getRightClicked())) {
            e.getRightClicked().addPassenger(e.getPlayer());
        }
    }

    @EventHandler
    protected void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
    }

    @EventHandler
    protected void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("biomia.coloredchat")) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }
        e.setFormat(RankManager.getPrefix(p) + "%s\u00A77: \u00A7f%s");
    }

    @EventHandler
    protected void onChat_Report(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (ReportManager.waitingForBugReason.contains(p)) {
            ReportManager.waitingForBugReason.remove(p);
            e.setCancelled(true);

            ReportSQL.addBugReport(p, e.getMessage());
            p.sendMessage("\u00A7cDanke f\u00fcr deinen Bug Report! \u00A7bWir werden den Bug so schnell wie m\u00F6glich beheben!");
        } else if (ReportManager.waitingForName.contains(p)) {
            ReportManager.waitingForName.remove(p);
            e.setCancelled(true);
            new PlayerReport(bp, Biomia.getOfflineBiomiaPlayer(e.getMessage()));
            p.openInventory(ReportManager.grund);
        } else if (ReportManager.waitForCostumReason.containsKey(bp)) {
            e.setCancelled(true);
            ReportManager.waitForCostumReason.get(bp).setReason(e.getMessage());
            ReportManager.waitForCostumReason.remove(bp);
        }
    }

}
