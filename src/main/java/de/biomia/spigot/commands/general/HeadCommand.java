package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HeadCommand extends BiomiaCommand {

    public HeadCommand() {
        super("head");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        if (!Biomia.getBiomiaPlayer((Player) sender).isStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

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
