package de.biomia.plugin.commands;

import de.biomiaAPI.msg.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("biomia.fly")) {
            sender.sendMessage(Messages.noperm);
            return true;
        }

        if (sender instanceof Player && args.length == 0) {
            // Falls Sender ein Spieler und kein Spielername übergeben
            Player p = (Player) sender;

            if (p.getAllowFlight()) {
                p.setAllowFlight(false);
                p.sendMessage("§cFly Mode deaktiviert.");
            } else {
                p.setAllowFlight(true);
                p.sendMessage("§cFly Mode aktiviert.");
            }

        } else if (args.length == 1) {
            // Falls Spielername übergeben
            Player toSet = Bukkit.getPlayer(args[0]);
            if (toSet == null) {
                sender.sendMessage("§cDieser Spieler ist nicht online!");
            } else {
                if (toSet.getAllowFlight()) {
                    toSet.setAllowFlight(false);
                    sender.sendMessage("§cFly Mode für Spieler " + toSet.getName() + " deaktiviert.");
                    toSet.sendMessage("§cFly Mode für Spieler " + toSet.getName() + " wurde von " + sender.getName()
                            + " deaktiviert.");

                } else {
                    toSet.setAllowFlight(true);
                    sender.sendMessage("§cFly Mode für Spieler " + toSet.getName() + " aktiviert.");
                    toSet.sendMessage("§cFly Mode für Spieler " + toSet.getName() + " wurde von " + sender.getName()
                            + " aktiviert.");

                }
            }
        } else {
            // Ansonsten
            sender.sendMessage("§c/fly [optional:Spielername]");
        }

        return false;
    }

}
