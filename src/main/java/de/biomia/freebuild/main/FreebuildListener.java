package de.biomia.freebuild.main;

import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.connect.Connect;
import de.biomia.api.main.Main;
import de.biomia.api.msg.Scoreboards;
import de.biomia.api.pex.Rank;
import de.biomia.api.tools.InventorySave;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class FreebuildListener implements Listener {

	@EventHandler
	public static void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
		new BukkitRunnable() {

			@Override
			public void run() {
				InventorySave.setInventory(p, "FreebuildServer");
				p.sendMessage("00A77[00A75Bio00A72mia00A77] 00A76Willkommen auf dem FreebuildServer, " + p.getName() + "!");
			}

		}.runTaskLater(Main.getPlugin(), 20);

		if (!bp.isStaff())
			p.setGameMode(GameMode.SURVIVAL);
		bp.setBuild(true);
		Scoreboards.setTabList(p);
	}

	@EventHandler
	public static void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		InventorySave.saveInventory(p, "FreebuildServer");
	}

	@EventHandler
	private void onMove(PlayerMoveEvent event) {
		ArrayList<ServerObject> servers = new ArrayList<>();
		Player p = event.getPlayer();

		double x = p.getLocation().getX();
		double y = p.getLocation().getY();
		double z = p.getLocation().getZ();

		if ((-194 <= x) && (x <= -191) && (64 <= y) && (y <= 67)) {
			if ((384 <= z) && (z <= 387)) {
				// ZUM FARMSERVER
				p.teleport(new Location(Bukkit.getWorld("world"), -198, 64, 385, 90, 0));
				if (Biomia.getBiomiaPlayer(p).isStaff()) {
					servers.addAll(Main.getUniversalTimoapi().getServerGroup("FarmServer").getServers());

					if (!servers.isEmpty() && servers != null) {
						Connect.connectToRandom(p, "FarmServer");
					}
				} else
					p.sendMessage(
							"00A7cDas Portal zur Farmwelt ist offenbar noch nicht richtig ausgerichtet! Probier es am besten in ein paar Tagen erneut!");
			} else if ((390 <= z) && (z <= 393)) {
				// ZUR LOBBY
				p.teleport(new Location(Bukkit.getWorld("world"), -198, 64, 391, 120, 0));
				servers.addAll(Main.getUniversalTimoapi().getServerGroup("Lobby").getServers());

				if (!servers.isEmpty() && servers != null) {
					Connect.connectToRandom(p, "Lobby");
				} else {
					p.sendMessage("00A7cHuch, da ist offenbar etwas schiefgelaufen! Probiere stattdessen 00A7a/hub00A7c!");
				}
			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		String msg = e.getMessage();
		String format;
		String group = Rank.getPrefix(p);

		if (p.hasPermission("biomia.coloredchat")) {
			msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());
			format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "00A77: 00A7f" + msg;
			e.setFormat(format);
		} else {
			format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "00A77: 00A7f" + msg;
			e.setFormat(format);
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		EntityType type = e.getEntityType();
		int spawnX = e.getLocation().getBlockX();
		int spawnZ = e.getLocation().getBlockZ();
		if (spawnX > -434 && spawnX < -102 && spawnZ > 80 && spawnZ < 547) {
			if (type.equals(EntityType.ZOMBIE) || type.equals(EntityType.ZOMBIE_VILLAGER)
					|| type.equals(EntityType.CREEPER) || type.equals(EntityType.WITCH)
					|| type.equals(EntityType.SKELETON) || type.equals(EntityType.SPIDER)
					|| type.equals(EntityType.ENDERMAN)) {
				e.setCancelled(true);
			}
		}
	}

}