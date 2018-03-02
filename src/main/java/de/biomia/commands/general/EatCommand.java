package de.biomia.commands.general;

import de.biomia.commands.BiomiaCommand;
import de.biomia.messages.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EatCommand extends BiomiaCommand {

    public EatCommand() {
        super("eat");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (!sender.hasPermission("biomia.eat")) {
            sender.sendMessage(Messages.NO_PERM);
            return true;
        }

        Player p = (Player) sender;
        p.setFoodLevel(50);
        return true;
    }

}
