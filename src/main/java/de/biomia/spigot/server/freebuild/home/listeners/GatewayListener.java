package de.biomia.spigot.server.freebuild.home.listeners;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.freebuild.home.homes.HomeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GatewayListener implements Listener {
    private final HomeManager homeManager;

    public GatewayListener(HomeManager manager) {
        homeManager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        homeManager.loadPlayerHomes(Biomia.getBiomiaPlayer(event.getPlayer()).getBiomiaPlayerID());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        homeManager.unloadPlayerHomes(Biomia.getBiomiaPlayer(event.getPlayer()).getBiomiaPlayerID());
    }
}
