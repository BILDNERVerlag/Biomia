package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.general.Dead;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

public abstract class GameHandler implements Listener {

    protected final GameMode mode;

    protected GameHandler(GameMode mode) {
        this.mode = mode;
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getEntity());
            BiomiaPlayer damager = Biomia.getBiomiaPlayer((Player) e.getDamager());
            if (mode.containsPlayer(bp) && mode.containsPlayer(damager))
                if (mode.getTeam(bp).equals(mode.getTeam(damager)))
                    e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        GameTeam team = mode.getTeam(bp);
        String msg = e.getMessage();
        String format;
        if (mode.containsPlayer(bp)) {
            if (p.hasPermission("biomia.coloredchat"))
                msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());
            if (e.getMessage().startsWith("@")) {
                msg = msg.replaceAll("@all ", "");
                msg = msg.replaceAll("@all", "");
                msg = msg.replaceAll("@a ", "");
                msg = msg.replaceAll("@a", "");
                msg = msg.replaceAll("@ ", "");
                msg = msg.replaceAll("@", "");
                format = BedWarsMessages.chatMessageAll.replaceAll("%p", team.getColorcode() + p.getDisplayName()).replaceAll("%msg", msg);
            } else if (team != null) {
                e.setCancelled(true);
                format = BedWarsMessages.chatMessageTeam.replaceAll("%p", team.getColorcode() + p.getDisplayName()).replaceAll("%msg", msg);
                for (BiomiaPlayer teamPlayer : team.getPlayers())
                    teamPlayer.getPlayer().sendMessage(format);
                return;
            } else {
                format = BedWarsMessages.chatMessageDead.replaceAll("%p", p.getDisplayName()).replaceAll("%msg", msg);
            }
            e.setFormat(format);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (e.getFrom().equals(mode.getInstance().getWorld()))
            if (mode.containsPlayer(bp))
                mode.getTeam(bp).leave(bp);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (mode.containsPlayer(bp)) {
            mode.getTeam(bp).leave(bp);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (mode.containsPlayer(bp)) {
            if (e.getTo().getBlockY() <= 0) {
                e.getPlayer().setHealth(0);
                Dead.respawn(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (mode.containsPlayer(bp) && bp.getPlayer().getWorld().equals(mode.getInstance().getWorld())) {
            e.setRespawnLocation(mode.getInstance().getWorld().getSpawnLocation().add(0, 100, 0));
        }
    }
}
