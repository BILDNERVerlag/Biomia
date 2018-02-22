package de.biomia.lobby.commands;

import de.biomia.lobby.main.LobbyMain;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.msg.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LobbyComands implements CommandExecutor {

	public static boolean pvp = false;

	public static ArrayList<Player> targetarmorstands = new ArrayList<>();

	public static boolean flyall = false;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("lobbysettings")) {
			if (sender.hasPermission("biomia.lobbysettings")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("destroyarmorstands") && (sender instanceof Player)) {
						Player p = (Player) sender;
						if (!targetarmorstands.contains(p)) {
							targetarmorstands.add(p);
							p.sendMessage(Messages.prefix + "§aDu kannst nun ArmorStands auf der Lobby abbauen!");
							return true;
						} else {
							targetarmorstands.remove(p);
							p.sendMessage(
									Messages.prefix + "§cDu kannst nun keine ArmorStands mehr auf der Lobby abbauen!");
							return true;
						}

					}
					if (args[0].equalsIgnoreCase("fly")) {
						for (Player pl : Bukkit.getOnlinePlayers()) {
							if (pl.getWorld().getName().contains("Lobby")) {
								if (!flyall) {
									pl.setAllowFlight(true);
									pl.setFlying(true);
									flyall = true;
									sender.sendMessage(Messages.prefix + "§aEs können nun alle auf der Lobby fliegen!");
									return true;
								} else {
									pl.setAllowFlight(false);
									pl.setFlying(false);
									flyall = false;
									sender.sendMessage(
											Messages.prefix + "§cEs kann nun keiner mehr auf der Lobby fliegen!");
									return true;
								}
							}
							return false;
						}
					}
					return false;
				}
				return false;

			} else {
				sender.sendMessage(Messages.noperm);
				return true;
			}
		}

		return false;
	}

}
