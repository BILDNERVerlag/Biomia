package de.biomia.plugin.commands;

import de.biomia.api.itemcreator.ItemCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Head implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length >= 1) {
                if (args.length >= 2) {
                    p.getInventory().addItem(ItemCreator.headWithSkin(args[0], args[1]));
                    return true;
                } else {
                    p.getInventory().addItem(ItemCreator.headWithSkin(args[0]));
                    return true;
                }
            } else {
                sender.sendMessage("/head <skinname> (itemname)");
            }

        }
        return true;
    }

}
