package de.biomia.spigot.configs;

import de.biomia.spigot.minigames.*;
import de.biomia.spigot.minigames.general.shop.ItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BedWarsConfig extends MinigamesConfig {

    public BedWarsConfig(GameMode mode) {
        super(mode);
    }

    public static void addSpawnerLocations(Location loc, ItemType spawner) {
        int i = getConfig().getInt("lastID." + spawner.name());
        addLocation(loc, "Spawner." + spawner.name() + "." + ++i, GameType.BED_WARS);
        getConfig().set("lastID." + spawner.name(), i);
        saveConfig();
    }

    public static void addBedsLocations(Location foot, Location head, TeamColor team) {
        addLocation(head, team, "Beds.Head", GameType.BED_WARS);
        addLocation(foot, team, "Beds.Foot", GameType.BED_WARS);
    }

    public ArrayList<Block> loadBeds(GameTeam t) {
        Location l1 = getLocation(t.getColor(), "Beds.Foot");
        Location l2 = getLocation(t.getColor(), "Beds.Head");
        return new ArrayList<>(Arrays.asList(l1.getBlock(), l2.getBlock()));
    }

    public HashMap<ItemType, ArrayList<Location>> loadSpawner() {
        HashMap<ItemType, ArrayList<Location>> spawner = new HashMap<>();
        for (ItemType itemType : ItemType.values()) {
            for (int i = 1; i <= getConfig().getInt("lastID." + itemType.name()); i++) {
                Location spawnerLoc = getLocation("Spawner." + itemType.name() + "." + i).add(0, 1.1, 0);
                while (spawnerLoc.getBlock().getType() != Material.AIR)
                    spawnerLoc = spawnerLoc.subtract(0, 1, 0);
                spawner.computeIfAbsent(itemType, list -> new ArrayList<>()).add(spawnerLoc);
            }
        }
        return spawner;
    }
}