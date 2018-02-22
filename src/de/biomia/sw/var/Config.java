package de.biomia.sw.var;

import de.biomia.sw.chests.Chests;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.Teams.Team;
import de.biomiaAPI.Teams.Teams;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.UUID;

public class Config {

    public static final FileConfiguration config = Main.getPlugin().getConfig();

    public static void addLocation(Location loc) {
        int lastID = Config.config.getInt("lastID");

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();
        String wo = loc.getWorld().getName();

        Config.config.set("Spawnpoints." + (lastID + 1) + ".X", x + 0.5);
        Config.config.set("Spawnpoints." + (lastID + 1) + ".Y", y);
        Config.config.set("Spawnpoints." + (lastID + 1) + ".Z", z + 0.5);
        Config.config.set("Spawnpoints." + (lastID + 1) + ".Yaw", ya);
        Config.config.set("Spawnpoints." + (lastID + 1) + ".World", wo);

        Config.config.set("lastID", lastID + 1);
        Variables.teamSpawns.put(Biomia.TeamManager().getTeams().get(lastID), loc);
        Main.getPlugin().saveConfig();
    }

    public static void removeAllLocations() {

        for (int i = 0; i <= Config.config.getInt("lastID"); i++) {
            Config.config.set("Spawnpoints." + i, null);
        }

        Config.config.set("lastID", 0);

        Variables.teamSpawns.clear();
        Main.getPlugin().saveConfig();
    }

    public static void removeAllChests() {

        for (int i = 0; i <= Config.config.getInt("lastNormalChestID"); i++) {
            Config.config.set("Chests.NormalChest." + i + "", null);
        }
        Variables.normalChests.clear();
        for (int i = 0; i <= Config.config.getInt("lastGoodChestID"); i++) {
            Config.config.set("Chests.GoodChest." + i + "", null);
        }

        Config.config.set("lastNormalChestID", 0);
        Config.config.set("lastGoodChestID", 0);

        Variables.goodChests.clear();
        Main.getPlugin().saveConfig();
    }

    public static void addGoodChestLocation(Location loc) {
        int lastChestID = Config.config.getInt("lastGoodChestID");

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        String wo = loc.getWorld().getName();

        Config.config.set("Chests.GoodChest." + (lastChestID + 1) + ".X", x);
        Config.config.set("Chests.GoodChest." + (lastChestID + 1) + ".Y", y);
        Config.config.set("Chests.GoodChest." + (lastChestID + 1) + ".Z", z);
        Config.config.set("Chests.GoodChest." + (lastChestID + 1) + ".World", wo);

        Config.config.set("lastGoodChestID", lastChestID + 1);
        Variables.goodChests.add(Chests.getChestByLoc(loc));
        Main.getPlugin().saveConfig();
    }

    public static void addNormalChestLocation(Location loc) {
        int lastChestID = Config.config.getInt("lastNormalChestID");

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        String wo = loc.getWorld().getName();

        Config.config.set("Chests.NormalChest." + (lastChestID + 1) + ".X", x);
        Config.config.set("Chests.NormalChest." + (lastChestID + 1) + ".Y", y);
        Config.config.set("Chests.NormalChest." + (lastChestID + 1) + ".Z", z);
        Config.config.set("Chests.NormalChest." + (lastChestID + 1) + ".World", wo);

        Config.config.set("lastNormalChestID", lastChestID + 1);
        Variables.normalChests.add(Chests.getChestByLoc(loc));
        Main.getPlugin().saveConfig();

    }

    public static void loadLocsFromConfig() {
        for (int i = 0; i <= Config.config.getInt("lastID"); i++) {
            if (Config.config.getString("Spawnpoints." + i + ".World") != null) {
                double x = Config.config.getDouble("Spawnpoints." + i + ".X");
                double y = Config.config.getDouble("Spawnpoints." + i + ".Y");
                double z = Config.config.getDouble("Spawnpoints." + i + ".Z");
                float ya = Config.config.getInt("Spawnpoints." + i + ".Yaw");
                World wo = Bukkit.getWorld(Config.config.getString("Spawnpoints." + i + ".World"));

                Location loc = new Location(wo, x, y, z, ya, 0);
                Variables.teamSpawns.put(Biomia.TeamManager().getTeams().get(i - 1), loc);
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
//					kd = Math.round(kd * 100);
//					kd = kd / 100;
//
//					sign.setLine(0, Messages.rank.replaceAll("%rank", i + "").replaceAll("%p", stat.name));
//					sign.setLine(1, Messages.wunGames + stat.wins);
//					sign.setLine(2, Messages.kd + kd);
//					sign.setLine(3, Messages.playedGames + stat.played_games);
//					sign.update();
//
//					Block b = sign.getBlock().getLocation().add(0, 1, 0).getBlock();
//
//					b.setTypeIdAndData(Material.SKULL.getId(), Leaderboard.getFacingDirectionByte(signData.getFacing()),
//							true);
//
//					Skull s = (Skull) b.getState();
//					s.setSkullType(SkullType.PLAYER);
//					s.update();
//
//					if (s.getOwner() == null || !s.getOwner().equalsIgnoreCase(stat.name)) {
//						s.setOwner(stat.name);
//						s.update();
//					}
//
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

    public static void removeAllSigns() {
        Config.config.set("Signs", null);
        Main.getPlugin().saveConfig();
    }

    public static void loadChestsFromConfig() {
        for (int i = 0; i <= Config.config.getInt("lastNormalChestID"); i++) {
            if (Config.config.getString("Chests.NormalChest." + i + ".World") != null) {
                double x = Config.config.getDouble("Chests.NormalChest." + i + ".X");
                double y = Config.config.getDouble("Chests.NormalChest." + i + ".Y");
                double z = Config.config.getDouble("Chests.NormalChest." + i + ".Z");
                World wo = Bukkit.getWorld(Config.config.getString("Chests.NormalChest." + i + ".World"));

                Location loc = new Location(wo, x, y, z);
                Variables.normalChests.add(Chests.getChestByLoc(loc));

            }
        }

        for (int i = 0; i <= Config.config.getInt("lastGoodChestID"); i++) {
            if (Config.config.getString("Chests.GoodChest." + i + ".World") != null) {
                double x = Config.config.getDouble("Chests.GoodChest." + i + ".X");
                double y = Config.config.getDouble("Chests.GoodChest." + i + ".Y");
                double z = Config.config.getDouble("Chests.GoodChest." + i + ".Z");
                World wo = Bukkit.getWorld(Config.config.getString("Chests.GoodChest." + i + ".World"));

                Location loc = new Location(wo, x, y, z);
                Variables.goodChests.add(Chests.getChestByLoc(loc));
            }
        }
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
                Variables.joiner.put(team, UUID.fromString(stringuuid));
            } else {
                continue;
            }

            Bukkit.getEntity(UUID.fromString(stringuuid))
                    .setCustomName(team.getColorcode() + Biomia.TeamManager().translate(team.getTeamname()));
        }

    }

    public static void removeTeamJoiner(Team t) {
        Config.config.set("Joiner." + t.getTeamname(), null);
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
            Main.getPlugin().getConfig().options().copyDefaults(true);
        } else {
            Main.getPlugin().getLogger().info("config.yml found, loading!");
        }
    }

}