package de.biomia.lobby.events;

import de.biomiaAPI.pex.Rank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		String msg = e.getMessage();
		String format;
		String group = Rank.getPrefix(p);
		msg = msg.replaceAll("%", "prozent");

		if (p.hasPermission("biomia.coloredchat")) {
			msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());
			format = group + p.getName() + "§7: §f" + msg;
			e.setFormat(format);
		} else {
			format = group + p.getName() + "§7: §f" + msg;
			e.setFormat(format);
		}
	}
}