package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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

    private final ArrayList<BiomiaServerType> allowedGroups;

    public WarpCommand(String command) {
        super(command);
        allowedGroups = new ArrayList<>(Arrays.asList(BiomiaServerType.BauServer,
                BiomiaServerType.Freebuild, BiomiaServerType.FreebuildFarm, BiomiaServerType.Quest, BiomiaServerType.TestQuest));
    }

    public void onCommand(CommandSender sender, String label, String[] args) {
        Player p = (Player) sender;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        Location ploc = p.getLocation();

        HashMap<String, WarpLocation> playerWarpLocations = getAllLocations(p);
        HashMap<String, WarpLocation> publicWarpLocations = initPublicWarps(p);

        switch (getName().toLowerCase()) {
            case "setwarp":
                if (args.length < 1) {
                    sendWarpInstructions(p);
                    return;
                }
                args[0] = args[0].toLowerCase();
                if (args[0].equals("spawn")) {
                    p.sendMessage("§cDu darfst deinen Warp nicht §b'spawn' §cnennen.");
                    return;
                }
                int verbleibendeWarps = bp.getPremiumLevel() + 3 - playerWarpLocations.size();
                if (bp.isStaff())
                    verbleibendeWarps += 2;
                if (bp.isOwnerOrDev())
                    verbleibendeWarps += 6;

                if (verbleibendeWarps <= 0) {
                    p.sendMessage("§cDu hast bereits die §bmaximale §cAnzahl Warps erreicht.");
                    p.sendMessage("§cBenutze §7/§cdelwarp §7<§cName§7> §bum Warps zu löschen §coder hol dir einen unserer Premiumränge und unterstütze damit den Server.");
                    return;
                }
                if (!allowedGroups.contains(Biomia.getServerInstance().getServerType())) {
                    p.sendMessage("§cEigene Warps sind auf diesem Server (§b" + Biomia.getServerInstance().getServerType().name() + "§c) nicht erlaubt.");
                    return;
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
                p.sendMessage("§cDu hast den Warp §b" + args[0] + "§c erfolgreich erstellt!");
                verbleibendeWarps--;
                if (verbleibendeWarps <= 0)
                    p.sendMessage("§7Dies war dein letzter verbleibender Warppunkt.");
                else
                    p.sendMessage("§7Verbleibende Warppunkte: " + verbleibendeWarps + " §7(vom Rang abhängig)");
                break;
            case "warp":
                if (!allowedGroups.contains(Biomia.getServerInstance().getServerType())) {
                    p.sendMessage("§cWarps sind auf diesem Server (§b" + Biomia.getServerInstance().getServerType().name() + "§c) nicht erlaubt.");
                    return;
                }
                if (args.length < 1) {
                    if (playerWarpLocations.isEmpty()) {
                        sendWarpInstructions(p);
                    } else {
                        p.sendMessage(Messages.PREFIX + "§cGib einen deiner Warps an!");
                        sendWarpList(p, playerWarpLocations, publicWarpLocations);
                    }
                    return;
                }
                if (playerWarpLocations.containsKey(args[0]) || publicWarpLocations.containsKey(args[0])) {
                    WarpLocation wLoc = playerWarpLocations.get(args[0]);
                    if (wLoc == null)
                        wLoc = publicWarpLocations.get(args[0]);
                    Location targetLoc = wLoc.getLocation();
                    if (Biomia.getServerInstance().getServerType().name().equals(wLoc.getGroupname()) && p.getWorld().getName().equals(wLoc.getWorldname())) {
                        p.teleport(targetLoc);
                    } else {
                        p.sendMessage(Messages.PREFIX + "§cDu musst dich auf dem selben Server wie dein Warppunkt befinden. " +
                                "(§b" + args[0] + "§c befindet sich auf §b" + wLoc.getGroupname() + "§c)");
                        p.sendMessage("Du befindest dich auf " + Biomia.getServerInstance().getServerType().name() + ", Weltname: " + wLoc.getWorldname());
                    }
                } else {
                    p.sendMessage("§cDu hast keinen Warppunkt mit dem Namen §b" + args[0] + "§c!");
                }
                break;
            case "delwarp":
                if (args.length < 1) {
                    p.sendMessage("§7/§cdelwarp §7<§cName§7> §bum Warps zu löschen");
                    return;
                }
                if (playerWarpLocations.containsKey(args[0])) {
                    MySQL.executeUpdate("DELETE FROM `Warps` WHERE biomiaPlayerID = " + Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + " AND name = '" + args[0].toLowerCase() + "'", MySQL.Databases.biomia_db);
                    p.sendMessage("§cWarp §b" + args[0] + " §cwurde gelöscht.");
                } else {
                    p.sendMessage("§cWarp §b" + args[0] + " §cwurde nicht gelöscht, denn er wurde nicht gefunden.");
                }
            default:
                break;
        }
    }

    private void sendWarpInstructions(Player p) {
        p.sendMessage("§7/§cwarp §7[§cZiel§7] §bum zu warpen / Warps anzuzeigen");
        p.sendMessage("§7/§csetwarp §7<§cName§7> §bum Warps zu speichern");
        p.sendMessage("§7/§cdelwarp §7<§cName§7> §bum Warps zu löschen");
    }

    private void sendWarpList(Player p, HashMap<String, WarpLocation> playerWarpLocations0, HashMap<String, WarpLocation> publicWarpLocations0) {
        if (!playerWarpLocations0.isEmpty()) {
            p.sendMessage("§c Deine Warps:");
            iterate(p, playerWarpLocations0.entrySet().iterator());
        }
        if (!publicWarpLocations0.isEmpty()) {
            p.sendMessage("§cÖffentliche Warps:");
            iterate(p, publicWarpLocations0.entrySet().iterator());
        }
    }

    private void iterate(Player p, Iterator it2) {
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            TextComponent msg = new TextComponent("§7-§b" + pair.getKey() + " " + (pair.getValue()).toString());
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + pair.getKey()));
            p.spigot().sendMessage(msg);
        }
    }

    private HashMap<String, WarpLocation> initPublicWarps(Player p) {
        HashMap<String, WarpLocation> locations = new HashMap<>();
        switch (Biomia.getServerInstance().getServerType()) {
            case TestQuest:
            case Quest:
            case Freebuild:
            case FreebuildFarm:
            case BauServer:
            case Weltenlabor_1:
                break;
            default:
                return locations;
        }
        if (p.getWorld().getName().contains("nether") || p.getWorld().getName().contains("end")) {
            p.sendMessage(Messages.PREFIX + "§cWarps sind im Nether und im End nicht erlaubt.");
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
                    "SELECT x,y,z,yaw,pitch,groupname,worldname,name FROM Warps WHERE biomiaPlayerID = ?");
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

    private class WarpLocation {

        private final String groupname;
        private final Location loc;

        WarpLocation(double x, double y, double z, float yaw, float pitch, String groupname, String worldname) {
            loc = new Location(Bukkit.getWorld(worldname), x, y, z, yaw, pitch);
            this.groupname = groupname;
        }

        WarpLocation(Location loc0, String groupname) {
            loc = loc0;
            this.groupname = groupname;
        }

        Location getLocation() {
            return loc;
        }

        public String toString() {
            return loc.serialize().toString();
        }

        String getWorldname() {
            return loc.getWorld().getName();
        }

        String getGroupname() {
            return groupname;
        }

    }
}

