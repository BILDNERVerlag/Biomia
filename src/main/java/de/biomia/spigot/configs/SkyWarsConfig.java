package de.biomia.spigot.configs;

import de.biomia.spigot.events.game.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class SkyWarsConfig extends MinigamesConfig {

    public SkyWarsConfig(GameMode mode) {
        super(mode);
    }

    public static void addChestLocation(Location loc, SkyWarsOpenChestEvent.ChestType type) {

        String lastID = "last" + type.name() + "ID";

        int lastChestID = getConfig().getInt(lastID);
        addLocation(loc, "Chests." + type.name() + "." + ++lastChestID, GameType.SKY_WARS);
        getConfig().set(lastID, lastChestID);
        saveConfig();
    }

    public HashMap<SkyWarsOpenChestEvent.ChestType, ArrayList<Block>> loadChestsFromConfig(GameInstance instance) {

        HashMap<SkyWarsOpenChestEvent.ChestType, ArrayList<Block>> map = new HashMap<>();

        for (SkyWarsOpenChestEvent.ChestType type : SkyWarsOpenChestEvent.ChestType.values()) {
            String lastID = "last" + type.name() + "ID";
            ArrayList<Block> list = new ArrayList<>();
            map.put(type, list);
            for (int i = 1; i <= getConfig().getInt(lastID); i++)
                list.add(getLocation(instance, "Chests." + type.name() + "." + i).getBlock());
        }
        return map;
    }
}