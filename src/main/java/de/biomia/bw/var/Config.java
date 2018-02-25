package de.biomia.bw.var;

import de.biomia.api.Biomia;
import de.biomia.api.Teams.Team;
import de.biomia.api.Teams.Teams;
import de.biomia.api.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class Config {

    public static final FileConfiguration config = Main.getPlugin().getConfig();

    public static void addLocation(Location loc, Teams team) {
        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();
        String wo = loc.getWorld().getName();

        Config.config.set("Spawnpoints." + team.name() + ".X", x + 0.5);
        Config.config.set("Spawnpoints." + team.name() + ".Y", y);
        Config.config.set("Spawnpoints." + team.name() + ".Z", z + 0.5);
        Config.config.set("Spawnpoints." + team.name() + ".Yaw", ya);
        Config.config.set("Spawnpoints." + team.name() + ".World", wo);

        Team t = Biomia.TeamManager().getTeam(team.name());
        if (t != null) {
            Variables.teamSpawns.put(t, loc);
        }
        Main.getPlugin().saveConfig();
    }

    public static void removeAllLocations() {
        Config.config.set("Spawnpoints", null);
        Variables.teamSpawns.clear();
        Main.getPlugin().saveConfig();
    }

    public static void loadLocsFromConfig() {
        for (Team t : Biomia.TeamManager().getTeams()) {

            if (Config.config.getString("Spawnpoints." + t.getTeamname() + ".World") != null) {
                double x = Config.config.getDouble("Spawnpoints." + t.getTeamname() + ".X");
                double y = Config.config.getDouble("Spawnpoints." + t.getTeamname() + ".Y");
                double z = Config.config.getDouble("Spawnpoints." + t.getTeamname() + ".Z");
                float ya = Config.config.getInt("Spawnpoints." + t.getTeamname() + ".Yaw");
                World wo = Bukkit.getWorld(Config.config.getString("Spawnpoints." + t.getTeamname() + ".World"));

                Location loc = new Location(wo, x, y, z, ya, 0);
                Variables.teamSpawns.put(t, loc);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void loadSignsFromConfig() {
        for (int i = 1; i <= 10; i++) {
            if (Config.config.getString("Signs." + i + ".World") != null) {

                double x = Config.config.getDouble("Signs." + i + ".X");
                double y = Config.config.getDouble("Signs." + i + ".Y");
                double z = Config.config.getDouble("Signs." + i + ".Z");
                BlockFace blockFace = BlockFace.valueOf(Config.config.getString("Signs." + i + ".Facing"));
                World wo = Bukkit.getWorld(Config.config.getString("Signs." + i + ".World"));

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

        Config.config.set("Signs." + (id) + ".X", x);
        Config.config.set("Signs." + (id) + ".Y", y);
        Config.config.set("Signs." + (id) + ".Z", z);
        Config.config.set("Signs." + (id) + ".Facing", signData.getFacing().name());
        Config.config.set("Signs." + (id) + ".World", wo);

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

        Config.config.set("Beds." + color + ".Foot.X", fx);
        Config.config.set("Beds." + color + ".Foot.Y", fy);
        Config.config.set("Beds." + color + ".Foot.Z", fz);
        Config.config.set("Beds." + color + ".Foot.World", fwo);

        Config.config.set("Beds." + color + ".Head.X", hx);
        Config.config.set("Beds." + color + ".Head.Y", hy);
        Config.config.set("Beds." + color + ".Head.Z", hz);
        Config.config.set("Beds." + color + ".Head.World", hwo);

        ArrayList<Location> ar = new ArrayList<>();
        ar.add(head);
        ar.add(foot);

        Variables.beds.put(team, ar);
        Variables.teamsWithBeds.add(team);
        Main.getPlugin().saveConfig();
    }

    public static void loadBeds() {
        for (Team t : Biomia.TeamManager().getTeams()) {
            String fwo = Config.config.getString("Beds." + t.getTeamname() + ".Foot.World");
            if (fwo != null) {
                double fx = Config.config.getDouble("Beds." + t.getTeamname() + ".Foot.X");
                double fy = Config.config.getDouble("Beds." + t.getTeamname() + ".Foot.Y");
                double fz = Config.config.getDouble("Beds." + t.getTeamname() + ".Foot.Z");

                String hwo = Config.config.getString("Beds." + t.getTeamname() + ".Head.World");
                double hx = Config.config.getDouble("Beds." + t.getTeamname() + ".Head.X");
                double hy = Config.config.getDouble("Beds." + t.getTeamname() + ".Head.Y");
                double hz = Config.config.getDouble("Beds." + t.getTeamname() + ".Head.Z");

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
            for (int i = 1; i <= Config.config.getInt("lastID." + spawner.name()); i++) {
                String wo = Config.config.getString("Spawner." + spawner.name() + "." + i + ".World");
                double x = Config.config.getDouble("Spawner." + spawner.name() + "." + i + ".X");
                double y = Config.config.getDouble("Spawner." + spawner.name() + "." + i + ".Y");
                double z = Config.config.getDouble("Spawner." + spawner.name() + "." + i + ".Z");

                Location spawnerLoc = new Location(Bukkit.getWorld(wo), x, y, z);

                if (!Variables.spawner.containsKey(spawner))
                    Variables.spawner.put(spawner, new ArrayList<>());

                Variables.spawner.get(spawner).add(spawnerLoc);
            }
        }
    }

    public static void addSpawnerLocations(Location loc, ItemType spawner) {

        int i = Config.config.getInt("lastID." + spawner.name());
        i++;
        Config.config.set("lastID." + spawner.name(), i);

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        String wo = loc.getWorld().getName();

        Config.config.set("Spawner." + spawner.name() + "." + i + ".X", x);
        Config.config.set("Spawner." + spawner.name() + "." + i + ".Y", y);
        Config.config.set("Spawner." + spawner.name() + "." + i + ".Z", z);
        Config.config.set("Spawner." + spawner.name() + "." + i + ".World", wo);

        if (!Variables.spawner.containsKey(spawner)) {
            Variables.spawner.put(spawner, new ArrayList<>());
        }
        Variables.spawner.get(spawner).add(loc);
        Main.getPlugin().saveConfig();
    }

    public static void removeSpawnerLocations(Location loc, ItemType spawner) {
        Config.config.set("Spawner." + spawner.name(), null);
        Main.getPlugin().saveConfig();
    }

    public static void removeAllSpawnerLocations(Location loc, ItemType spawner) {
        Config.config.set("Spawner", null);
        Main.getPlugin().saveConfig();
    }

    public static void removeAllBeds() {
        Config.config.set("Beds", null);
        Main.getPlugin().saveConfig();
    }

    public static void removeAllSigns() {
        Config.config.set("Signs", null);
        Main.getPlugin().saveConfig();
    }

    public static void addTeamJoiner(UUID uuid, Team t) {
        Config.config.set("Joiner." + t.getTeamname(), uuid.toString());
        Variables.joiner.put(t, uuid);
        Main.getPlugin().saveConfig();
    }

    public static void loadTeamJoiner() {

        if (Biomia.TeamManager().getTeams().size() == 4) {
            for (Teams t : Teams.values()) {
                if (t.name().equalsIgnoreCase("Black") || t.name().equalsIgnoreCase("White")
                        || t.name().equalsIgnoreCase("Orange") || t.name().equalsIgnoreCase("Purple")) {
                    Bukkit.getEntity(UUID.fromString(Config.config.getString("Joiner." + t.name()))).remove();
                }
            }
        }

        for (Team team : Biomia.TeamManager().getTeams()) {
            String stringuuid = Config.config.getString("Joiner." + team.getTeamname());

            if (stringuuid != null) {
                UUID uuid = UUID.fromString(stringuuid);
                Variables.joiner.put(team, uuid);
                Bukkit.getEntity(uuid).setCustomNameVisible(true);
                Bukkit.getEntity(uuid)
                        .setCustomName(team.getColorcode() + Biomia.TeamManager().translate(team.getTeamname()));
            }

        }

    }

    public static void removeTeamJoiner(Team t) {
        config.set("Joiner." + t.getTeamname(), null);
        Main.getPlugin().saveConfig();
        if (Variables.joiner.containsKey(t))
            Variables.joiner.remove(t);
    }

    public static void addDefaults() {
        if (!Main.getPlugin().getDataFolder().exists()) {
            if (!Main.getPlugin().getDataFolder().mkdirs()) {
                return;
            }
        }
        File file = new File(Main.getPlugin().getDataFolder(), "config.yml");
        if (!file.exists()) {
            Main.getPlugin().getLogger().info("config.yml not found, creating!");
            Main.getPlugin().saveDefaultConfig();
            config.options().copyDefaults(true);
        } else {
            Main.getPlugin().getLogger().info("config.yml found, loading!");
        }
    }

}