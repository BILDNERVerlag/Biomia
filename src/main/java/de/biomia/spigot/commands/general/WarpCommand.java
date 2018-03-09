package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.tools.RankManager;
import de.biomia.universal.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class WarpCommand extends BiomiaCommand {

    public WarpCommand(String command) {
        super(command);
        allowedGroups = new ArrayList<String>(Arrays.asList("BauServer", "FreebuildServer", "FarmServer", "QuestServer"));
    }

    ArrayList<String> allowedGroups;

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;
        Location ploc = p.getLocation();
        HashMap<String, WarpLocation> map = getAllLocations(p);
        switch (getName().toLowerCase()) {
            case "setwarp":
                if (args.length < 1) {
                    sendWarpInstructions(p);
                    return true;
                }
                if (map.size() >= RankManager.getPremiumLevel(p.getName()) + 3) {
                    p.sendMessage("\u00A7cDu hast bereits die \u00A7bmaximale \u00A7cAnzahl Warps erreicht.");
                    p.sendMessage("\u00A7cBenutze \u00A77/\u00A7cdelwarp \u00A77<\u00A7cName\u00A77> \u00A7bum Warps zu l\u00f6schen.");
                    return true;
                }
                if (!allowedGroups.contains(Main.getGroupName())) {
                    p.sendMessage("\u00A7cWarps sind auf diesem Server (\u00A7b" + Main.getGroupName() + "\u00A7c) nicht erlaubt. Sorry!");
                    return true;
                }

                MySQL.executeUpdate("INSERT INTO Warps (`x`, `y`, `z`, `yaw`, `pitch`, `groupname`, `worldname`, `name`, `biomiaPlayerID`) VALUES (" +
                        ploc.getBlockX() + "," +
                        ploc.getBlockY() + "," +
                        ploc.getBlockZ() + "," +
                        ploc.getYaw() + "," +
                        ploc.getPitch() + ",'" +
                        Main.getGroupName() + "','" +
                        p.getWorld().getName() + "','" +
                        args[0] + "'," +
                        Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() +
                        ")", MySQL.Databases.biomia_db);
                p.sendMessage("\u00A7cDu hast den Warp \u00A7b" + args[0] + "\u00A7c erfolgreich erstellt!");
                map = getAllLocations(p);
                int verbleibendeWarps = RankManager.getPremiumLevel((p.getName())) + 3 - map.size();
                if (verbleibendeWarps <= 0)
                    p.sendMessage("\u00A77Dies war dein letzter verbleibender Warppunkt.");
                else p.sendMessage("\u00A77Verbleibende Warppunkte: " + verbleibendeWarps + " \u00A77(vom Rang abh\u00e4ngig)");
                break;
            case "warp":
                if (args.length < 1) {
                    if (map.isEmpty()) {
                        sendWarpInstructions(p);
                    } else {
                        p.sendMessage("\u00A7cGib einen deiner Warps an! \u00A7bDu hast folgende:");
                        sendWarpList(p, map);
                    }
                    return true;
                }
                if (map.containsKey(args[0])) {
                    WarpLocation wLoc = (WarpLocation) map.get(args[0]);
                    Location targetLoc = wLoc.getLocation();
                    if (Main.getGroupName().equals(wLoc.groupname) && p.getWorld().getName().equals(wLoc.worldname)) {
                        p.teleport(targetLoc);
                    } else {
                        p.sendMessage("\u00A7cDu musst dich auf dem selben Server befinden, auf dem sich auch dein Warppunkt befindet. " +
                                "(\u00A7b" + args[0] + "\u00A7c befindet sich auf \u00A7b" + wLoc.groupname + "\u00A7c)");

                    }
                } else {
                    p.sendMessage("\u00A7cDu hast keinen Warppunkt mit dem Namen \u00A7b" + args[0] + "\u00A7c!");
                }
                break;
            case "delwarp":
                if (args.length < 1) {
                    //sendInstructions
                    return true;
                }
                if (MySQL.executeUpdate("DELETE FROM `Warps` WHERE biomiaPlayerID = " + Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + " AND name = '" + args[0] + "'", MySQL.Databases.biomia_db)) {
                    p.sendMessage("\u00A7cWarp \u00A7b" + args[0] + " \u00A7cwurde gel\u00f6scht.");
                } else {
                    p.sendMessage("\u00A7cWarp \u00A7b" + args[0] + " \u00A7cwurde nicht gel\u00f6scht, denn er wurde nicht gefunden.");
                }
            default:
                break;
        }
        return true;
    }

    private void sendWarpInstructions(Player p) {
        p.sendMessage("\u00A77/\u00A7cwarp \u00A77<\u00A7cZiel\u00A77> \u00A7bum zu warpen");
        p.sendMessage("\u00A77/\u00A7csetwarp \u00A77<\u00A7cName\u00A77> \u00A7bum Warps zu speichern");
        p.sendMessage("\u00A77/\u00A7cdelwarp \u00A77<\u00A7cName\u00A77> \u00A7bum Warps zu l\u00f6schen");
    }

    private void sendWarpList(Player p, HashMap<String, WarpLocation> map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            p.sendMessage("\u00A77-\u00A7b" + pair.getKey() + " " + ((WarpLocation) pair.getValue()).toString());
            it.remove();
        }
    }

    private HashMap<String, WarpLocation> getAllLocations(Player p) {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        WarpLocation wloc = null;
        HashMap<String, WarpLocation> locations = new HashMap<>();
        try {
            PreparedStatement ps = con.prepareStatement(
                    "Select x,y,z,yaw,pitch,groupname,worldname,name from Warps where biomiaPlayerID = ?");
            ps.setString(1, Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + "");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                float yaw = (float) rs.getDouble("yaw");
                float pitch = (float) rs.getDouble("pitch");
                String groupname = rs.getString("groupname");
                String worldname = rs.getString("worldname");
                String name = rs.getString("name");

                wloc = new WarpLocation(x, y, z, yaw, pitch, groupname, worldname, name);
                locations.put(name, wloc);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

}

class WarpLocation {
    public double x, y, z;
    public float yaw, pitch;
    public String groupname, worldname, name;

    public WarpLocation(double x0, double y0, double z0, float yaw0, float pitch0, String groupname0, String worldname0, String name0) {
        x = x0;
        y = y0;
        z = z0;
        yaw = yaw0;
        pitch = pitch0;
        groupname = groupname0;
        worldname = worldname0;
        name = name0;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(worldname), x, y, z, yaw, pitch);
    }

    public String toString() {
        return "\u00A77[x=\u00A7c" + x + "\u00A77, y=\u00A7c" + y + "\u00A77, z=\u00A7c" + z + "\u00A77 : \u00A7c" + groupname + "\u00A77]";
    }
}
