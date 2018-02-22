package de.biomia.lobby.events;

import de.biomia.lobby.commands.LobbyComands;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class NoDamage implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent ee) {
		if (ee.getEntity() instanceof Player) {
			ee.setDamage(0);
			ee.setCancelled(true);
		}
	}

	@EventHandler
	public void onArmorStandDamage(EntityDamageByEntityEvent ee) {
		if (ee.getEntity() instanceof ArmorStand || ee.getEntity() instanceof ItemFrame) {
			if (ee.getDamager() instanceof Player) {
				Player pl = (Player) ee.getDamager();
				if (!LobbyComands.targetarmorstands.contains(pl)) {
					ee.setCancelled(true);
				}
			} else {
				ee.setCancelled(true);
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
}