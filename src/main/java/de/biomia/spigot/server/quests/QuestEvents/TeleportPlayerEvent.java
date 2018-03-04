package de.biomia.spigot.server.quests.QuestEvents;

import de.biomia.spigot.server.quests.general.QuestPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

class TeleportPlayerEvent implements Event {

	private Location loc = null;
	private Entity e = null;
	
	public TeleportPlayerEvent(Location loc) {
		this.loc = loc;
	}
	
	public TeleportPlayerEvent(Entity e) {
		this.e = e;
	}
	
	@Override
	public void executeEvent(QuestPlayer qp) {
		if (loc == null && e != null) {
			qp.getPlayer().teleport(e);
		} else if (e == null && loc != null) {
			qp.getPlayer().teleport(loc);
		}
	}
	
}
