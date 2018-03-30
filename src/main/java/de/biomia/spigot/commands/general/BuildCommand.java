package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand extends BiomiaCommand {

    public BuildCommand() {
        super("build");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("biomia.build." + Biomia.getServerInstance().getServerType().name()) || sender.hasPermission("biomia.build.*")) {
            BiomiaPlayer bp;
            if (args.length == 0) {
                if (sender instanceof Player)
                    bp = Biomia.getBiomiaPlayer((Player) sender);
                else {
                    sender.sendMessage(Messages.NO_PLAYER);
                    return true;
                }
            } else {
                Player p = Bukkit.getPlayer(args[0]);
                if (p != null)
                    bp = Biomia.getBiomiaPlayer(p);
                else {
                    sender.sendMessage(Messages.NOT_ONLINE);
                    return true;
                }
            }
            if (!bp.canBuild()) {
                bp.setBuild(true);
                bp.getPlayer().sendMessage("\u00A7aDu kannst nun bauen!");
                if (sender.equals(bp.getPlayer())) {
                    sender.sendMessage("\u00A7aDer Spieler \u00A76" + bp.getPlayer().getName() + " \u00A7akann nun bauen!");
                }

            } else {
                bp.setBuild(false);
                bp.getPlayer().sendMessage("\u00A7cDu kannst nun nicht mehr bauen!");
                if (!sender.equals(bp.getPlayer())) {
                    sender.sendMessage("\u00A7cDer Spieler \u00A76" + bp.getPlayer().getName() + " \u00A7ckann nun nicht mehr bauen!");
                }

            }
        } else
            sender.sendMessage(Messages.NO_PERM);

        return true;
    }
}