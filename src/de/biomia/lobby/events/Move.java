package de.biomia.lobby.events;

import at.TimoCraft.TimoCloud.api.objects.ServerObject;
import de.biomiaAPI.connect.Connect;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class Move implements Listener {

	@EventHandler
	private void onMove(PlayerMoveEvent event) {

		ArrayList<ServerObject> servers = new ArrayList<>();
		Player p = event.getPlayer();

		double x = p.getLocation().getX();
		double y = p.getLocation().getY();
		double z = p.getLocation().getZ();

		if (p.getWorld().getName().equals("LobbyBiomia") && (360 <= x) && (x <= 800) && (150 <= z) && (z <= 700)) {
			// BauServer Portal
			if ((559 <= x) && (x <= 562) && (76 <= y) && (y <= 77) && (286 <= z) && (z <= 289)) {
				p.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 551.5, 80, 285.5, -90, 0));

				servers.addAll(Main.getUniversalTimoapi().getGroup("BauServer").getServers());
				if (!servers.isEmpty() && servers != null) {
					Connect.connect(p, "BauServer");
					return;
				}
			}
			// QuestServer Portale
			else if ((446 <= x) && (x <= 447) && (124 <= y) && (y <= 125) && (359 <= z) && (z <= 361)) {
				p.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 480.5, 123, 359.5, 90, 0));

				servers.addAll(Main.getUniversalTimoapi().getGroup("QuestServer").getServers());
				if (!servers.isEmpty() && servers != null) {
					Connect.connectToRandom(p, "QuestServer");
					return;
				}
			} else if ((459 <= x) && (x <= 460) && (124 <= y) && (y <= 125)
					&& (((377 <= z) && (z <= 378)) || ((341 <= z) && (z <= 342)))) {

				p.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 480.5, 123, 359.5, 90, 0));
				servers.addAll(Main.getUniversalTimoapi().getGroup("QuestServer").getServers());

				if (!servers.isEmpty() && servers != null) {
					Connect.connectToRandom(p, "QuestServer");
				}
			}
			// Farm- und FreebuildServer Portale
			else if ((552 <= x) && (x <= 553) && (97 <= y) && (y <= 98)) {
				if (((290 <= z) && (z <= 291))) {
					// Freebuildwelt
					p.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 556.5, 96, 292.5, -90, 0));

					servers.addAll(Main.getUniversalTimoapi().getGroup("FreebuildServer").getServers());
					if (!servers.isEmpty() && servers != null)
						Connect.connect(p, "FreebuildServer");
					else
						p.sendMessage("§cHuch, da ist etwas schiefgelaufen! Probier es in ein paar Minuten erneut!");
					return;
				} else if (((294 <= z) && (z <= 295))) {
					// Farmwelt
					p.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 556.5, 96, 292.5, -90, 0));

					servers.addAll(Main.getUniversalTimoapi().getGroup("FarmServer").getServers());
					// comment entfernen wenn farmwelt fertig
					// if (!servers.isEmpty() && servers != null)
					// Connect.connect(p, "FarmServer");
					// else
					p.sendMessage(
							"§cDas Portal zur Farmwelt ist offenbar noch nicht richtig ausgerichtet! Probier es am besten in einigen Tagen erneut!");
					return;
				}
			}
			// unbenutzer Eventserver-Code
			//
			// else if ((552 <= x) && (x <= 553) && (97 <= y) && (y <= 98)) {
			// if (((290 <= z) && (z <= 291))) {
			// //Eventserver
			// p.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 556.5, 96, 292.5, 90,
			// 0));
			//
			// servers.addAll(LobbyMain.getUniversalTimoapi().getGroup("EventServer").getServers());
			// if (!servers.isEmpty() && servers != null)
			// Connect.connect(p, "EventServer");
			// else
			// p.sendMessage("§cZur Zeit gibt es keine Events!");
			// return;
			// }
			// SignLobby Portale
			else if ((459.5 <= x) && (x <= 460) && (71 <= y) && (y <= 73)) {
				if ((264 <= z) && (z <= 269)) {
					p.teleport(new Location(Bukkit.getWorld("SkywarsSignlobby"), 370.5, 82, 264.5, 70, 0));
				} else if ((252 <= z) && (z <= 257)) {
					p.teleport(new Location(Bukkit.getWorld("BedwarsSignlobby"), 370.5, 82, 264.5, 70, 0));
				}
			}
		} else if (p.getWorld().getName().contains("Signlobby")) {
			if (!((0 <= x) && (x <= 800) && (0 <= z) && (z <= 1024)))
				p.teleport(new Location(p.getWorld(), 370.5, 82, 264.5, 70, 0));
		} else {
			p.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 534.5, 67.5, 193.5));
		}
	}
}
