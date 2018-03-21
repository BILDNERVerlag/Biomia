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
                sender.sendMessage("\u00A77\u00A7kxxx\u00A7r\u00A7cLogging \u00A7baktiviert\u00A7c!\u00A77\u00A7kxxx");
            else sender.sendMessage("\u00A77\u00A7kxxx\u00A7r\u00A7cLogging \u00A7bdeaktiviert\u00A7c!\u00A77\u00A7kxxx");
        }
        return true;
    }
}