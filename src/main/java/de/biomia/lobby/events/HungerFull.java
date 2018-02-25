package de.biomia.lobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerFull implements Listener {

	@EventHandler
	public void onHungerSwitch(FoodLevelChangeEvent fe) {
		if (fe.getEntity() instanceof Player) {
			Player pl = (Player) fe.getEntity();
			pl.setFoodLevel(200);
			fe.setCancelled(true);
		}
	}

}
