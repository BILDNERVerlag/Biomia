package de.biomia.general.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;

abstract public class BiomiaCommand extends BukkitCommand {

    //TODO extend in every CommandClass (see BuildCommand class)

    public BiomiaCommand(String command, String... args) {
        super(command, "", "", Arrays.asList(args));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return true;
    }

}
