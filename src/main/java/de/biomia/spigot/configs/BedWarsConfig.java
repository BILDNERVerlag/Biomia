package de.biomia.spigot.configs;

import de.biomia.spigot.minigames.*;
import de.biomia.spigot.minigames.general.shop.ItemType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BedWarsConfig extends MinigamesConfig {

    public BedWarsConfig(GameMode mode) {
        super(mode);
    }

    public void addSignsLocation(Location loc, int id) {

//        Sign sign = (Sign) loc.getBlock().getState();
//        org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
//
//        addLocation(loc, "Signs." + id, GameType.SKY_WARS);
//        getConfig().set(getSavePath("Signs." + (id) + ".Facing"), signData.getFacing().name());
//
//        Variables.signLocations.put(sign, id);
        saveConfig();
    }

    public static void addSpawnerLocations(Location loc, ItemType spawner) {

        int i = getConfig().getInt("lastID." + spawner.name());
        addLocation(loc, "Spawner." + spawner.name() + "." + ++i, GameType.SKY_WARS);
        getConfig().set("lastID." + spawner.name(), i);
        saveConfig();
    }

    public static void addBedsLocations(Location foot, Location head, TeamColor team) {

        addLocation(head, team, "Beds.Head", GameType.BED_WARS);
        addLocation(foot, team, "Beds.Foot", GameType.BED_WARS);
    }

    public ArrayList<Block> loadBeds(GameInstance instance, GameTeam t) {

        Location l1 = getLocation(t.getColor(), instance, "Beds.Foot");
        Location l2 = getLocation(t.getColor(), instance, "Beds.Head");

        return new ArrayList<>(Arrays.asList(l1.getBlock(), l2.getBlock()));
    }

    public void loadSignsFromConfig(GameInstance instance) {
        for (int i = 1; i <= 10; i++) {
            if (getConfig().getString("Signs." + i + ".World") != null) {

                BlockFace blockFace = BlockFace.valueOf(getSavePath("Signs." + (i) + ".Facing"));
                Location loc = getLocation(instance, "Signs." + i);

                org.bukkit.material.Sign signData = (org.bukkit.material.Sign) loc.getBlock().getState().getData();
                signData.setFacingDirection(blockFace);
                Sign sign = (Sign) loc.getBlock().getState();
                sign.setData(signData);

//				Stats stat = Leaderboard.getStat(i);
//
//				if (stat != null) {
//
//					double kd = 0;
//					if(stat.deaths != 0) {
//						kd = stat.kills / stat.deaths;
//					}
//
//					NumberFormat n = NumberFormat.getInstance();
//					n.setMaximumFractionDigits(2);
//
//					sign.setLine(0, "?5" + stat.name + " ?2#" + stat.rank);
//					sign.setLine(1, "?8Wins:?7 " + stat.wins);
//					sign.setLine(2, "?8K/D:?7 " + n.format(kd));
//					sign.setLine(3, "?8Gespielt:?7 " + stat.played_games);
//
//					sign.update(true);
//
//					Block b = sign.getBlock().getLocation().add(0, 1, 0).getBlock();
//
//					b.setTypeIdAndData(Material.SKULL.getId(), Leaderboard.getFacingDirectionByte(signData.getFacing()),
//							true);
//
//					Skull s = (Skull) b.getState();
//					s.setSkullType(SkullType.PLAYER);
//					s.setOwner(stat.name);
//					s.update();
//				}

//                Variables.signLocations.put(sign, i);
            }
        }
    }

    public HashMap<ItemType, ArrayList<Location>> loadSpawner(GameInstance instance) {

        HashMap<ItemType, ArrayList<Location>> spawner = new HashMap<>();

        for (ItemType itemType : ItemType.values()) {
            for (int i = 1; i <= getConfig().getInt("lastID." + itemType.name()); i++) {
                Location spawnerLoc = getLocation(instance, "Spawner." + itemType.name() + "." + i);
                spawner.computeIfAbsent(itemType, list -> new ArrayList<>()).add(spawnerLoc);
            }
        }

        return spawner;
    }

}