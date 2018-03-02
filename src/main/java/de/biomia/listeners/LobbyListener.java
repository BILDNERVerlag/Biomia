package de.biomia.listeners;

import de.biomia.Biomia;
import de.biomia.commands.lobby.LobbySettingsCommand;
import de.biomia.listeners.lobby.Inventory;
import de.biomia.listeners.lobby.Join;
import de.biomia.server.lobby.Lobby;
import de.biomia.server.lobby.LobbyScoreboard;
import de.biomia.tools.RankManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class LobbyListener extends BiomiaListener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent di) {
        if (di.getItemDrop().getItemStack().getItemMeta().getDisplayName() != null) {
            if (!Biomia.getBiomiaPlayer(di.getPlayer()).canBuild()) {
                di.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityInteract(PlayerInteractAtEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onHungerSwitch(FoodLevelChangeEvent fe) {
        if (fe.getEntity() instanceof Player) {
            Player pl = (Player) fe.getEntity();
            pl.setFoodLevel(200);
            fe.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent pr) {
        Player pl = pr.getPlayer();

        Inventory.setInventory(pl);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.getPlayer().getInventory().clear();
    }

    @EventHandler
    public void onJoin_(PlayerJoinEvent pj) {
        Player p = pj.getPlayer();
        p.setAllowFlight(true);
        sendRegMsg(p);
        Inventory.setInventory(p);
        LobbyScoreboard.sendScoreboard(p);

        for (Player pl : Lobby.getSilentLobby()) {
            p.hidePlayer(pl);
            pl.hidePlayer(p);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setDamage(0);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onArmorStandDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ArmorStand || e.getEntity() instanceof ItemFrame) {
            if (e.getDamager() instanceof Player) {
                Player pl = (Player) e.getDamager();
                if (!LobbySettingsCommand.targetarmorstands.contains(pl)) {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void stopCreepers(EntityExplodeEvent e) {
        e.blockList().clear();
    }

    @EventHandler
    public void stopOtherExplosions(BlockExplodeEvent e) {
        e.blockList().clear();
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e) {

        Player p = e.getPlayer();
        if (p.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
            if (!Lobby.getInAir().contains(p)) {
                p.setFlying(false);
                p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1, 0);
                Vector jump = p.getLocation().getDirection().multiply(2.6D).setY(1.2);
                p.setVelocity(p.getVelocity().add(jump));
                p.setAllowFlight(false);
                Lobby.getInAir().add(p);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Lobby.getInAir().contains(e.getPlayer())) {
            if (e.getPlayer().isOnGround()) {
                e.getPlayer().setAllowFlight(true);
                Lobby.getInAir().remove(e.getPlayer());
            }
        }
    }

    private static void sendRegMsg(Player p) {
        if (RankManager.getRank(p).equals("UnregSpieler")) {
            TextComponent register = new TextComponent();
            p.sendMessage(ChatColor.DARK_PURPLE + "Du bist noch nicht registriert!");
            register.setText(ChatColor.BLUE + "Registriere dich jetzt auf: " + ChatColor.GRAY + "www."
                    + ChatColor.DARK_PURPLE + "Bio" + ChatColor.DARK_GREEN + "mia"
                    + ChatColor.GRAY + ".de");
            register.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));
            p.spigot().sendMessage(register);
            p.sendMessage(ChatColor.GRAY + "Oder sp\u00fcter mit " + ChatColor.GOLD + "/register");
        }
    }
}
