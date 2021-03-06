package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.events.ChannelListener;
import de.biomia.universal.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GTPCommand extends Command {

    public GTPCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(sender.getName());
        if (!bp.isSrStaff() && !bp.isModerator()) {
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
        } else {
            ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (args.length == 0) {
                sender.sendMessage(new TextComponent(Messages.format("Bitte nutze /gtp <Spieler> [Spieler]")));
                return;
            }
            ProxiedPlayer from = args.length > 1 ? ProxyServer.getInstance().getPlayer(args[0]) : pp;
            ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args.length > 1 ? args[1] : args[0]);
            if (from != null && to != null) {
                if (!from.getServer().equals(to.getServer()))
                    from.connect(to.getServer().getInfo());
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {
                    }
                    ChannelListener.teleport(pp.getName(), to.getName(), to.getServer().getInfo());
                });
                thread.start();
                BungeeMain.allThreads.add(thread);
            } else {
                sender.sendMessage(new TextComponent(Messages.NOT_ONLINE));
            }
        }
    }
}