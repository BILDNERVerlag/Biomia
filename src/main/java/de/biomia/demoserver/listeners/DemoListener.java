package de.biomia.demoserver.listeners;

import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.demoserver.teleporter.Bauten;
import de.biomia.general.configs.DemoConfig;
import de.biomia.demoserver.teleporter.ScrollingInventory;
import de.biomia.demoserver.Weltenlabor;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.messages.Scoreboards;
import de.biomia.api.mysql.MySQL;
import de.biomia.api.pex.Rank;
import de.biomia.api.tools.BackToLobby;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DemoListener implements Listener {

    public static HashMap<Player, ScrollingInventory> invs = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDamageEntitys(false);
        bp.setGetDamage(false);
        p.setGameMode(GameMode.SURVIVAL);
        Scoreboards.setTabList(p);

        BackToLobby.getLobbyItem(p, 8);
        p.getInventory().setItem(0, ItemCreator.itemCreate(Material.CHEST, "\u00A7dTeleporter"));

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        e.allow();
        String code = DemoConfig.config.getString("Code");

        if (code == null)
            code = "krs522tpr8a";

        if (MySQL.executeQuery(
                "SELECT `code`, `rangEingeloestFuerPlayeruuid` FROM `CodesFuerRaenge` WHERE `code` = '" + code
                        + "' AND `rangEingeloestFuerPlayeruuid` = '" + e.getPlayer().getUniqueId() + "'",
                "rangEingeloestFuerPlayeruuid", MySQL.Databases.biomia_db) == null && !e.getPlayer().hasPermission("biomia.demoserver.free")) {

            e.getPlayer().sendMessage(
                    "\u00A7cDu hast den Code f\u00fcr den Zugang zu dieser Welt nicht auf unserer Website eingeben!");
            e.getPlayer().kickPlayer("");
        }
    }

    @EventHandler
    public void onHungerSwitch(FoodLevelChangeEvent fe) {
        if (fe.getEntity() instanceof Player) {
            Player pl = (Player) fe.getEntity();
            pl.setFoodLevel(20);
            fe.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String msg = e.getMessage();
        String format;

        String group = Rank.getPrefix(p);

        if (p.hasPermission("biomia.coloredchat")) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "\u00A77: \u00A7f" + msg;
            e.setFormat(format);
        } else {
            format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "\u00A77: \u00A7f" + msg;
            e.setFormat(format);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(InventoryClickEvent ie) {
        if (ie.getCurrentItem() != null) {
            Material t = ie.getCurrentItem().getType();
            Player p = (Player) ie.getWhoClicked();
            if (!Biomia.getBiomiaPlayer(p).canBuild()) {
                ie.setCancelled(true);
                ie.setCursor(new ItemStack(Material.AIR));
            }

            for (Bauten b : Weltenlabor.bauten) {
                if (t.equals(b.getMaterial())
                        && ie.getCurrentItem().getItemMeta().getDisplayName().equals(b.getName())) {
                    p.teleport(b.getLoc());
                    p.closeInventory();
                }

            }

        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            if (e.getItem() != null && e.getItem().getItemMeta().getDisplayName().equals("\u00A7dTeleporter")) {
                Weltenlabor.si.openCopy(e.getPlayer());
                e.setCancelled(true);

            }
    }
}