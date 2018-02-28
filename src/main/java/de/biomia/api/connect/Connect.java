package de.biomia.api.connect;

import cloud.timo.TimoCloud.api.objects.ServerObject;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.biomia.api.Biomia;
import de.biomia.api.main.Main;
import de.biomia.api.mysql.MySQL;
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

public class Connect implements PluginMessageListener {

    public static void connect(Player p, String servername) {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(servername);
        } catch (IOException ignored) {

        }
        p.sendPluginMessage(Main.plugin, "BungeeCord", b.toByteArray());

    }

    // public static void connectToRandom(Player p, String group) {
    // ArrayList<ServerObject> servers = new
    // ArrayList<>(QuestMain.getUniversalTimoapi().getGroup(group).getServers());
    // ServerObject random = servers.get(new Random().nextInt(servers.size() - 1));
    // connect(p, random.getName());
    // }

    public static void connectToRandom(Player p, String group) {
        if (group.contains("Weltenlabor")) {
            ArrayList<String> list = executeQuery(
                    "Select code from CodesFuerRaenge where rangEingeloestFuerPlayeruuid = '" + p.getUniqueId() + "'");
            if (list != null)
                if (group.contains("#1")) {
                    if (!list.contains("krs522tpr8a")) {
                        p.sendMessage(
                                "\u00A7cNur Spieler die das entsprechende Buch besitzen k\u00A7nnen der jeweiligen Welt beitreten");
                        TextComponent text = new TextComponent("\u00A78>\u00A75F\u00fcr mehr Infos hier klicken!\u00A78<");
                        text.setClickEvent(new ClickEvent(Action.OPEN_URL,
                                "https://biomia.bildnerverlag.de/forum/topic/id/18-code-eingabe"));
                        p.spigot().sendMessage(text);
                        return;
                    }
                }
        }
        List<ServerObject> servers = Main.getUniversalTimoapi().getServerGroup(group).getServers();

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

    public static void teleport(Player p, String zielName) {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("TeleportToPlayer");
            out.writeUTF(zielName);
        } catch (IOException ignored) {
        }

        Player player;

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            player = Bukkit.getOnlinePlayers().iterator().next();
            player.sendPluginMessage(Main.plugin, "BiomiaChannel", b.toByteArray());
        }

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
                from.sendMessage("\u00A7aDu wurdest zu \u00A76" + to.getName() + " \u00A7ateleportiert!");
            }

        }

        if (subchannel.equals("AddCoins")) {
            int coins = Integer.valueOf(in.readUTF());
            Biomia.getBiomiaPlayer(Bukkit.getPlayer(in.readUTF())).addCoins(coins, false);
        }
    }

}
