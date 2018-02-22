package de.biomia.plugin.commands;

import at.TimoCraft.TimoCloud.api.TimoCloudAPI;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.msg.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Build implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

        String group = TimoCloudAPI.getBukkitInstance().getThisServer().getGroupName();

        if (sender.hasPermission("biomia.build." + group) || sender.hasPermission("biomia.build.*")) {
            BiomiaPlayer bp;
            if (args.length == 0) {
                if (sender instanceof Player)
                    bp = Biomia.getBiomiaPlayer((Player) sender);
                else {
                    sender.sendMessage("§cDu musst ein Spieler sein!");
                    return true;
                }
            } else {
                Player p = Bukkit.getPlayer(args[0]);
                if (p != null)
                    bp = Biomia.getBiomiaPlayer(p);
                else {
                    sender.sendMessage("§cDer Spieler ist nicht Online!");
                    return true;
                }
            }
            if (!bp.canBuild()) {
                bp.setBuild(true);
                bp.getPlayer().sendMessage("§aDu kannst nun bauen!");
                if (sender.equals(bp.getPlayer())) {
                    sender.sendMessage("§aDer Spieler §6" + bp.getPlayer().getName() + " §akann nun bauen!");
                }

            } else {
                bp.setBuild(false);
                bp.getPlayer().sendMessage("§cDu kannst nun nicht mehr bauen!");
                if (!sender.equals(bp.getPlayer())) {
                    sender.sendMessage("§cDer Spieler §6" + bp.getPlayer().getName() + " §ckann nun nicht mehr bauen!");
                }

            }
        } else
            sender.sendMessage(Messages.noperm);

        return true;
    }

}