package de.biomia.plugin.commands;

import de.biomia.api.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("biomia.eat")) {
            sender.sendMessage(Messages.NO_PERM);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("\u00A7cNur Spieler k\u00F6nnen essen!");
            return true;
        }

        Player p = (Player) sender;

        p.setFoodLevel(20);
        return true;
    }

}
