package de.biomia.spigot.configs;

import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.minigames.bedwars.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.UUID;

public abstract class MinigamesConfig extends Config {

    public static String mapName = null;

    private final GameType type;

    MinigamesConfig(GameType type) {
        this.type = type;
    }

    private String getSaveTeamPath(String settingName, TeamColor team) {
        return String.format("%s.%s.%s.%s.", type.getDisplayName(), mapName == null ? "" : mapName, team.name(), settingName);

    }

    String getSavePath(String settingName) {
        return String.format("%s.%s.%s.", type.getDisplayName(), mapName == null ? "" : mapName, settingName);

    }

    public void addSpawnLocation(Location loc, TeamColor team) {
        addLocation(loc, team, "Spawnpoint");
    }

    public Location getSpawnLocation(TeamColor team, GameInstance instance) {
        return getLocation(team, instance, "Spawnpoint");
    }


    private void addLoc(Location loc, String savePath) {
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

    void addLocation(Location loc, TeamColor team, String settingName) {
        addLoc(loc, getSaveTeamPath(settingName, team));
    }

    void addLocation(Location loc, String settingName) {
        addLoc(loc, getSavePath(settingName));
        saveConfig();
    }

    public Location getLocation(GameInstance instance, String settingName) {

        String savePath = getSavePath(settingName);

        double x = getConfig().getDouble(savePath + "X");
        double y = getConfig().getDouble(savePath + "Y");
        double z = getConfig().getDouble(savePath + "Z");
        float ya = getConfig().getInt(savePath + "Yaw");

        return new Location(instance.getWorld(), x, y, z, ya, 0);
    }

    public Location getLocation(TeamColor color, GameInstance instance, String settingName) {

        String savePath = getSaveTeamPath(settingName, color);

        double x = getConfig().getDouble(savePath + "X");
        double y = getConfig().getDouble(savePath + "Y");
        double z = getConfig().getDouble(savePath + "Z");
        float ya = getConfig().getInt(savePath + "Yaw");

        return new Location(instance.getWorld(), x, y, z, ya, 0);
    }

    public void loadTeamJoiner() {

        HashMap<TeamColor, UUID> teamJoiner = new HashMap<>();
        teamJoiner.put(TeamColor.BLACK, UUID.fromString("92af2252-1787-4dc2-bb52-a9bd012865a2"));
        teamJoiner.put(TeamColor.ORANGE, UUID.fromString("d33e1331-3390-4c27-82e3-9095a3614610"));
        teamJoiner.put(TeamColor.BLUE, UUID.fromString("f75b9a9d-d122-4a70-b542-aeb69b59c61c"));
        teamJoiner.put(TeamColor.GREEN, UUID.fromString("ff626803-fa9e-47b0-ad18-d2334098376d"));
        teamJoiner.put(TeamColor.YELLOW, UUID.fromString("056ae035-7a07-470e-87c9-b4fe14c620d4"));
        teamJoiner.put(TeamColor.RED, UUID.fromString("0fdfcda5-24e9-4aae-89d7-ddfde06ae0ce"));
        teamJoiner.put(TeamColor.WHITE, UUID.fromString("23126ec9-9288-4d9e-96ea-9a9d93e1e96c"));
        teamJoiner.put(TeamColor.PURPLE, UUID.fromString("7d4309d7-344c-410e-80ef-2db2724a814b"));

        int remFromID = BedWars.getBedWars().getTeams().size();
        for (TeamColor t : TeamColor.values()) {
            Entity entity = Bukkit.getEntity(teamJoiner.get(t));
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
