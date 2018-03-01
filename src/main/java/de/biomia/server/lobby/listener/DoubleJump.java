package de.biomia.server.lobby.listener;

import de.biomia.server.lobby.Lobby;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class DoubleJump implements Listener {

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
}
