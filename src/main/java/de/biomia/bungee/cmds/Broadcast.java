package de.biomia.bungee.cmds;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Broadcast extends Command {

    public Broadcast(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("biomia.broadcast")) {

            String s = String.join(" ", args);
            s = ChatColor.translateAlternateColorCodes('&', s);
            BungeeCord.getInstance().broadcast(new TextComponent("§8[§5Broadcast§8]§r " + s));
        }
    }

}
