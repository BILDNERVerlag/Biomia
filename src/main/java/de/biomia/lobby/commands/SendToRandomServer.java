package de.biomia.lobby.commands;

import de.biomia.api.connect.Connect;
import de.biomia.api.msg.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendToRandomServer implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender.hasPermission("biomia.*")) {

			if (args.length != 0) {
				if (args.length > 1) {
					Connect.connectToRandom(Bukkit.getPlayer(args[1]), args[0]);
				} else if (sender instanceof Player) {
					Connect.connectToRandom((Player) sender, args[0]);
				} else {
					sender.sendMessage("Nutze /randomServerGroup <Gruppe> [Player] oder");
					sender.sendMessage("Nutze /randomServerGroup <Gruppe>");
				}
			} else {
				sender.sendMessage("Nutze /randomServerGroup <Gruppe> [Player] oder");
				sender.sendMessage("Nutze /randomServerGroup <Gruppe>");
			}
		} else {
			sender.sendMessage(Messages.NO_PERM);
		}
		return true;
	}
}
