package de.biomia.lobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Respawn implements Listener {
	@EventHandler
	public void onRespawn(PlayerRespawnEvent pr) {
		Player pl = pr.getPlayer();

		Inventory.setInventory(pl);
	}
}
