package de.biomia.plugin.commands;

import de.biomia.api.msg.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("biomia.heal")) {
            sender.sendMessage(Messages.NO_PERM);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("00A7cNur Spieler k\u00F6nnen sich heilen!");
            return true;
        }

        Player p = (Player) sender;
        p.setHealth(p.getHealthScale());
        sender.sendMessage("00A7dDu wurdest vollst\u00fcndig geheilt!");

        return true;
    }

}
