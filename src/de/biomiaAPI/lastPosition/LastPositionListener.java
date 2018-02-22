package de.biomiaAPI.lastPosition;

import at.TimoCraft.TimoCloud.api.TimoCloudAPI;
import at.TimoCraft.TimoCloud.api.objects.GroupObject;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LastPositionListener implements Listener {

    private static final String tableName = "LastPosition";
    private static final MySQL.Databases database = MySQL.Databases.biomia_db;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = getLastLocation(e.getPlayer(),
                        TimoCloudAPI.getBukkitInstance().getThisServer().getGroup());
                if (loc != null) {
                    e.getPlayer().teleport(loc);
                }
            }
        }.runTaskLater(Main.getPlugin(), 10);

    }

    @EventHandler
    public void onDisonnect(PlayerQuitEvent e) {
        saveLocation(e.getPlayer(), TimoCloudAPI.getBukkitInstance().getThisServer().getGroup());
    }

    public static void saveLocation(Player p, GroupObject group) {
        Location loc = p.getLocation();

        double x = (int) (loc.getX() * 100);
        x = x / 100;
        double y = (int) (loc.getY() * 100);
        y = y / 100;
        double z = (int) (loc.getZ() * 100);
        z = z / 100;
        double yaw = (int) loc.getYaw();
        double pitch = (int) loc.getPitch();

        Connection con = de.biomiaAPI.mysql.MySQL.Connect(database);

        if (con != null)
            try {
                PreparedStatement ps;
                if (!containsLocation(p, group)) {
                    ps = con.prepareStatement("INSERT INTO " + tableName
                            + "(`biomiaID`, `ServerGroup`, `x`, `y`, `z`, `yaw`, `pitch`, `world`) VALUES (?,?,?,?,?,?,?,?)");

                    ps.setInt(1, Biomia.getBiomiaPlayer(p).getBiomiaPlayerID());
                    ps.setString(2, group.getName());
                    ps.setDouble(3, x);
                    ps.setDouble(4, y);
                    ps.setDouble(5, z);
                    ps.setDouble(6, yaw);
                    ps.setDouble(7, pitch);
                    ps.setString(8, loc.getWorld().getName());
                } else {
                    ps = con.prepareStatement("UPDATE " + tableName
                            + " SET `x`=?,`y`=?,`z`=?,`yaw`=?,`pitch`=?,`world`=? WHERE `biomiaID`=? AND `ServerGroup`=?");
                    ps.setDouble(1, x);
                    ps.setDouble(2, y);
                    ps.setDouble(3, z);
                    ps.setDouble(4, yaw);
                    ps.setDouble(5, pitch);
                    ps.setString(6, loc.getWorld().getName());
                    ps.setInt(7, Biomia.getBiomiaPlayer(p).getBiomiaPlayerID());
                    ps.setString(8, group.getName());
                }
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private static boolean containsLocation(Player p, GroupObject group) {
        Connection con = de.biomiaAPI.mysql.MySQL.Connect(database);
        try {
            PreparedStatement ps = con
                    .prepareStatement("Select x from " + tableName + " where ServerGroup = ? AND biomiaID = ?");
            ps.setString(1, group.getName());
            ps.setString(2, Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + "");
            ResultSet s = ps.executeQuery();
            ps.close();
            if (s != null) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Location getLastLocation(Player p, GroupObject group) {
        Connection con = de.biomiaAPI.mysql.MySQL.Connect(database);
        Location loc = null;
        try {
            PreparedStatement ps = con.prepareStatement(
                    "Select x,y,z,yaw,pitch,world from " + tableName + " where ServerGroup = ? AND biomiaID = ?");
            ps.setString(1, group.getName());
            ps.setString(2, Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + "");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                World w = Bukkit.getWorld(rs.getString("world"));
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                float yaw = (float) rs.getDouble("yaw");
                float pitch = (float) rs.getDouble("pitch");
                loc = new Location(w, x, y, z, yaw, pitch);
                break;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loc;
    }
}
