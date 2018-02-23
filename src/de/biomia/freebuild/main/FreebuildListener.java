package de.biomia.freebuild.main;

import at.TimoCraft.TimoCloud.api.objects.ServerObject;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.connect.Connect;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.msg.Scoreboards;
import de.biomiaAPI.pex.Rank;
import de.biomiaAPI.tools.InventorySave;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
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
				p.sendMessage("§7[§5Bio§2mia§7] §6Willkommen auf dem FreebuildServer, " + p.getName() + "!");
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
					servers.addAll(Main.getUniversalTimoapi().getGroup("FarmServer").getServers());

					if (!servers.isEmpty() && servers != null) {
						Connect.connectToRandom(p, "FarmServer");
					}
				} else
					p.sendMessage(
							"§cDas Portal zur Farmwelt ist offenbar noch nicht richtig ausgerichtet! Probier es am besten in ein paar Tagen erneut!");
			} else if ((390 <= z) && (z <= 393)) {
				// ZUR LOBBY
				p.teleport(new Location(Bukkit.getWorld("world"), -198, 64, 391, 120, 0));
				servers.addAll(Main.getUniversalTimoapi().getGroup("Lobby").getServers());

				if (!servers.isEmpty() && servers != null) {
					Connect.connectToRandom(p, "Lobby");
				} else {
					p.sendMessage("§cHuch, da ist offenbar etwas schiefgelaufen! Probiere stattdessen §a/hub§c!");
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
			format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "§7: §f" + msg;
			e.setFormat(format);
		} else {
			format = group + Biomia.getBiomiaPlayer(p).getPlayer().getDisplayName() + "§7: §f" + msg;
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
	
	@EventHandler 
	public void onBlockSet(BlockPlaceEvent e) {
		
	}
}