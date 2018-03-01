package de.biomia.commands.freebuild;

import de.biomia.Biomia;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildForAllCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission("biomia.freebuildadmin")) {
            for (Object o : Bukkit.getOnlinePlayers()) {
                if (o instanceof Player) {
                    Biomia.getBiomiaPlayer((Player) o).setBuild(true);
                    sender.sendMessage(((Player) o).getDisplayName() + " kann nun bauen.");
                }
            }
        }
        return false;
    }

}
