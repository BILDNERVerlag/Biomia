package de.biomia.general.configs;

import de.biomia.api.Biomia;
import de.biomia.api.Teams.Team;
import de.biomia.api.Teams.Teams;
import de.biomia.Main;
import de.biomia.minigames.bedwars.var.ItemType;
import de.biomia.minigames.bedwars.var.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.UUID;

public class BedWarsConfig extends Config {

    public static void addLocation(Location loc, Teams team) {
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

        Team t = Biomia.getTeamManager().getTeam(team.name());
        if (t != null) {
            Variables.teamSpawns.put(t, loc);
        }
        Main.getPlugin().saveConfig();
    }

    public static void removeAllLocations() {
        getConfig().set("Spawnpoints", null);
        Variables.teamSpawns.clear();
        Main.getPlugin().saveConfig();
    }

    public static void loadLocsFromConfig() {
        for (Team t : Biomia.getTeamManager().getTeams()) {

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

    @SuppressWarnings("deprecation")
    public static void loadSignsFromConfig() {
        for (int i = 1; i <= 10; i++) {
            if (getConfig().getString("Signs." + i + ".World") != null) {

                double x = getConfig().getDouble("Signs." + i + ".X");
                double y = getConfig().getDouble("Signs." + i + ".Y");
                double z = getConfig().getDouble("Signs." + i + ".Z");
                BlockFace blockFace = BlockFace.valueOf(getConfig().getString("Signs." + i + ".Facing"));
                World wo = Bukkit.getWorld(getConfig().getString("Signs." + i + ".World"));

                Location loc = new Location(wo, x, y, z);

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
//					sign.update();
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
        String wo = loc.getWorld().getName();

        getConfig().set("Signs." + (id) + ".X", x);
        getConfig().set("Signs." + (id) + ".Y", y);
        getConfig().set("Signs." + (id) + ".Z", z);
        getConfig().set("Signs." + (id) + ".Facing", signData.getFacing().name());
        getConfig().set("Signs." + (id) + ".World", wo);

        Variables.signLocations.put(sign, id);
        Main.getPlugin().saveConfig();
    }

    public static void addBedsLocations(Location foot, Location head, Team team) {

        String color = team.getTeamname();

        double fx = foot.getBlockX();
        double fy = foot.getBlockY();
        double fz = foot.getBlockZ();
        String fwo = foot.getWorld().getName();

        double hx = head.getBlockX();
        double hy = head.getBlockY();
        double hz = head.getBlockZ();
        String hwo = head.getWorld().getName();

        getConfig().set("Beds." + color + ".Foot.X", fx);
        getConfig().set("Beds." + color + ".Foot.Y", fy);
        getConfig().set("Beds." + color + ".Foot.Z", fz);
        getConfig().set("Beds." + color + ".Foot.World", fwo);

        getConfig().set("Beds." + color + ".Head.X", hx);
        getConfig().set("Beds." + color + ".Head.Y", hy);
        getConfig().set("Beds." + color + ".Head.Z", hz);
        getConfig().set("Beds." + color + ".Head.World", hwo);

        ArrayList<Location> ar = new ArrayList<>();
        ar.add(head);
        ar.add(foot);

        Variables.beds.put(team, ar);
        Variables.teamsWithBeds.add(team);
        Main.getPlugin().saveConfig();
    }

    public static void loadBeds() {
        for (Team t : Biomia.getTeamManager().getTeams()) {
            String fwo = getConfig().getString("Beds." + t.getTeamname() + ".Foot.World");
            if (fwo != null) {
                double fx = getConfig().getDouble("Beds." + t.getTeamname() + ".Foot.X");
                double fy = getConfig().getDouble("Beds." + t.getTeamname() + ".Foot.Y");
                double fz = getConfig().getDouble("Beds." + t.getTeamname() + ".Foot.Z");

                String hwo = getConfig().getString("Beds." + t.getTeamname() + ".Head.World");
                double hx = getConfig().getDouble("Beds." + t.getTeamname() + ".Head.X");
                double hy = getConfig().getDouble("Beds." + t.getTeamname() + ".Head.Y");
                double hz = getConfig().getDouble("Beds." + t.getTeamname() + ".Head.Z");

                Location head = new Location(Bukkit.getWorld(hwo), hx, hy, hz);
                Location foot = new Location(Bukkit.getWorld(fwo), fx, fy, fz);

                ArrayList<Location> ar = new ArrayList<>();
                ar.add(head);
                ar.add(foot);
                Variables.beds.put(t, ar);
                Variables.teamsWithBeds.add(t);
            }
        }
    }

    public static void loadSpawner() {
        for (ItemType spawner : ItemType.values()) {
            for (int i = 1; i <= getConfig().getInt("lastID." + spawner.name()); i++) {
                String wo = getConfig().getString("Spawner." + spawner.name() + "." + i + ".World");
                double x = getConfig().getDouble("Spawner." + spawner.name() + "." + i + ".X");
                double y = getConfig().getDouble("Spawner." + spawner.name() + "." + i + ".Y");
                double z = getConfig().getDouble("Spawner." + spawner.name() + "." + i + ".Z");

                Location spawnerLoc = new Location(Bukkit.getWorld(wo), x, y, z);

                if (!Variables.spawner.containsKey(spawner))
                    Variables.spawner.put(spawner, new ArrayList<>());

                Variables.spawner.get(spawner).add(spawnerLoc);
            }
        }
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
        getConfig().set("Spawner." + spawner.name() + "." + i + ".World", wo);

        if (!Variables.spawner.containsKey(spawner)) {
            Variables.spawner.put(spawner, new ArrayList<>());
        }
        Variables.spawner.get(spawner).add(loc);
        Main.getPlugin().saveConfig();
    }

    public static void removeAllBeds() {
        getConfig().set("Beds", null);
        Main.getPlugin().saveConfig();
    }

    public static void removeAllSigns() {
        getConfig().set("Signs", null);
        Main.getPlugin().saveConfig();
    }

    public static void addTeamJoiner(UUID uuid, Team t) {
        getConfig().set("Joiner." + t.getTeamname(), uuid.toString());
        Variables.joiner.put(t, uuid);
        Main.getPlugin().saveConfig();
    }

    public static void loadTeamJoiner() {

        if (Biomia.getTeamManager().getTeams().size() == 4) {
            for (Teams t : Teams.values()) {
                if (t.name().equalsIgnoreCase("Black") || t.name().equalsIgnoreCase("White")
                        || t.name().equalsIgnoreCase("Orange") || t.name().equalsIgnoreCase("Purple")) {
                    Bukkit.getEntity(UUID.fromString(getConfig().getString("Joiner." + t.name()))).remove();
                }
            }
        }

        for (Team team : Biomia.getTeamManager().getTeams()) {
            String stringuuid = getConfig().getString("Joiner." + team.getTeamname());

            if (stringuuid != null) {
                UUID uuid = UUID.fromString(stringuuid);
                Variables.joiner.put(team, uuid);
                Bukkit.getEntity(uuid).setCustomNameVisible(true);
                Bukkit.getEntity(uuid)
                        .setCustomName(team.getColorcode() + Biomia.getTeamManager().translate(team.getTeamname()));
            }

        }

    }

    public static void removeTeamJoiner(Team t) {
        getConfig().set("Joiner." + t.getTeamname(), null);
        Main.getPlugin().saveConfig();
        if (Variables.joiner.containsKey(t))
            Variables.joiner.remove(t);
    }

}