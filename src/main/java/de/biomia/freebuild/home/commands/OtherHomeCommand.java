package de.biomia.freebuild.home.commands;

import de.biomia.freebuild.home.Home;
import de.biomia.freebuild.home.homes.HomeManager;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.main.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class OtherHomeCommand implements CommandExecutor {
	private final HomeManager homeManager;

	public OtherHomeCommand(HomeManager manager) {
		homeManager = manager;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
		if ((sender instanceof Player) && sender.hasPermission("biomia.adminhomes")) {
			if (strings.length == 0) {
				return false;
			}
			final Player player = (Player) sender;
			String homeName;
			if (strings.length == 2) {
				homeName = strings[1].toLowerCase();
			} else {
				homeName = "default";
			}
			final String targetName = strings[0].toLowerCase();
			Main.getPlugin().getServer().getScheduler().runTaskAsynchronously(Main.getPlugin(),
					new BukkitRunnable() {
						int bpID;

						public void run() {
							bpID = BiomiaPlayer.getBiomiaPlayerID(targetName);
							if (bpID != -1) {
								Main.getPlugin().getServer().getScheduler().runTask(Main.getPlugin(),
										new BukkitRunnable() {
											public void run() {
												Object location = homeManager.getPlayerHome(bpID, homeName);
												if (location == null) {
													location = homeManager.getPlayerHomeFromFile(bpID, homeName);
												}
												if (location != null) {
													player.teleport((Location)location);
													player.sendMessage(Home.TELEPORT_OTHERHOME
															.replaceAll("%p", targetName));
												} else {
													player.sendMessage(Home.HOME_NOT_FOUND);
												}
											}
										});
							} else {
								player.sendMessage(Home.PLAYER_NOT_EXIST);
							}
						}
					});
		} else {
			sender.sendMessage(Home.PLAYER_COMMAND_ONLY);
		}
		return true;
	}
}
