package de.biomia.spigot.listeners.servers;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.versus.VSMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class VersusLobbyListener extends BiomiaListener {

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.getWorld().getName().contains("Spawn"))
            p.teleport(((VSMain) Biomia.getSeverInstance()).getManager().getHome());
        ((VSMain) Biomia.getSeverInstance()).getManager().setInventory(p);
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
                e.getPlayer().teleport(((VSMain) Biomia.getSeverInstance()).getManager().getHome());
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
    public void onDeath_(PlayerDeathEvent e) {
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
}
