package de.biomia.server.freebuild.home.commands;

import de.biomia.Main;
import de.biomia.commands.BiomiaCommand;
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
