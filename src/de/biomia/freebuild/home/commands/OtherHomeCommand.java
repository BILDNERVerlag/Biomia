package de.biomia.freebuild.home.commands;

import de.biomia.freebuild.home.configuration.languages.LanguageManager;
import de.biomia.freebuild.home.homes.HomeManager;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.tools.UUIDFetcher;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

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
						UUID targetUUID;

						public void run() {
							targetUUID = UUIDFetcher.getUUID(targetName);
							if (targetUUID != null) {
								Main.getPlugin().getServer().getScheduler().runTask(Main.getPlugin(),
										new BukkitRunnable() {
											public void run() {
												Object location = homeManager.getPlayerHome(targetUUID, homeName);
												if (location == null) {
													location = homeManager.getPlayerHomeFromFile(targetUUID, homeName);
												}
												if (location != null) {
													player.teleport((Location)location);
													player.sendMessage(LanguageManager.TELEPORT_OTHERHOME
															.replaceAll("%p", targetName));
												} else {
													player.sendMessage(LanguageManager.HOME_NOT_FOUND);
												}
											}
										});
							} else {
								player.sendMessage(LanguageManager.PLAYER_NOT_EXIST);
							}
						}
					});
		} else {
			sender.sendMessage(LanguageManager.PLAYER_COMMAND_ONLY);
		}
		return true;
	}
}
