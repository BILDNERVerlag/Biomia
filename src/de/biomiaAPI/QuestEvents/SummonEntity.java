package de.biomiaAPI.QuestEvents;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import de.biomiaAPI.Quests.QuestPlayer;

public class SummonEntity implements Event {

	QuestPlayer qp;
	Location loc;
	EntityType entityType;
	int menge;

	@Override
	public void executeEvent(QuestPlayer qp) {
		this.qp = qp;
		summonEntity();
	}

	public SummonEntity(Location loc, EntityType entityType, int menge) {
		this.loc = loc;
		this.entityType = entityType;
		this.menge = menge;
	}

	private void summonEntity() {
		for (int i = 0; i < menge; i++)
			loc.getWorld().spawnEntity(loc, entityType);
	}

}
