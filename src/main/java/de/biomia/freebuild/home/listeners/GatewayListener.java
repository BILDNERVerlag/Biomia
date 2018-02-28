package de.biomia.freebuild.home.listeners;

import de.biomia.freebuild.home.homes.HomeManager;
import de.biomia.api.Biomia;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GatewayListener implements Listener {
    private final HomeManager homeManager;

    public GatewayListener(HomeManager manager) {
        homeManager = manager;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        homeManager.loadPlayerHomes(Biomia.getBiomiaPlayer(event.getPlayer()).getBiomiaPlayerID());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        homeManager.unloadPlayerHomes(Biomia.getBiomiaPlayer(event.getPlayer()).getBiomiaPlayerID());
    }
}
