package de.biomia.server.freebuild.newhome;

import de.biomia.Biomia;
import de.biomia.commands.BiomiaCommand;
import de.biomia.dataManager.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeCommands extends BiomiaCommand {

    protected HomeCommands(String command) {
        super(command);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        Location ploc = p.getLocation();
        switch (getName().toLowerCase()) {
            case "setwarp":
                Location oldHomeLocation = getLocation(p);
                if (oldHomeLocation != null) {
                    //server
                } else {
                    MySQL.executeUpdate("INSERT INTO Warps (`biomiaID`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES (" +
                            Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + "," +
                            ploc.getBlockX() + "," +
                            ploc.getBlockY() + "," +
                            ploc.getBlockZ() + "," +
                            ploc.getYaw() + "," +
                            ploc.getPitch() +
                            ")", MySQL.Databases.biomia_db);
                }
                break;
            case "warp":
                Location homeLoc = getLocation(p);
                if (homeLoc == null) {
                    p.sendMessage("&cKein Home gefunden.");
                } else p.teleport(homeLoc);
                break;
            default:
                break;
        }
        return true;
    }

    private Location getLocation(Player p) {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        Location loc = null;
        try {
            PreparedStatement ps = con.prepareStatement(
                    "Select x,y,z,yaw,pitch from FreebuildHome where biomiaPlayerID = ?");
            ps.setString(2, Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + "");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                float yaw = (float) rs.getDouble("yaw");
                float pitch = (float) rs.getDouble("pitch");
                loc = new Location(Bukkit.getWorld("world"), x, y, z, yaw, pitch);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loc;
    }

}
