package de.biomia.bungee.cmds;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
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
            BungeeCord.getInstance().broadcast(new TextComponent("§7[§bBroadcast§7]§r " + s));
        }
    }

}
