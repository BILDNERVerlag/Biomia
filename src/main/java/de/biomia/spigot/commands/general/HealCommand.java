package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand extends BiomiaCommand {

    public HealCommand() {
        super("heal");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {


        if (!(sender instanceof Player)) {
            sender.sendMessage("\u00A7cNur Spieler können sich heilen!");
            return;
        }

        if (!Biomia.getBiomiaPlayer((Player) sender).isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        Player p = (Player) sender;
        p.setHealth(p.getHealthScale());
        sender.sendMessage("\u00A7bDu wurdest vollständig geheilt!");
    }
}
