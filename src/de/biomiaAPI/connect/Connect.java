package de.biomiaAPI.connect;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import at.TimoCraft.TimoCloud.api.objects.ServerObject;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.mysql.MySQL;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

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
					"Select code from CodesFuerRaenge where rangEingeloestFuerPlayeruuid = '" + p.getUniqueId() + "'", MySQL.Databases.biomia_db);
			if (group.contains("#1")) {
				if (!Objects.requireNonNull(list).contains("krs522tpr8a")) {
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
		ArrayList<ServerObject> servers = new ArrayList<>(Main.getUniversalTimoapi().getGroup(group).getServers());

		if (servers.size() > 1) {
			ServerObject random = servers.get(new Random().nextInt(servers.size() - 1));
			connect(p, random.getName());
		} else if (servers.size() == 1) {
			ServerObject random = servers.get(0);
			connect(p, random.getName());
		}
	}

	private static ArrayList<String> executeQuery(String cmd, MySQL.Databases database) {
		Connection con = MySQL.Connect(database);
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

	public static void getOnlinePlayers() {

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);

		try {
			out.writeUTF("PlayerList");
			out.writeUTF("ALL");
		} catch (IOException ignored) {

		}

		Player player;

		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			player = Bukkit.getOnlinePlayers().iterator().next();
			player.sendPluginMessage(Main.plugin, "RedisBungee", b.toByteArray());
		}
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

		if (!channel.equalsIgnoreCase("RedisBungee") && !channel.equalsIgnoreCase("BiomiaChannel"))
			return;
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();

		if (subchannel.equals("PlayerList")) {
			if (in.readUTF().equals("ALL")) {
				Main.allPlayersOnAllServer.clear();
				String players = in.readUTF();
				String[] stringarray = players.split(",");

                Collections.addAll(Main.allPlayersOnAllServer, stringarray);
			}
		}
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
