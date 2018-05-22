package de.biomia.bungee.cmds;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {

    public BroadcastCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("biomia.broadcast")) {

            String s = String.join(" ", args);
            s = ChatColor.translateAlternateColorCodes('&', s);
            ProxyServer.getInstance().broadcast(new TextComponent("\u00A77[\u00A7bBroadcast\u00A77]\u00A7r " + s));
        }
    }

}
