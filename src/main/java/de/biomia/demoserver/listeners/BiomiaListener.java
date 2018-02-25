package de.biomia.demoserver.listeners;

import de.biomia.demoserver.config.Bauten;
import de.biomia.demoserver.config.Config;
import de.biomia.demoserver.config.Teleporter;
import de.biomia.demoserver.main.ScrollingInventory;
import de.biomia.demoserver.main.WeltenlaborMain;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.msg.Scoreboards;
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

public class BiomiaListener implements Listener {

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
        Teleporter.giveItem(p);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        e.allow();
        String code = Config.config.getString("Code");

        if (code == null)
            code = "krs522tpr8a";

        if (MySQL.executeQuery(
                "SELECT `code`, `rangEingeloestFuerPlayeruuid` FROM `CodesFuerRaenge` WHERE `code` = '" + code
                        + "' AND `rangEingeloestFuerPlayeruuid` = '" + e.getPlayer().getUniqueId() + "'",
                "rangEingeloestFuerPlayeruuid", MySQL.Databases.biomia_db) == null && !e.getPlayer().hasPermission("biomia.demoserver.free")) {

            e.getPlayer().sendMessage(
                    "§cDu hast den Code für den Zugang zu dieser Welt nicht auf unserer Website eingeben!");
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
            format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "§7: §f" + msg;
            e.setFormat(format);
        } else {
            format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "§7: §f" + msg;
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

            for (Bauten b : WeltenlaborMain.bauten) {
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
            if (e.getItem() != null && e.getItem().getItemMeta().getDisplayName().equals("§dTeleporter")) {
                WeltenlaborMain.si.openCopy(e.getPlayer());
                e.setCancelled(true);

            }
    }
}