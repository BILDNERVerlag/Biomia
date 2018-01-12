package de.biomiaAPI.QuestConditions;

import org.bukkit.Location;

import de.biomiaAPI.Quests.QuestPlayer;

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
