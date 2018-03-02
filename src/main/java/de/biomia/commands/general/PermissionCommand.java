package de.biomia.commands.general;

import de.biomia.commands.BiomiaCommand;
import de.biomia.messages.BiomiaMessages;
import org.bukkit.command.CommandSender;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionCommand extends BiomiaCommand {

    public PermissionCommand() {
        super("permission");
    }

    private static void removePermission(String p, String permission) {
        PermissionUser user = PermissionsEx.getUser(p);
        user.removePermission(permission);
    }

    private static void addPermission(String p, String permission) {
        PermissionUser user = PermissionsEx.getUser(p);
        user.addPermission(permission);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("biomia.setpermission")) {
            if (args.length >= 3) {

                if (args[0].equalsIgnoreCase("add")) {
                    addPermission(args[1], args[2]);
                    sender.sendMessage(
                            "\u00A76Der Spieler \u00A7a" + args[1] + " \u00A76hat jetzt die Permission\u00A7a " + args[2] + "\u00A76!");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    removePermission(args[1], args[2]);
                    sender.sendMessage(
                            "\u00A76Dem Spieler \u00A7a" + args[1] + " \u00A76wurde die Permission\u00A7a " + args[2] + " \u00A76entzogen!");
                } else {
                    sender.sendMessage("\u00A7c/permission <add|remove> <Spieler> <Permission>");
                }
            } else {
                sender.sendMessage("\u00A7c/permission <add|remove> <Spieler> <Permission>");
            }
        } else {
            sender.sendMessage(BiomiaMessages.NO_PERM);

        }
        return true;
    }
}
