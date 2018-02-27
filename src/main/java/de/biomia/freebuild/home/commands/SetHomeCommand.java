package de.biomia.freebuild.home.commands;

import de.biomia.freebuild.home.Home;
import de.biomia.freebuild.home.homes.HomeManager;
import de.biomia.api.Biomia;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
	private final HomeManager homeManager;

	public SetHomeCommand(HomeManager manager) {
		homeManager = manager;
	}

	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if ((sender instanceof Player)) {
			Player player = (Player) sender;
			String homeName = "default";
			if ((args.length == 1) && (sender.hasPermission("simplehomes.multihomes"))) {
				homeName = args[0].toLowerCase();
			}
			if (homeManager == null) Bukkit.broadcastMessage("test");
			int bpID = Biomia.getBiomiaPlayer(player).getBiomiaPlayerID();
			if ((homeManager.reachedMaxHomes(bpID))
					&& (!homeManager.getPlayerHomes(bpID).containsKey(homeName))) {
				player.sendMessage(Home.HOME_MAX_REACHED);
				return true;
			}
			homeManager.saveHome(player, homeName);
			player.sendMessage(Home.HOME_SET);
			return true;
		}
		sender.sendMessage(Home.PLAYER_COMMAND_ONLY);

		return false;
	}
}