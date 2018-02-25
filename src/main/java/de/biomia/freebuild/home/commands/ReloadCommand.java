package de.biomia.freebuild.home.commands;

import de.biomia.api.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
	public ReloadCommand() {
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Main.getPlugin().reloadConfig();
		return true;
	}
}
