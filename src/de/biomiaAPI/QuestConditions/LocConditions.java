package de.biomiaAPI.QuestConditions;

import org.bukkit.Location;

import de.biomiaAPI.Quests.QuestPlayer;

public class LocConditions {

	QuestPlayer qp;
	Location playerLoc, zielLoc;
	boolean square;
	int radius;

	public LocConditions(QuestPlayer qp, Location zielLoc, boolean square, int radius) {
		this.playerLoc = qp.getPlayer().getLocation();
		this.zielLoc = zielLoc;
		this.square = square;
		this.radius = radius;
	}

	public boolean isOnLocation() {

		if (square && playerLoc.distanceSquared(zielLoc) <= radius)
				return true;
		else {
			if (playerLoc.distance(zielLoc) <= radius)
				return true;
		}

		return false;
	}

}
