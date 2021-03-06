package de.biomia.spigot.server.quests.QuestEvents;

import de.biomia.spigot.BiomiaPlayer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SummonEntity implements Event {

    private final Location loc;
    private final EntityType entityType;
    private final int menge;

    @Override
    public void executeEvent(BiomiaPlayer qp) {
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
