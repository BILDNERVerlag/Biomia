package de.biomia.lobby.events;

import de.biomiaAPI.Biomia;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Drop implements Listener {
	@EventHandler
	public void onDrop(PlayerDropItemEvent di) {
		if (di.getItemDrop().getItemStack().getItemMeta().getDisplayName() != null) {
			if (!Biomia.getBiomiaPlayer(di.getPlayer()).canBuild()){
				di.setCancelled(true);
			}
		}
	}
}
