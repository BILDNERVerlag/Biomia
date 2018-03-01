package de.biomia.server.lobby.listener;

import de.biomia.tools.RankManager;
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
        String group = RankManager.getPrefix(p);
        if (p.hasPermission("biomia.coloredchat")) {
            msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());
            format = group + p.getName() + "\u00A77: \u00A7f" + msg;
        } else {
            format = group + p.getName() + "\u00A77: \u00A7f" + msg;
        }
        e.setFormat(format);
    }
}