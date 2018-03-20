package de.biomia.spigot.configs;

import de.biomia.spigot.events.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameType;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class SkyWarsConfig extends MinigamesConfig {

    public SkyWarsConfig(GameMode mode) {
        super(mode);
    }

    public static void addChestLocation(Location loc, SkyWarsOpenChestEvent.ChestType type) {

        String lastID = "last" + type.name() + "ID";

        int lastChestID = getConfig().getInt(lastID);
        getConfig().set(lastID, lastChestID);
        addLocation(loc, "Chests." + type.name() + "." + ++lastChestID, GameType.SKY_WARS);
    }

    public HashMap<SkyWarsOpenChestEvent.ChestType, ArrayList<Location>> loadChestsFromConfig(GameInstance instance) {

        HashMap<SkyWarsOpenChestEvent.ChestType, ArrayList<Location>> map = new HashMap<>();

        for (SkyWarsOpenChestEvent.ChestType type : SkyWarsOpenChestEvent.ChestType.values()) {
            String lastID = "last" + type.name() + "ID";
            ArrayList<Location> list = map.put(type, new ArrayList<>());
            for (int i = 0; i <= getConfig().getInt(lastID); i++)
                list.add(getLocation(instance, "Chests." + type.name() + "." + i));
        }
        return map;
    }
}