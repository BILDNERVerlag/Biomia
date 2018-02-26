package de.biomia.plugin.commands;

import de.biomia.api.msg.Messages;
import de.biomia.api.pex.Permission;
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
                            "00A76Der Spieler 00A7a" + args[1] + " 00A76hat jetzt die Permission00A7a " + args[2] + "00A76!");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    Permission.removePermission(args[1], args[2]);
                    sender.sendMessage(
                            "00A76Dem Spieler 00A7a" + args[1] + " 00A76wurde die Permission00A7a " + args[2] + " 00A76entzogen!");
                } else {
                    sender.sendMessage("00A7c/permission <add|remove> <Spieler> <Permission>");
                }
            } else {
                sender.sendMessage("00A7c/permission <add|remove> <Spieler> <Permission>");
            }
        } else {
            sender.sendMessage(Messages.NO_PERM);

        }
        return true;
    }
}
