package de.biomia.freebuild.home.commands;

import de.biomia.freebuild.home.Home;
import de.biomia.freebuild.home.homes.HomeManager;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
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
		HashSet<?> homeSet;
		if ((sender instanceof Player) && sender.hasPermission("biomia.adminhomes")) {
			Player player = (Player) sender;
			homeSet = new HashSet();

			if (strings.length != 0) {
				int bpID = BiomiaPlayer.getID(strings[0]);
				if (bpID != -1) {
					homeSet = (HashSet<?>) homeManager.getPlayerHomes(bpID).keySet();
				} else {
					player.sendMessage(Home.PLAYER_NOT_EXIST);
				}
			} else {
				homeSet = (HashSet<?>) homeManager.getPlayerHomes(Biomia.getBiomiaPlayer(player).getBiomiaPlayerID()).keySet();
			}
			@SuppressWarnings("SuspiciousToArrayCall") String[] homeString = homeSet.toArray(new String[homeSet.size()]);
			Arrays.sort(homeString);

			String homes = homeListString(homeString);
			if (homes != null) {
				sender.sendMessage(Home.HOME_LIST_PREFIX + " " + homes);
			} else {
				sender.sendMessage(Home.NO_HOMES_FOUND);
			}
			return true;
		}

		HashMap<Integer, HashMap<String, Location>> homes = homeManager.getHomes();
		for (Map.Entry<Integer, HashMap<String, Location>> entry : homes.entrySet()) {
			String playerName = BiomiaPlayer.getName(entry.getKey());
			HashSet<?> playerHomes = (HashSet<?>) ((Map) entry.getValue()).keySet();
			@SuppressWarnings("SuspiciousToArrayCall") String[] homeStrings = playerHomes.toArray(new String[playerHomes.size()]);
			Arrays.sort(homeStrings);
			String homeList = homeListString(homeStrings);
			if (homeList != null) {
				sender.sendMessage("[" + playerName + "]" + Home.HOME_LIST_PREFIX + " " + homeList);
			} else {
				sender.sendMessage("[" + playerName + "] " + Home.NO_HOMES_FOUND);
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
