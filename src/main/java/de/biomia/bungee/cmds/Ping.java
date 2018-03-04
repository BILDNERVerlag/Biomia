package de.biomia.bungee.cmds;

import de.biomia.spigot.messages.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ping extends Command {

    public Ping(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer) sender;
            sender.sendMessage(new TextComponent(Messages.PREFIX + "§cDein Ping liegt bei: §6" + pp.getPing() + "ms"));
        }
    }

}
