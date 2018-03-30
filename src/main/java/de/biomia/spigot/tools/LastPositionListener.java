package de.biomia.spigot.tools;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.universal.MySQL;
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

    private static final MySQL.Databases database = MySQL.Databases.biomia_db;

    private final Location defaultLocation;

    public LastPositionListener(Location defaultLoc) {
        this.defaultLocation = defaultLoc;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = getLastLocation(e.getPlayer());
                if (loc != null) {
                    e.getPlayer().teleport(loc);
                }
            }
        }.runTaskLater(Main.getPlugin(), 10);

    }

    @EventHandler
    public void onDisonnect(PlayerQuitEvent e) {
        saveLocation(e.getPlayer());
    }

    private void saveLocation(Player p) {
        Location loc = p.getLocation();

        double x = (int) (loc.getX() * 100);
        x = x / 100;
        double y = (int) (loc.getY() * 1000);
        y = y / 1000;
        double z = (int) (loc.getZ() * 100);
        z = z / 100;
        double yaw = (int) loc.getYaw();
        double pitch = (int) loc.getPitch();

        Connection con = MySQL.Connect(database);

        if (con != null)
            try {
                PreparedStatement ps;
                if (!containsLocation(p)) {
                    ps = con.prepareStatement("INSERT INTO LastPosition (`biomiaID`, `ServerGroup`, `x`, `y`, `z`, `yaw`, `pitch`, `world`) VALUES (?,?,?,?,?,?,?,?)");

                    ps.setInt(1, Biomia.getBiomiaPlayer(p).getBiomiaPlayerID());
                    ps.setString(2, Biomia.getServerInstance().getServerType().name());
                    ps.setDouble(3, x);
                    ps.setDouble(4, y);
                    ps.setDouble(5, z);
                    ps.setDouble(6, yaw);
                    ps.setDouble(7, pitch);
                    ps.setString(8, loc.getWorld().getName());
                } else {
                    ps = con.prepareStatement("UPDATE LastPosition SET `x`=?,`y`=?,`z`=?,`yaw`=?,`pitch`=?,`world`=? WHERE `biomiaID`=? AND `ServerGroup`=?");
                    ps.setDouble(1, x);
                    ps.setDouble(2, y);
                    ps.setDouble(3, z);
                    ps.setDouble(4, yaw);
                    ps.setDouble(5, pitch);
                    ps.setString(6, loc.getWorld().getName());
                    ps.setInt(7, Biomia.getBiomiaPlayer(p).getBiomiaPlayerID());
                    ps.setString(8, Biomia.getServerInstance().getServerType().name());
                }
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private boolean containsLocation(Player p) {
        Connection con = MySQL.Connect(database);
        try {
            PreparedStatement ps = con
                    .prepareStatement("Select x from LastPosition where ServerGroup = ? AND biomiaID = ?");
            ps.setString(1, Biomia.getServerInstance().getServerType().name());
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

    private Location getLastLocation(Player p) {
        Connection con = MySQL.Connect(database);
        Location loc = defaultLocation;
        try {
            PreparedStatement ps = con.prepareStatement(
                    "Select x,y,z,yaw,pitch,world from LastPosition where ServerGroup = ? AND biomiaID = ?");
            ps.setString(1, Biomia.getServerInstance().getServerType().name());
            ps.setString(2, Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + "");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                World w = Bukkit.getWorld(rs.getString("world"));
                double x = rs.getDouble("x");
                double y = rs.getDouble("y") + 0.1;
                double z = rs.getDouble("z");
                float yaw = (float) rs.getDouble("yaw");
                float pitch = (float) rs.getDouble("pitch");
                loc = new Location(w, x, y, z, yaw, pitch);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loc;
    }
}
