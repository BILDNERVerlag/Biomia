package de.biomia.server.quests.QuestConditions;

import de.biomia.server.quests.general.QuestPlayer;
import org.bukkit.Location;

class LocConditions {

	QuestPlayer qp;
	private final Location playerLoc;
	private final Location zielLoc;
	private final boolean square;
	private final int radius;

	public LocConditions(QuestPlayer qp, Location zielLoc, boolean square, int radius) {
		this.playerLoc = qp.getPlayer().getLocation();
		this.zielLoc = zielLoc;
		this.square = square;
		this.radius = radius;
	}

	public boolean isOnLocation() {

		return square && playerLoc.distanceSquared(zielLoc) <= radius || playerLoc.distance(zielLoc) <= radius;

	}

}
