package de.biomia.versus.vs.settings;

import de.biomia.versus.vs.main.VSManager.VSMode;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.mysql.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class VSSettings {

    // id 0 = is group activated
    // id 100+ = maps

    private static final HashMap<VSMode, HashMap<Integer, VSSettingItem>> settingItems = new HashMap<>();

    static {
        for (VSMode mode : VSMode.values())
            settingItems.put(mode, new HashMap<>());
    }

    private final BiomiaPlayer bp;
    private final HashMap<VSMode, HashMap<Integer, Boolean>> settings = new HashMap<>();

    public VSSettings(BiomiaPlayer bp) {
        this.bp = bp;
        for (VSMode vsm : VSMode.values())
            settings.put(vsm, new HashMap<>());
        load();
    }

    public static void putSettingItem(VSMode mode, int id, VSSettingItem item) {
        settingItems.get(mode).put(id, item);
    }

    public static VSSettingItem getItem(VSMode mode, int id) {
        return settingItems.get(mode).get(id);
    }

    private void setSetting(VSMode group, int id, boolean wert) {
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
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getSetting(VSSettingItem item) {

        HashMap<Integer, Boolean> hm = settings.get(item.getGroup().getMode());

        if (hm.containsKey(item.getId()))
            return hm.get(item.getId());
        else
            return item.getStandard();
    }

    public void invertSetting(VSSettingItem item) {
        VSMode mode = item.getGroup().getMode();
        int id = item.getId();
        boolean b = !getSetting(item);
        setSetting(mode, id, b);
    }

    private void load() {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        try {
            PreparedStatement ps = con
                    .prepareStatement("SELECT id, wert, VSGroup FROM VSSettings WHERE BiomiaPlayer = ?");
            ps.setInt(1, bp.getBiomiaPlayerID());
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                settings.get(VSMode.valueOf(rs.getString("VSGroup"))).put(rs.getInt("id"), rs.getBoolean("wert"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}