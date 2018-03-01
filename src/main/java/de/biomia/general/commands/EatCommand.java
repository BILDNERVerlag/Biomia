package de.biomia.general.commands;

import de.biomia.api.messages.Messages;
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

        if (!(sender instanceof Player)) {
            sender.sendMessage("\u00A7cNur Spieler k\u00F6nnen essen!");
            return true;
        }

        Player p = (Player) sender;

        p.setFoodLevel(20);
        return true;
    }

}
