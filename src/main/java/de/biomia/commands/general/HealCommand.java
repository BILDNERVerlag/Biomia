package de.biomia.commands.general;

import de.biomia.commands.BiomiaCommand;
import de.biomia.messages.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand extends BiomiaCommand {

    public HealCommand() {
        super("heal");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (!sender.hasPermission("biomia.heal")) {
            sender.sendMessage(Messages.NO_PERM);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("\u00A7cNur Spieler k\u00F6nnen sich heilen!");
            return true;
        }

        Player p = (Player) sender;
        p.setHealth(p.getHealthScale());
        sender.sendMessage("\u00A7dDu wurdest vollst\u00fcndig geheilt!");

        return true;
    }

}
