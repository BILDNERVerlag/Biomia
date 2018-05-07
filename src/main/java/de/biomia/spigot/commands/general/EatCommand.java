package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EatCommand extends BiomiaCommand {

    public EatCommand() {
        super("eat");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {
        if (!Biomia.getBiomiaPlayer((Player) sender).isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }
        ((Player) sender).setFoodLevel(20);
    }

}
