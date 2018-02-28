package de.biomia.plugin.commands;

import de.biomia.api.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("biomia.fly")) {
            sender.sendMessage(Messages.NO_PERM);
            return true;
        }

        if (sender instanceof Player && args.length == 0) {
            // Falls Sender ein Spieler und kein Spielername \u00fcbergeben
            Player p = (Player) sender;

            if (p.getAllowFlight()) {
                p.setAllowFlight(false);
                p.sendMessage("\u00A7cFly Mode deaktiviert.");
            } else {
                p.setAllowFlight(true);
                p.sendMessage("\u00A7cFly Mode aktiviert.");
            }

        } else if (args.length == 1) {
            // Falls Spielername \u00fcbergeben
            Player toSet = Bukkit.getPlayer(args[0]);
            if (toSet == null) {
                sender.sendMessage("\u00A7cDieser Spieler ist nicht online!");
            } else {
                if (toSet.getAllowFlight()) {
                    toSet.setAllowFlight(false);
                    sender.sendMessage("\u00A7cFly Mode f\u00fcr Spieler " + toSet.getName() + " deaktiviert.");
                    toSet.sendMessage("\u00A7cFly Mode f\u00fcr Spieler " + toSet.getName() + " wurde von " + sender.getName()
                            + " deaktiviert.");

                } else {
                    toSet.setAllowFlight(true);
                    sender.sendMessage("\u00A7cFly Mode f\u00fcr Spieler " + toSet.getName() + " aktiviert.");
                    toSet.sendMessage("\u00A7cFly Mode f\u00fcr Spieler " + toSet.getName() + " wurde von " + sender.getName()
                            + " aktiviert.");

                }
            }
        } else {
            // Ansonsten
            sender.sendMessage("\u00A7c/fly [optional:Spielername]");
        }

        return false;
    }

}
