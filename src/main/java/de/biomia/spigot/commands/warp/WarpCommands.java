package de.biomia.spigot.commands.warp;

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

public class WarpCommands extends BiomiaCommand {

    public WarpCommands(String command) {
        super(command);
        allowedGroups = new ArrayList<String>(Arrays.asList("Lobby", "BauServer", "FreebuildServer", "FarmServer", "QuestServer"));
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
                    p.sendMessage("§cDu hast bereits die §bmaximale §cAnzahl Warps erreicht.");
                    p.sendMessage("§cBenutze §7/§cdelwarp §7<§cName§7> §bum Warps zu löschen.");
                    return true;
                }
                if (!allowedGroups.contains(Main.getGroupName())) {
                    p.sendMessage("§cWarps sind auf diesem Server nicht erlaubt. Sorry!");
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
                p.sendMessage("§cDu hast den Warp §b" + args[0] + "§c erfolgreich erstellt!");
                map = getAllLocations(p);
                int verbleibendeWarps = RankManager.getPremiumLevel((p.getName())) + 3 - map.size();
                if (verbleibendeWarps <= 0)
                    p.sendMessage("§7Dies war dein letzter verbleibender Warppunkt.");
                else p.sendMessage("§7Verbleibende Warppunkte: " + verbleibendeWarps + " §7(vom Rang abhängig)");
                break;
            case "warp":
                if (args.length < 1) {
                    if (map.isEmpty()) {
                        sendWarpInstructions(p);
                    } else {
                        p.sendMessage("§cGib einen deiner Warps an! §bDu hast folgende:");
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
                        p.sendMessage("§cDu musst dich auf dem selben Server befinden, auf dem sich auch dein Warppunkt befindet. " +
                                "(§b" + args[0] + "§c befindet sich auf §b" + wLoc.groupname + "§c)");

                    }
                } else {
                    p.sendMessage("§cDu hast keinen Warppunkt mit dem Namen §b" + args[0] + "§c!");
                }
                break;
            case "delwarp":
                if (args.length < 1) {
                    //sendInstructions
                    return true;
                }
                if (MySQL.executeUpdate("DELETE FROM `Warps` WHERE biomiaPlayerID = " + Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + " AND name = '" + args[0] + "'", MySQL.Databases.biomia_db)) {
                    p.sendMessage("§cWarp §b" + args[0] + " §cwurde gelöscht.");
                } else {
                    p.sendMessage("§cWarp §b" + args[0] + " §cwurde nicht gelöscht, denn er wurde nicht gefunden.");
                }
            default:
                break;
        }
        return true;
    }

    private void sendWarpInstructions(Player p) {
        p.sendMessage("§7/§cwarp §7<§cZiel§7> §bum dich zu warpen");
        p.sendMessage("§7/§csetwarp §7<§cName§7> §bum Warps zu speichern");
        p.sendMessage("§7/§cdelwarp §7<§cName§7> §bum Warps zu löschen");
    }

    private void sendWarpList(Player p, HashMap<String, WarpLocation> map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            p.sendMessage("§7-§b" + pair.getKey() + " " + ((WarpLocation) pair.getValue()).toString());
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
        return "§7[x=§c" + x + "§7, y=§c" + y + "§7, z=§c" + z + "§7 : §c" + groupname + "§7]";
    }
}
