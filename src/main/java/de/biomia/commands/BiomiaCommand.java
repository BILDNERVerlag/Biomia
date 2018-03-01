package de.biomia.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;

abstract public class BiomiaCommand extends BukkitCommand {

    //TODO extend in every Command (see e.g. BuildCommand class)

    protected BiomiaCommand(String command, String... args) {
        super(command, "", "", Arrays.asList(args));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return true;
    }
}
