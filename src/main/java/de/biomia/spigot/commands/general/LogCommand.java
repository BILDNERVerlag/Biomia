package de.biomia.spigot.commands.general;

import de.biomia.spigot.achievements.Stats;
import de.biomia.spigot.commands.BiomiaCommand;
import org.bukkit.command.CommandSender;

public class LogCommand extends BiomiaCommand {

    public LogCommand() {
        super("log");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("biomia.log")) {
            Stats.logSwitch();
            if (Stats.isLogging())
                sender.sendMessage("§7§kxxx§r§cLogging §baktiviert§c!§7§kxxx");
            else sender.sendMessage("§7§kxxx§r§cLogging §bdeaktiviert§c!§7§kxxx");
        }
        return true;
    }
}