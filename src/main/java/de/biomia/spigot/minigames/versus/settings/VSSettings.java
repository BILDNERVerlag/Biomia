package de.biomia.spigot.minigames.versus.settings;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.GameType;
import de.biomia.universal.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class VSSettings {

    // id 0 = is group activated
    // id 100+ = maps

    private static final HashMap<GameType, HashMap<Integer, VSSettingItem>> settingItems = new HashMap<>();

    private final HashMap<GameType, HashMap<Integer, Boolean>> settings = new HashMap<>();

    private final BiomiaPlayer bp;

    static {
        for (GameType mode : GameType.values())
            settingItems.put(mode, new HashMap<>());
    }

    public VSSettings(BiomiaPlayer bp) {
        this.bp = bp;
        for (GameType vsm : GameType.values())
            settings.put(vsm, new HashMap<>());
        load();
    }

    private void load() {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id, wert, VSGroup FROM VSSettings WHERE BiomiaPlayer = ?");
            ps.setInt(1, bp.getBiomiaPlayerID());
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                settings.get(GameType.valueOf(rs.getString("VSGroup"))).put(rs.getInt("id"), rs.getBoolean("wert"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getSetting(VSSettingItem item) {

        HashMap<Integer, Boolean> hm = settings.get(item.getGroup().getMode());

        if (hm.containsKey(item.getID()))
            return hm.get(item.getID());
        else
            return item.getStandard();
    }

    private void setSetting(GameType group, int id, boolean wert) {
        settings.get(group).put(id, wert);
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        try {
            if (MySQL.executeQuerygetint(
                    "SELECT id from VSSettings where BiomiaPlayer = " + bp.getBiomiaPlayerID() + " AND id = " + id + " AND VSGroup = " + "'" + group.name() + "'",
                    "id", MySQL.Databases.biomia_db) == -1) {
                PreparedStatement statement = con
                        .prepareStatement("INSERT INTO `VSSettings`(`BiomiaPlayer`, VSGroup, `id`, `wert`) VALUES (?,?,?,?)");
                statement.setInt(1, bp.getBiomiaPlayerID());
                statement.setString(2, group.name());
                statement.setInt(3, id);
                statement.setBoolean(4, wert);
                statement.executeUpdate();
            } else {
                PreparedStatement statement = con
                        .prepareStatement("UPDATE `VSSettings` SET wert = ? WHERE BiomiaPlayer = ? AND id = ? AND VSGroup = ?");
                statement.setBoolean(1, wert);
                statement.setInt(2, bp.getBiomiaPlayerID());
                statement.setInt(3, id);
                statement.setString(4, group.name());
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invertSetting(VSSettingItem item) {
        GameType mode = item.getGroup().getMode();
        int id = item.getID();
        boolean b = !getSetting(item);
        setSetting(mode, id, b);
    }

    public static void putSettingItem(GameType mode, int id, VSSettingItem item) {
        settingItems.get(mode).put(id, item);
    }

    public static VSSettingItem getItem(GameType mode, int id) {
        return settingItems.get(mode).get(id);
    }
}