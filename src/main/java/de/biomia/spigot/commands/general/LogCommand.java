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
            sender.sendMessage("§7§kxxx§r§cLogging §baktiviert§c!§7§kxxx");
        else sender.sendMessage("§7§kxxx§r§cLogging §bdeaktiviert§c!§7§kxxx");
    }
}