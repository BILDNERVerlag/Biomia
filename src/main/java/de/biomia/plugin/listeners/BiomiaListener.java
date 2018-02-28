package de.biomia.plugin.listeners;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.connect.Connect;
import de.biomia.api.cosmetics.Cosmetic;
import de.biomia.api.cosmetics.Cosmetic.Group;
import de.biomia.api.cosmetics.CosmeticPetItem;
import de.biomia.api.msg.HeaderAndFooter;
import de.biomia.api.msg.Messages;
import de.biomia.api.pex.Rank;
import de.biomia.api.tools.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
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
import org.bukkit.inventory.ItemStack;

import java.util.Observable;

public class BiomiaListener extends Observable implements Listener {

    @EventHandler
    public static void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getItem() != null) {
                if (e.getItem().getType().equals(Material.MAGMA_CREAM)
                        && e.getItem().getItemMeta().getDisplayName().equals("\u00A7cLobby"))
                    Connect.connectToRandom(e.getPlayer(), "Lobby");
            }
        }
        if (e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock().getType() == Material.SOIL) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            String name = e.getCurrentItem().getItemMeta().getDisplayName();
            if (Cosmetic.openGroupInventory(Biomia.getBiomiaPlayer((Player) e.getWhoClicked()), name)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        Cosmetic.load(bp);
        if (Rank.getRank(e.getPlayer()).equals("default"))
            Rank.setRank(e.getPlayer(), "UnregSpieler");
        e.setJoinMessage(null);
        HeaderAndFooter.sendHeaderAndFooter(e.getPlayer(), "\n\u00A75Bio\u00A72mia\n",
                "\u00A75Website: \u00A72www.biomia.de\n\u00A75TS: \u00A72ts.biomia.de");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        e.setQuitMessage(null);
        for (Group g : Cosmetic.getGroups().keySet())
            Cosmetic.getGroups().get(g).remove(bp);
        Biomia.removePlayers(e.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        setChanged();
        notifyObservers(e);
        if (!Biomia.getBiomiaPlayer(e.getPlayer()).canBuild())
            if (!Biomia.getQuestPlayer(e.getPlayer()).getMineableBlocks().contains(e.getBlock().getType()))
                e.setCancelled(true);
    }

    @EventHandler
    public void onBlockSet(BlockPlaceEvent e) {
        if (!Biomia.getBiomiaPlayer(e.getPlayer()).canBuild())
            if (!Biomia.getQuestPlayer(e.getPlayer()).getBuildableBlocks().contains(e.getBlock().getType()))
                e.setCancelled(true);
    }

    @EventHandler
    public void damageToPlayer(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!Biomia.getBiomiaPlayer((Player) e.getEntity()).canGetDamage()) {
                e.setCancelled(true);
            }
        } else if (CosmeticPetItem.isPet(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void damageByPlayer(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();

            if (!Biomia.getBiomiaPlayer(p).canDamageEntitys()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent pe) {

        String cmd = pe.getMessage().split(" ")[0];

        String[] args = pe.getMessage().substring(cmd.length()).split(" ");

        if (cmd.equalsIgnoreCase("/rl") || cmd.equalsIgnoreCase("/reload")) {
            pe.setCancelled(true);
            if (pe.getPlayer().hasPermission("biomia.reload")) {
                Bukkit.broadcastMessage(Messages.PREFIX + "\u00A7cAlle Serverdateien werden von \u00A76"
                        + pe.getPlayer().getDisplayName() + " \u00A7cneu geladen!");
                Bukkit.reload();
                Bukkit.broadcastMessage(Messages.PREFIX + "\u00A7aAlle Serverdateien wurden erfolgreich von \u00A76"
                        + pe.getPlayer().getDisplayName() + " \u00A7aneu geladen!");
            } else
                pe.getPlayer().sendMessage(Messages.NO_PERM);
        } else if (cmd.equalsIgnoreCase("/gamemode")) {

            pe.setCancelled(true);
            Player p = pe.getPlayer();

            if (args.length >= 1) {

                GameMode gameMode = null;

                switch (args[0].toLowerCase()) {
                    case "0":
                    case "s":
                    case "survival":
                        gameMode = GameMode.SURVIVAL;
                        break;
                    case "1":
                    case "c":
                    case "creative":
                        gameMode = GameMode.CREATIVE;
                        break;
                    case "2":
                    case "a":
                    case "adventure":
                        gameMode = GameMode.ADVENTURE;
                        break;
                    case "3":
                    case "sp":
                    case "spectator":
                        gameMode = GameMode.SPECTATOR;
                        break;
                    default:
                        break;
                }

                if (args.length >= 2) {
                    if (gameMode != null) {

                        Player target = Bukkit.getPlayer(args[1]);

                        target.setGameMode(gameMode);
                        target.sendMessage("\u00A75Du bist nun im GameMode \u00A72" + gameMode.name().toLowerCase());
                        p.sendMessage("\u00A75Der Spieler\u00A72 " + target.getName() + " \u00A75ist nun im GameMode \u00A72"
                                + gameMode.name().toLowerCase());
                    }
                } else {

                    if (gameMode != null) {
                        p.setGameMode(gameMode);
                        p.sendMessage("\u00A75Du bist nun im GameMode \u00A72" + gameMode.name().toLowerCase());
                    }
                }
            } else {
                p.sendMessage("/gm <GameMode> [Spieler]");
            }
        }

    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e) {

        if (e.getRightClicked() instanceof ArmorStand) {

            ItemStack is = e.getPlayer().getInventory().getItemInMainHand();

            if (is != null && is.getType().equals(Material.ARMOR_STAND)
                    && is.getItemMeta().getDisplayName().equals("\u00A7cHologrammentferner")) {
                Hologram.removeHologram((ArmorStand) e.getRightClicked());
            }
        }

        if (CosmeticPetItem.isOwner(Biomia.getBiomiaPlayer(e.getPlayer()), e.getRightClicked())) {
            e.getRightClicked().addPassenger(e.getPlayer());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
    }

}
