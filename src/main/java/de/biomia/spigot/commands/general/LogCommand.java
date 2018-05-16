package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.achievements.BiomiaAchievement;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogCommand extends BiomiaCommand {

    public LogCommand() {
        super("log");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        if (!Biomia.getBiomiaPlayer((Player) sender).isSrStaff()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (BiomiaAchievement.logSwitch())
            sender.sendMessage("\u00A77\u00A7kxxx\u00A7r\u00A7cLogging \u00A7baktiviert\u00A7c!\u00A77\u00A7kxxx");
        else sender.sendMessage("\u00A77\u00A7kxxx\u00A7r\u00A7cLogging \u00A7bdeaktiviert\u00A7c!\u00A77\u00A7kxxx");
    }
}