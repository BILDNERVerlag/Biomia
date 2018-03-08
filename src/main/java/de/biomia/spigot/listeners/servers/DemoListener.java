package de.biomia.spigot.listeners.servers;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.universal.MySQL;
import de.biomia.spigot.configs.DemoConfig;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.server.demoserver.Weltenlabor;
import de.biomia.spigot.server.demoserver.teleporter.Bauten;
import de.biomia.spigot.server.demoserver.teleporter.ScrollingInventory;
import de.biomia.spigot.tools.BackToLobby;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DemoListener extends BiomiaListener {

    public static HashMap<Player, ScrollingInventory> invs = new HashMap<>();

    @EventHandler
    public void onJoin_(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDamageEntitys(false);
        bp.setGetDamage(false);
        p.setGameMode(GameMode.SURVIVAL);

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

            for (Bauten b : ((Weltenlabor) Biomia.getSeverInstance()).getBauten()) {
                if (t.equals(b.getMaterial())
                        && ie.getCurrentItem().getItemMeta().getDisplayName().equals(b.getName())) {
                    p.teleport(b.getLoc());
                    p.closeInventory();
                }

            }

        }

    }

    @EventHandler
    public void onInteract_(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            if (e.getItem() != null && e.getItem().getItemMeta().getDisplayName().equals("\u00A7dTeleporter")) {

                ((Weltenlabor) Biomia.getSeverInstance()).getScrollingInv().computeIfAbsent(Biomia.getBiomiaPlayer(e.getPlayer()), inv -> new ScrollingInventory(e.getPlayer())).openInventorry();
                e.setCancelled(true);

            }
    }
}