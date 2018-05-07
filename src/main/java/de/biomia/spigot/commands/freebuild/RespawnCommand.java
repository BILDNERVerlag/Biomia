package de.biomia.spigot.commands.freebuild;

import de.biomia.spigot.commands.BiomiaCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RespawnCommand extends BiomiaCommand {

    public RespawnCommand() {
        super("respawn", "suicide");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.teleport(new Location(Bukkit.getWorld("world"), -230, 68, 350.5, 16, 0));
        }
    }

}
