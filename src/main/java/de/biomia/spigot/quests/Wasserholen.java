package de.biomia.spigot.quests;

import org.bukkit.entity.EntityType;

public class Wasserholen extends Quest {

    public Wasserholen() {
        super(Band.Band1, "Wasserholen");
        registerNpc("Elsa", EntityType.VILLAGER, 0, 0, 0);
    }
}
