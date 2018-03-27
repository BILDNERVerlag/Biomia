package de.biomia.spigot.configs;

import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.UUID;

public abstract class MinigamesConfig extends Config {

    public static String mapName = null;

    private final GameMode mode;

    MinigamesConfig(GameMode mode) {
        this.mode = mode;
    }

    private String getSaveTeamPath(String settingName, TeamColor team) {
        return String.format("%s.%s.%s.%s.", mode.getInstance().getType().getDisplayName(), mapName, team.name(), settingName);
    }

    private static String getSaveTeamPath(String settingName, TeamColor team, GameType type) {
        return String.format("%s.%s.%s.%s.", type.getDisplayName(), mapName, team.name(), settingName);

    }

    String getSavePath(String settingName) {
        return String.format("%s.%s.%s.", mode.getInstance().getType().getDisplayName(), mapName, settingName);
    }

    private static String getSavePath(String settingName, GameType type) {
        return String.format("%s.%s.%s.", type.getDisplayName(), mapName, settingName);
    }

    public static String getMapName() {
        return getConfig().getString("Name");
    }

    public static int getTeamSize() {
        return getConfig().getInt("TeamSize");
    }

    public static int getTeamAmount() {
        return getConfig().getInt("NumberOfTeams");
    }

    public static void addSpawnLocation(Location loc, TeamColor team, GameType type) {
        addLocation(loc, team, "Spawnpoint", type);
    }

    public Location getSpawnLocation(TeamColor team, GameInstance instance) {
        return getLocation(team, instance, "Spawnpoint");
    }


    private static void addLoc(Location loc, String savePath) {
        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        float ya = loc.getYaw();
        getConfig().set(savePath + "X", x + 0.5);
        getConfig().set(savePath + "Y", y);
        getConfig().set(savePath + "Z", z + 0.5);
        getConfig().set(savePath + "Yaw", ya);
        saveConfig();
    }

    static void addLocation(Location loc, TeamColor team, String settingName, GameType type) {
        addLoc(loc, getSaveTeamPath(settingName, team, type));
    }

    static void addLocation(Location loc, String settingName, GameType type) {
        addLoc(loc, getSavePath(settingName, type));
        saveConfig();
    }

    Location getLocation(GameInstance instance, String settingName) {

        String savePath = getSavePath(settingName);

        double x = getConfig().getDouble(savePath + "X");
        double y = getConfig().getDouble(savePath + "Y");
        double z = getConfig().getDouble(savePath + "Z");
        float ya = getConfig().getInt(savePath + "Yaw");

        return new Location(instance.getWorld(), x, y, z, ya, 0);
    }

    Location getLocation(TeamColor color, GameInstance instance, String settingName) {

        String savePath = getSaveTeamPath(settingName, color);

        double x = getConfig().getDouble(savePath + "X");
        double y = getConfig().getDouble(savePath + "Y");
        double z = getConfig().getDouble(savePath + "Z");
        float ya = getConfig().getInt(savePath + "Yaw");

        return new Location(instance.getWorld(), x, y, z, ya, 0);
    }

    public void loadTeamJoiner(HashMap<TeamColor, UUID> teamJoiner) {

        if (teamJoiner == null)
            return;

        int remFromID = mode.getTeams().size();
        for (TeamColor t : TeamColor.values()) {
            Entity entity = Bukkit.getEntity(teamJoiner.get(t));
            if (t.getID() > remFromID) {
                entity.remove();
            } else {
                mode.getJoiner().put(t, entity);
                entity.setCustomName(t.getColorcode() + t.translate());
                entity.setCustomNameVisible(true);
            }
        }
    }


}
