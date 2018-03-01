package de.biomia.general.commands;

import de.biomia.api.messages.Messages;
import de.biomia.api.pex.Permission;
import org.bukkit.command.CommandSender;

public class PermissionCommand extends BiomiaCommand {

    public PermissionCommand() {
        super("permission");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("biomia.setpermission")) {
            if (args.length >= 3) {

                if (args[0].equalsIgnoreCase("add")) {
                    Permission.addPermission(args[1], args[2]);
                    sender.sendMessage(
                            "\u00A76Der Spieler \u00A7a" + args[1] + " \u00A76hat jetzt die Permission\u00A7a " + args[2] + "\u00A76!");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    Permission.removePermission(args[1], args[2]);
                    sender.sendMessage(
                            "\u00A76Dem Spieler \u00A7a" + args[1] + " \u00A76wurde die Permission\u00A7a " + args[2] + " \u00A76entzogen!");
                } else {
                    sender.sendMessage("\u00A7c/permission <add|remove> <Spieler> <Permission>");
                }
            } else {
                sender.sendMessage("\u00A7c/permission <add|remove> <Spieler> <Permission>");
            }
        } else {
            sender.sendMessage(Messages.NO_PERM);

        }
        return true;
    }
}
