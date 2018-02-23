package de.biomia.freebuild.home.commands;

import de.biomia.freebuild.home.configuration.languages.LanguageManager;
import de.biomia.freebuild.home.homes.HomeManager;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class HomeListCommand implements CommandExecutor {
	private final HomeManager homeManager;

	public HomeListCommand(HomeManager manager) {
		homeManager = manager;
	}

	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
		Set<String> homeSet;
		if ((sender instanceof Player) && sender.hasPermission("biomia.adminhomes")) {
			Player player = (Player) sender;
			homeSet = new HashSet();

			if (strings.length != 0) {
				int bpID = BiomiaPlayer.getID(strings[0]);
				if (bpID != -1) {
					homeSet = homeManager.getPlayerHomes(bpID).keySet();
				} else {
					player.sendMessage(LanguageManager.PLAYER_NOT_EXIST);
				}
			} else {
				homeSet = homeManager.getPlayerHomes(Biomia.getBiomiaPlayer(player).getBiomiaPlayerID()).keySet();
			}
			String[] homeString = homeSet.toArray(new String[homeSet.size()]);
			Arrays.sort(homeString);

			String homes = homeListString(homeString);
			if (homes != null) {
				sender.sendMessage(LanguageManager.HOME_LIST_PREFIX + " " + homes);
			} else {
				sender.sendMessage(LanguageManager.NO_HOMES_FOUND);
			}
			return true;
		}

		Map<Integer, Map<String, Location>> homes = homeManager.getHomes();
		for (Map.Entry<Integer, Map<String, Location>> entry : homes.entrySet()) {
			String playerName = BiomiaPlayer.getName(entry.getKey());
			Set<String> playerHomes = ((Map) entry.getValue()).keySet();
			String[] homeStrings = playerHomes.toArray(new String[playerHomes.size()]);
			Arrays.sort(homeStrings);
			String homeList = homeListString(homeStrings);
			if (homeList != null) {
				sender.sendMessage("[" + playerName + "]" + LanguageManager.HOME_LIST_PREFIX + " " + homeList);
			} else {
				sender.sendMessage("[" + playerName + "] " + LanguageManager.NO_HOMES_FOUND);
			}
		}

		return true;
	}

	private String homeListString(String[] playerHomes) {
		int size = playerHomes.length;
		if (size > 0) {
			StringBuilder sb = new StringBuilder();
			if (size > 1) {
				for (String playerHome : playerHomes) {
					sb.append(playerHome).append(", ");
				}
			}
			sb.append(playerHomes[(size - 1)]);

			return sb.toString();
		}
		return null;
	}
}
