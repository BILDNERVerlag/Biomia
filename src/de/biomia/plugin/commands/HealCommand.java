package de.biomia.plugin.commands;

import de.biomiaAPI.msg.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("biomia.heal")) {
            sender.sendMessage(Messages.noperm);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cNur Spieler können sich heilen!");
            return true;
        }

        Player p = (Player) sender;
        p.setHealth(p.getHealthScale());
        sender.sendMessage("§dDu wurdest vollständig geheilt!");

        return true;
    }

}
