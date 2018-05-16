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

        Player p = (Player) sender;
        if (!Biomia.getBiomiaPlayer(p).isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        p.setHealth(p.getHealthScale());
        sender.sendMessage("\u00A7bDu wurdest vollständig geheilt!");
    }
}
