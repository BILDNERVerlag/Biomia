package de.biomia.spigot.server.freebuild.home.commands;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.BiomiaCommand;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends BiomiaCommand {

    public ReloadCommand() {
        super("rlconfig", "reloadconfig", "rlc");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Main.getPlugin().reloadConfig();
        return true;
    }
}
