package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class WarpCommand extends BiomiaCommand {

    private final ArrayList<BiomiaServerType> allowedGroups;

    public WarpCommand(String command) {
        super(command);
        allowedGroups = new ArrayList<>(Arrays.asList(BiomiaServerType.BauServer, BiomiaServerType.Freebuild, BiomiaServerType.FreebuildFarm, BiomiaServerType.Quest, BiomiaServerType.TestQuest));
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        Location ploc = p.getLocation();

        HashMap<String, WarpLocation> playerWarpLocations = getAllLocations(p);
        HashMap<String, WarpLocation> publicWarpLocations = initPublicWarps(p);

        switch (getName().toLowerCase()) {
            case "setwarp":
                if (args.length < 1) {
                    sendWarpInstructions(p);
                    return true;
                }
                args[0] = args[0].toLowerCase();
                int verbleibendeWarps = bp.getPremiumLevel() + 3 - playerWarpLocations.size();
                if (bp.isStaff()) verbleibendeWarps += 2;
                if (bp.isOwnerOrDev()) verbleibendeWarps += 6;

                if (playerWarpLocations.size() >= bp.getPremiumLevel() + 3) {
                    p.sendMessage("\u00A7cDu hast bereits die \u00A7bmaximale \u00A7cAnzahl Warps erreicht.");
                    p.sendMessage("\u00A7cBenutze \u00A77/\u00A7cdelwarp \u00A77<\u00A7cName\u00A77> \u00A7bum Warps zu l\u00f6schen \u00A7coder hol dir einen unserer Premiumränge und unterstütze damit den Server.");
                    return true;
                }
                if (!allowedGroups.contains(Biomia.getServerInstance().getServerType())) {
                    p.sendMessage("\u00A7cEigene Warps sind auf diesem Server (\u00A7b" + Biomia.getServerInstance().getServerType().name() + "\u00A7c) nicht erlaubt.");
                    return true;
                }
                MySQL.executeUpdate("INSERT INTO Warps (`x`, `y`, `z`, `yaw`, `pitch`, `groupname`, `worldname`, `name`, `biomiaPlayerID`) VALUES (" +
                        ploc.getBlockX() + "," +
                        ploc.getBlockY() + "," +
                        ploc.getBlockZ() + "," +
                        ploc.getYaw() + "," +
                        ploc.getPitch() + ",'" +
                        Biomia.getServerInstance().getServerType().name() + "','" +
                        p.getWorld().getName() + "','" +
                        args[0] + "'," +
                        bp.getBiomiaPlayerID() +
                        ")", MySQL.Databases.biomia_db);
                p.sendMessage("\u00A7cDu hast den Warp \u00A7b" + args[0] + "\u00A7c erfolgreich erstellt!");
                verbleibendeWarps--;
                if (verbleibendeWarps <= 0)
                    p.sendMessage("\u00A77Dies war dein letzter verbleibender Warppunkt.");
                else
                    p.sendMessage("\u00A77Verbleibende Warppunkte: " + verbleibendeWarps + " \u00A77(vom Rang abh\u00e4ngig)");
                break;
            case "warp":
                if (args.length < 1) {
                    if (playerWarpLocations.isEmpty()) {
                        sendWarpInstructions(p);
                    } else {
                        p.sendMessage(Messages.PREFIX + "\u00A7cGib einen deiner Warps an!");
                        sendWarpList(p, playerWarpLocations, publicWarpLocations);
                    }
                    return true;
                }
                if (playerWarpLocations.containsKey(args[0]) || publicWarpLocations.containsKey(args[0])) {
                    WarpLocation wLoc = playerWarpLocations.get(args[0]);
                    if (wLoc == null) wLoc = publicWarpLocations.get(args[0]);
                    Location targetLoc = wLoc.getLocation();
                    if (Biomia.getServerInstance().getServerType().name().equals(wLoc.getGroupname()) && p.getWorld().getName().equals(wLoc.getWorldname())) {
                        p.teleport(targetLoc);
                    } else {
                        p.sendMessage(Messages.PREFIX + "\u00A7cDu musst dich auf dem selben Server wie dein Warppunkt befinden. " +
                                "(\u00A7b" + args[0] + "\u00A7c befindet sich auf \u00A7b" + wLoc.getGroupname() + "\u00A7c)");
                        p.sendMessage("Du befindest dich auf " + Biomia.getServerInstance().getServerType().name() + ", Weltname: " + wLoc.getWorldname());
                    }
                } else {
                    p.sendMessage("\u00A7cDu hast keinen Warppunkt mit dem Namen \u00A7b" + args[0] + "\u00A7c!");
                }
                break;
            case "delwarp":
                if (args.length < 1) {
                    p.sendMessage("\u00A77/\u00A7cdelwarp \u00A77<\u00A7cName\u00A77> \u00A7bum Warps zu l\u00f6schen");
                    return true;
                }
                if (playerWarpLocations.containsKey(args[0])) {
                    MySQL.executeUpdate("DELETE FROM `Warps` WHERE biomiaPlayerID = " + Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + " AND name = '" + args[0].toLowerCase() + "'", MySQL.Databases.biomia_db);
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
        p.sendMessage("\u00A77/\u00A7cwarp \u00A77[\u00A7cZiel\u00A77] \u00A7bum zu warpen / Warps anzuzeigen");
        p.sendMessage("\u00A77/\u00A7csetwarp \u00A77<\u00A7cName\u00A77> \u00A7bum Warps zu speichern");
        p.sendMessage("\u00A77/\u00A7cdelwarp \u00A77<\u00A7cName\u00A77> \u00A7bum Warps zu l\u00f6schen");
    }

    private void sendWarpList(Player p, HashMap<String, WarpLocation> playerWarpLocations0, HashMap<String, WarpLocation> publicWarpLocations0) {
        if (!playerWarpLocations0.isEmpty()) {
            p.sendMessage("\u00A7c Deine Warps:");
            Iterator it = playerWarpLocations0.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                TextComponent msg = new TextComponent("\u00A77-\u00A7b" + pair.getKey() + " " + (pair.getValue()).toString());
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + pair.getKey()));
                p.spigot().sendMessage(msg);
                //p.sendMessage("\u00A77-\u00A7b" + pair.getKey() + " " + (pair.getValue()).toString());
                it.remove();
            }
        }
        if (!publicWarpLocations0.isEmpty()) {
            p.sendMessage("\u00A7c Öffentliche Warps:");
            Iterator it2 = publicWarpLocations0.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry pair = (Map.Entry) it2.next();
                TextComponent msg = new TextComponent("\u00A77-\u00A7b" + pair.getKey() + " " + (pair.getValue()).toString());
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + pair.getKey()));
                p.spigot().sendMessage(msg);
                //p.sendMessage("\u00A77-\u00A7b" + pair.getKey() + " " + (pair.getValue()).toString());
                it2.remove();
            }
        }
    }

    private HashMap<String, WarpLocation> initPublicWarps(Player p) {
        HashMap<String, WarpLocation> locations = new HashMap<>();
        switch (Biomia.getServerInstance().getServerType()) {
            case TestQuest:
            case Quest:
                break;
            case Freebuild:
                break;
            case FreebuildFarm:
                break;
            case BauServer:
                break;
            case Duell:
                break;
            case Weltenlabor_1:
                break;
            case TestLobby:
            case Lobby:
                break;
            default:
                return locations;
        }
        locations.put("spawn", new WarpLocation(p.getWorld().getSpawnLocation(), Biomia.getServerInstance().getServerType().name()));
        return locations;
    }

    private HashMap<String, WarpLocation> getAllLocations(Player p) {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        WarpLocation wloc;
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

                wloc = new WarpLocation(x, y, z, yaw, pitch, groupname, worldname);
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
