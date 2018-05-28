package de.biomia.spigot.tools;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerToServerConnector implements PluginMessageListener {

    public static void connect(Player p, String servername) {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(servername);
        } catch (IOException ignored) {

        }
        p.sendPluginMessage(Main.getPlugin(), "BungeeCord", b.toByteArray());
    }

    public static void connectToRandom(Player p, BiomiaServerType type) {

        if (type.name().contains("Weltenlabor")) {
            ArrayList<String> list = executeQuery(
                    String.format("Select code from CodesFuerRaenge where rangEingeloestFuerPlayeruuid = '%s'", p.getUniqueId()));
            if (list != null)
                if (type.name().contains("1")) {
                    if (!list.contains("krs522tpr8a")) {
                        p.sendMessage(String.format("%sNur Spieler die das entsprechende Buch besitzen k00f6nnen der jeweiligen Welt beitreten", Messages.COLOR_MAIN));
                        TextComponent text = new TextComponent(String.format("%s>%sF00fcr mehr Infos hier klicken!%s<", Messages.COLOR_AUX, Messages.COLOR_SUB, Messages.COLOR_AUX));
                        text.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://biomia.bildnerverlag.de/forum/topic/id/18-code-eingabe"));
                        p.spigot().sendMessage(text);
                        return;
                    }
                }
        }

        List<ServerObject> servers = TimoCloudAPI.getUniversalAPI().getServerGroup(type.name()).getServers();

        if (servers.size() > 1) {
            ServerObject random = servers.get(new Random().nextInt(servers.size() - 1));
            connect(p, random.getName());
        } else if (servers.size() == 1) {
            ServerObject random = servers.get(0);
            connect(p, random.getName());
        }
    }

    private static ArrayList<String> executeQuery(String cmd) {
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        if (con != null) {
            try {
                PreparedStatement sql = con.prepareStatement(cmd);
                ResultSet rs = sql.executeQuery();
                ArrayList<String> s = new ArrayList<>();
                while (rs.next()) {
                    s.add(rs.getString("code"));
                }
                rs.close();
                sql.close();
                return s;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if (!channel.equalsIgnoreCase("BiomiaChannel"))
            return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("TeleportToPlayer")) {

            Player from = Bukkit.getPlayer(in.readUTF());
            Player to = Bukkit.getPlayer(in.readUTF());
            if (to != null && from != null) {
                from.teleport(to);
                from.sendMessage(String.format("%sDu wurdest zu %s%s %steleportiert!", Messages.COLOR_MAIN, Messages.COLOR_SUB, to.getName(), Messages.COLOR_MAIN));
            }
        }
    }
}
