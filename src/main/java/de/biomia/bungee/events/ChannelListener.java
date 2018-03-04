package de.biomia.bungee.events;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.cmds.Ban;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;

public class ChannelListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) throws IOException {
        if (e.getTag().equals("BiomiaChannel")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));

            String subchannel;
            try {
                subchannel = in.readUTF();
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }

            if (subchannel.equals("Report")) {
                int playerID = in.readInt();
                int reporterID = in.readInt();
                String grund = in.readUTF();

                String playerName = BungeeBiomia.getOfflineBiomiaPlayer(playerID).getName();
                String reporterName = BungeeBiomia.getOfflineBiomiaPlayer(reporterID).getName();

                TextComponent comp = new TextComponent("§dDer Spieler §c" + playerName + " §dwurde wegen §c" + Grund.toText(Grund.valueOf(grund)) + " §dvon §a" + reporterName + " §dreportet!");
                comp.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/gtp " + playerName));

                BungeeCord.getInstance().getPlayers().forEach(each -> {
                    if (each.hasPermission("biomia.reports.cansee") || each.hasPermission("biomia.*")) {
                        each.sendMessage(comp);
                    }
                });
            } else if (subchannel.equals("BanTimeReason")) {

                int biomiaID = in.readInt();
                int idToBan = in.readInt();
                int time = in.readInt();
                String reason = in.readUTF();
                String playerName = BungeeBiomia.getOfflineBiomiaPlayer(biomiaID).getName();


                if (time == -1) {
                    Ban.banPerm(BungeeCord.getInstance().getPlayer(playerName), idToBan, reason);
                } else {
                    Ban.banTemp(BungeeCord.getInstance().getPlayer(playerName), idToBan, time, reason);
                }

            }

        }
    }

    public static void teleport(String from, String to, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF("TeleportToPlayer");
            out.writeUTF(from);
            out.writeUTF(to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(stream, server);
    }

    private static void send(ByteArrayOutputStream stream, ServerInfo server) {
        server.sendData("BiomiaChannel", stream.toByteArray());
    }

    public static void rank(String rank, ProxiedPlayer player) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF("RankUp");
            out.writeUTF(player.getName());
            out.writeUTF(rank);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(stream, player.getServer().getInfo());
    }

    public static void sendBanRequest(OfflineBungeeBiomiaPlayer bp, int IDtoBan) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF("BanReason");
            out.writeInt(bp.getBiomiaPlayerID());
            out.writeInt(IDtoBan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(stream, bp.getProxiedPlayer().getServer().getInfo());
    }
}
