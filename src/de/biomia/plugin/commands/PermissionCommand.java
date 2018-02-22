package de.biomia.plugin.commands;

import de.biomiaAPI.msg.Messages;
import de.biomiaAPI.pex.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PermissionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("biomia.setpermission")) {
            if (args.length >= 3) {

                if (args[0].equalsIgnoreCase("add")) {
                    Permission.addPermission(args[1], args[2]);
                    sender.sendMessage(
                            "§6Der Spieler §a" + args[1] + " §6hat jetzt die Permission§a " + args[2] + "§6!");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    Permission.removePermission(args[1], args[2]);
                    sender.sendMessage(
                            "§6Dem Spieler §a" + args[1] + " §6wurde die Permission§a " + args[2] + " §6entzogen!");
                } else {
                    sender.sendMessage("§c/permission <add|remove> <Spieler> <Permission>");
                }
            } else {
                sender.sendMessage("§c/permission <add|remove> <Spieler> <Permission>");
            }
        } else {
            sender.sendMessage(Messages.noperm);

        }
        return true;
    }
}
