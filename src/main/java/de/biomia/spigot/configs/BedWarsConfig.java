package de.biomia.spigot.configs;

import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.minigames.bedwars.var.Variables;
import de.biomia.spigot.minigames.general.shop.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class BedWarsConfig extends Config {

    public static void addLocation(Location loc, TeamColor team) {
        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();
        String wo = loc.getWorld().getName();

        getConfig().set("Spawnpoints." + team.name() + ".X", x + 0.5);
        getConfig().set("Spawnpoints." + team.name() + ".Y", y);
        getConfig().set("Spawnpoints." + team.name() + ".Z", z + 0.5);
        getConfig().set("Spawnpoints." + team.name() + ".Yaw", ya);
        getConfig().set("Spawnpoints." + team.name() + ".World", wo);
        saveConfig();
    }

    public static void removeAllLocations() {
        getConfig().set("Spawnpoints", null);
        Variables.teamSpawns.clear();
        saveConfig();
    }

    public static void loadLocsFromConfig() {
        for (GameTeam t : BedWars.getBedWars().getTeams()) {
            if (getConfig().getString("Spawnpoints." + t.getTeamname() + ".World") != null) {
                double x = getConfig().getDouble("Spawnpoints." + t.getTeamname() + ".X");
                double y = getConfig().getDouble("Spawnpoints." + t.getTeamname() + ".Y");
                double z = getConfig().getDouble("Spawnpoints." + t.getTeamname() + ".Z");
                float ya = getConfig().getInt("Spawnpoints." + t.getTeamname() + ".Yaw");
                World wo = Bukkit.getWorld(getConfig().getString("Spawnpoints." + t.getTeamname() + ".World"));

                Location loc = new Location(wo, x, y, z, ya, 0);
                Variables.teamSpawns.put(t, loc);
            }
        }
    }

    public static void loadSignsFromConfig(GameInstance instance) {
        for (int i = 1; i <= 10; i++) {
            if (getConfig().getString("Signs." + i + ".World") != null) {

                double x = getConfig().getDouble("Signs." + i + ".X");
                double y = getConfig().getDouble("Signs." + i + ".Y");
                double z = getConfig().getDouble("Signs." + i + ".Z");
                BlockFace blockFace = BlockFace.valueOf(getConfig().getString("Signs." + i + ".Facing"));

                Location loc = new Location(instance.getWorld(), x, y, z);

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

                Variables.signLocations.put(sign, i);
            }
        }
    }

    public static void addSignsLocation(Location loc, int id) {

        Sign sign = (Sign) loc.getBlock().getState();
        org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();

        getConfig().set("Signs." + (id) + ".X", x);
        getConfig().set("Signs." + (id) + ".Y", y);
        getConfig().set("Signs." + (id) + ".Z", z);
        getConfig().set("Signs." + (id) + ".Facing", signData.getFacing().name());

        Variables.signLocations.put(sign, id);
        saveConfig();
    }

    public static void addBedsLocations(Location foot, Location head, TeamColor team) {

        String color = team.name();

        double fx = foot.getBlockX();
        double fy = foot.getBlockY();
        double fz = foot.getBlockZ();

        double hx = head.getBlockX();
        double hy = head.getBlockY();
        double hz = head.getBlockZ();

        getConfig().set("Beds." + color + ".Foot.X", fx);
        getConfig().set("Beds." + color + ".Foot.Y", fy);
        getConfig().set("Beds." + color + ".Foot.Z", fz);

        getConfig().set("Beds." + color + ".Head.X", hx);
        getConfig().set("Beds." + color + ".Head.Y", hy);
        getConfig().set("Beds." + color + ".Head.Z", hz);

        saveConfig();
    }

    public static ArrayList<Block> loadBeds(GameInstance instance, GameTeam t) {

        double fx = getConfig().getDouble("Beds." + t.getTeamname() + ".Foot.X");
        double fy = getConfig().getDouble("Beds." + t.getTeamname() + ".Foot.Y");
        double fz = getConfig().getDouble("Beds." + t.getTeamname() + ".Foot.Z");

        double hx = getConfig().getDouble("Beds." + t.getTeamname() + ".Head.X");
        double hy = getConfig().getDouble("Beds." + t.getTeamname() + ".Head.Y");
        double hz = getConfig().getDouble("Beds." + t.getTeamname() + ".Head.Z");

        Location l1 = new Location(instance.getWorld(), fx, fy, fz);
        Location l2 = new Location(instance.getWorld(), hx, hy, hz);

        return new ArrayList<>(Arrays.asList(l1.getBlock(), l2.getBlock()));
    }

    public static HashMap<ItemType, ArrayList<Location>> loadSpawner(GameInstance instance) {

        HashMap<ItemType, ArrayList<Location>> spawner = new HashMap<>();

        for (ItemType itemType : ItemType.values()) {
            for (int i = 1; i <= getConfig().getInt("lastID." + itemType.name()); i++) {
                double x = getConfig().getDouble("Spawner." + itemType.name() + "." + i + ".X");
                double y = getConfig().getDouble("Spawner." + itemType.name() + "." + i + ".Y");
                double z = getConfig().getDouble("Spawner." + itemType.name() + "." + i + ".Z");
                Location spawnerLoc = new Location(instance.getWorld(), x, y, z);
                spawner.computeIfAbsent(itemType, list -> new ArrayList<>()).add(spawnerLoc);
            }
        }

        return spawner;
    }

    public static void addSpawnerLocations(Location loc, ItemType spawner) {

        int i = getConfig().getInt("lastID." + spawner.name());
        i++;
        getConfig().set("lastID." + spawner.name(), i);

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        String wo = loc.getWorld().getName();

        getConfig().set("Spawner." + spawner.name() + "." + i + ".X", x);
        getConfig().set("Spawner." + spawner.name() + "." + i + ".Y", y);
        getConfig().set("Spawner." + spawner.name() + "." + i + ".Z", z);

        if (!Variables.spawner.containsKey(spawner)) {
            Variables.spawner.put(spawner, new ArrayList<>());
        }
        Variables.spawner.get(spawner).add(loc);
        saveConfig();
    }

    public static void removeAllSigns() {
        getConfig().set("Signs", null);
        saveConfig();
    }

    public static void addTeamJoiner(Entity entity, TeamColor t) {
        getConfig().set("Joiner." + t.name(), entity.getUniqueId().toString());
        Variables.joiner.put(t, entity);
        saveConfig();
    }

    public static void loadTeamJoiner() {

        int remFromID = BedWars.getBedWars().getTeams().size();
        for (TeamColor t : TeamColor.values()) {
            Entity entity = Bukkit.getEntity(UUID.fromString(getConfig().getString("Joiner." + t.name())));
            if (t.getID() > remFromID) {
                entity.remove();
            } else {
                Variables.joiner.put(t, entity);
                entity.setCustomName(t.getColorcode() + t.translate());
                entity.setCustomNameVisible(true);
            }
        }

    }
}