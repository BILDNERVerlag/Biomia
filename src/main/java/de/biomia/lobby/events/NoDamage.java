package de.biomia.lobby.events;

import de.biomia.lobby.commands.LobbySettingsCommand;
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
}