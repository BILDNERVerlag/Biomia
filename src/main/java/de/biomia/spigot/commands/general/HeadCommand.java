package de.biomia.spigot.commands.general;

import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HeadCommand extends BiomiaCommand {

    public HeadCommand() {
        super("head");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length >= 1) {
                if (args.length >= 2) {
                    p.getInventory().addItem(ItemCreator.headWithSkin(args[0], args[1]));
                } else {
                    p.getInventory().addItem(ItemCreator.headWithSkin(args[0]));
                }
            } else {
                sender.sendMessage("/head <skinname> (itemname)");
            }
        }
    }
}
