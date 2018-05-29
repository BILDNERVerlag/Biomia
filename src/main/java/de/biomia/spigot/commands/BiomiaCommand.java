package de.biomia.spigot.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;

abstract public class BiomiaCommand extends BukkitCommand {

    protected BiomiaCommand(String command, String... args) {
        super(command, "", "", Arrays.asList(args));
    }

    /**
     * Deprecate
     * <p>
     * use onCommand instead of
     * (easier to use, cause of no boolean return)
     */
    @Deprecated
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        onCommand(sender, label, args);
        return true;
    }

    protected void onCommand(CommandSender sender, String label, String[] args) {

    }
}