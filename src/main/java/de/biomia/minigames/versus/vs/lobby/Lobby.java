package de.biomia.minigames.versus.vs.lobby;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.general.messages.BedWarsMessages;
import de.biomia.minigames.versus.VSMain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Lobby implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.getWorld().getName().contains("Spawn"))
            p.teleport(VSMain.getManager().getHome());
        VSMain.getManager().setInventory(p);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getPlayer().getWorld().getName().contains("Spawn") && !Biomia.getBiomiaPlayer(e.getPlayer()).canBuild())
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getPlayer().getWorld().getName().contains("Spawn")) {
            if (e.getTo().getBlockY() <= 0) {
                e.getPlayer().teleport(VSMain.getManager().getHome());
            }
        }
    }
    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getEntity());
            if (e.getEntity().getWorld().getName().contains("Spawn") && !bp.canBuild())
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getWorld().getName().contains("Spawn")) {
            e.setKeepInventory(true);
        }
    }

    @EventHandler
    public void onHit(EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().contains("Spawn")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent e) {
        if (e.getEntity().getWorld().getName().contains("Spawn")) {
            e.setCancelled(true);
            e.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();
        String format;
        if (p.getWorld().getName().contains("Spawn")) {
            if (p.hasPermission("biomia.coloredchat"))
                msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());
            format = BedWarsMessages.chatMessageLobby.replaceAll("%p", p.getDisplayName()).replaceAll("%msg", msg);
            e.setFormat(format);
        }
    }
}
