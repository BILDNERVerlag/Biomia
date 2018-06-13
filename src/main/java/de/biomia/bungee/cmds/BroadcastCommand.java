package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.universal.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {

    public BroadcastCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(sender.getName());
        if (!bp.isSrStaff() && !bp.isModerator()) {
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
        } else {
            String s = String.join(" ", args);
            s = ChatColor.translateAlternateColorCodes('&', s);
            ProxyServer.getInstance().broadcast(new TextComponent("§7[§bBroadcast§7]§r " + s));
        }
    }

}
