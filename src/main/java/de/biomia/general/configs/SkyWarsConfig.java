package de.biomia.general.configs;

import de.biomia.general.configs.Config;
import de.biomia.minigames.sw.chests.Chests;
import de.biomia.api.Biomia;
import de.biomia.api.Teams.Team;
import de.biomia.api.Teams.Teams;
import de.biomia.api.main.Main;
import de.biomia.minigames.sw.var.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.UUID;

public class SkyWarsConfig extends Config {
    
    public static void addLocation(Location loc) {
        int lastID = getConfig().getInt("lastID");

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();
        String wo = loc.getWorld().getName();

        getConfig().set("Spawnpoints." + (lastID + 1) + ".X", x + 0.5);
        getConfig().set("Spawnpoints." + (lastID + 1) + ".Y", y);
        getConfig().set("Spawnpoints." + (lastID + 1) + ".Z", z + 0.5);
        getConfig().set("Spawnpoints." + (lastID + 1) + ".Yaw", ya);
        getConfig().set("Spawnpoints." + (lastID + 1) + ".World", wo);

        getConfig().set("lastID", lastID + 1);
        Variables.teamSpawns.put(Biomia.getTeamManager().getTeams().get(lastID), loc);
        Main.getPlugin().saveConfig();
    }

    public static void removeAllLocations() {

        for (int i = 0; i <= getConfig().getInt("lastID"); i++) {
            getConfig().set("Spawnpoints." + i, null);
        }

        getConfig().set("lastID", 0);

        Variables.teamSpawns.clear();
        Main.getPlugin().saveConfig();
    }

    public static void removeAllChests() {

        for (int i = 0; i <= getConfig().getInt("lastNormalChestID"); i++) {
            getConfig().set("Chests.NormalChest." + i + "", null);
        }
        Variables.normalChests.clear();
        for (int i = 0; i <= getConfig().getInt("lastGoodChestID"); i++) {
            getConfig().set("Chests.GoodChest." + i + "", null);
        }

        getConfig().set("lastNormalChestID", 0);
        getConfig().set("lastGoodChestID", 0);

        Variables.goodChests.clear();
        Main.getPlugin().saveConfig();
    }

    public static void addGoodChestLocation(Location loc) {
        int lastChestID = getConfig().getInt("lastGoodChestID");

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        String wo = loc.getWorld().getName();

        getConfig().set("Chests.GoodChest." + (lastChestID + 1) + ".X", x);
        getConfig().set("Chests.GoodChest." + (lastChestID + 1) + ".Y", y);
        getConfig().set("Chests.GoodChest." + (lastChestID + 1) + ".Z", z);
        getConfig().set("Chests.GoodChest." + (lastChestID + 1) + ".World", wo);

        getConfig().set("lastGoodChestID", lastChestID + 1);
        Variables.goodChests.add(Chests.getChestByLoc(loc));
        Main.getPlugin().saveConfig();
    }

    public static void addNormalChestLocation(Location loc) {
        int lastChestID = getConfig().getInt("lastNormalChestID");

        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        String wo = loc.getWorld().getName();

        getConfig().set("Chests.NormalChest." + (lastChestID + 1) + ".X", x);
        getConfig().set("Chests.NormalChest." + (lastChestID + 1) + ".Y", y);
        getConfig().set("Chests.NormalChest." + (lastChestID + 1) + ".Z", z);
        getConfig().set("Chests.NormalChest." + (lastChestID + 1) + ".World", wo);

        getConfig().set("lastNormalChestID", lastChestID + 1);
        Variables.normalChests.add(Chests.getChestByLoc(loc));
        Main.getPlugin().saveConfig();

    }

    public static void loadLocsFromConfig() {
        for (int i = 0; i <= getConfig().getInt("lastID"); i++) {
            if (getConfig().getString("Spawnpoints." + i + ".World") != null) {
                double x = getConfig().getDouble("Spawnpoints." + i + ".X");
                double y = getConfig().getDouble("Spawnpoints." + i + ".Y");
                double z = getConfig().getDouble("Spawnpoints." + i + ".Z");
                float ya = getConfig().getInt("Spawnpoints." + i + ".Yaw");
                World wo = Bukkit.getWorld(getConfig().getString("Spawnpoints." + i + ".World"));

                Location loc = new Location(wo, x, y, z, ya, 0);
                Variables.teamSpawns.put(Biomia.getTeamManager().getTeams().get(i - 1), loc);
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

        getConfig().set("Signs." + (id) + ".X", x);
        getConfig().set("Signs." + (id) + ".Y", y);
        getConfig().set("Signs." + (id) + ".Z", z);
        getConfig().set("Signs." + (id) + ".Facing", signData.getFacing().name());
        getConfig().set("Signs." + (id) + ".World", wo);

        Variables.signLocations.put(sign, id);
        Main.getPlugin().saveConfig();
    }

    public static void removeAllSigns() {
        getConfig().set("Signs", null);
        Main.getPlugin().saveConfig();
    }

    public static void loadChestsFromConfig() {
        for (int i = 0; i <= getConfig().getInt("lastNormalChestID"); i++) {
            if (getConfig().getString("Chests.NormalChest." + i + ".World") != null) {
                double x = getConfig().getDouble("Chests.NormalChest." + i + ".X");
                double y = getConfig().getDouble("Chests.NormalChest." + i + ".Y");
                double z = getConfig().getDouble("Chests.NormalChest." + i + ".Z");
                World wo = Bukkit.getWorld(getConfig().getString("Chests.NormalChest." + i + ".World"));

                Location loc = new Location(wo, x, y, z);
                Variables.normalChests.add(Chests.getChestByLoc(loc));

            }
        }

        for (int i = 0; i <= getConfig().getInt("lastGoodChestID"); i++) {
            if (getConfig().getString("Chests.GoodChest." + i + ".World") != null) {
                double x = getConfig().getDouble("Chests.GoodChest." + i + ".X");
                double y = getConfig().getDouble("Chests.GoodChest." + i + ".Y");
                double z = getConfig().getDouble("Chests.GoodChest." + i + ".Z");
                World wo = Bukkit.getWorld(getConfig().getString("Chests.GoodChest." + i + ".World"));

                Location loc = new Location(wo, x, y, z);
                Variables.goodChests.add(Chests.getChestByLoc(loc));
            }
        }
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
                Variables.joiner.put(team, UUID.fromString(stringuuid));
            } else {
                continue;
            }

            Bukkit.getEntity(UUID.fromString(stringuuid))
                    .setCustomName(team.getColorcode() + Biomia.getTeamManager().translate(team.getTeamname()));
        }

    }

    public static void removeTeamJoiner(Team t) {
        getConfig().set("Joiner." + t.getTeamname(), null);
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
            Main.getPlugin().getLogger().info("configs.yml not found, creating!");
            Main.getPlugin().saveDefaultConfig();
            Main.getPlugin().getConfig().options().copyDefaults(true);
        } else {
            Main.getPlugin().getLogger().info("configs.yml found, loading!");
        }
    }

}