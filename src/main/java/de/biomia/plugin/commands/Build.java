package de.biomia.plugin.commands;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.main.Main;
import de.biomia.api.msg.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Build implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

        if (sender.hasPermission("biomia.build." + Main.getGroupName()) || sender.hasPermission("biomia.build.*")) {
            BiomiaPlayer bp;
            if (args.length == 0) {
                if (sender instanceof Player)
                    bp = Biomia.getBiomiaPlayer((Player) sender);
                else {
                    sender.sendMessage("00A7cDu musst ein Spieler sein!");
                    return true;
                }
            } else {
                Player p = Bukkit.getPlayer(args[0]);
                if (p != null)
                    bp = Biomia.getBiomiaPlayer(p);
                else {
                    sender.sendMessage("00A7cDer Spieler ist nicht Online!");
                    return true;
                }
            }
            if (!bp.canBuild()) {
                bp.setBuild(true);
                bp.getPlayer().sendMessage("00A7aDu kannst nun bauen!");
                if (sender.equals(bp.getPlayer())) {
                    sender.sendMessage("00A7aDer Spieler 00A76" + bp.getPlayer().getName() + " 00A7akann nun bauen!");
                }

            } else {
                bp.setBuild(false);
                bp.getPlayer().sendMessage("00A7cDu kannst nun nicht mehr bauen!");
                if (!sender.equals(bp.getPlayer())) {
                    sender.sendMessage("00A7cDer Spieler 00A76" + bp.getPlayer().getName() + " 00A7ckann nun nicht mehr bauen!");
                }

            }
        } else
            sender.sendMessage(Messages.NO_PERM);

        return true;
    }

}