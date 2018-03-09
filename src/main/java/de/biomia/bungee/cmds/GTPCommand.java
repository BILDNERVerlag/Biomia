package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.events.ChannelListener;
import de.biomia.universal.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GTPCommand extends Command {

    public GTPCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("biomia.bungeeteleport") || pp.hasPermission("biomia.*")) {

                switch (args.length) {
                case 1: {

                    ProxiedPlayer to = BungeeCord.getInstance().getPlayer(args[0]);

                    if (to != null) {

                        if (!pp.getServer().getInfo().equals(to.getServer().getInfo()))
                            pp.connect(to.getServer().getInfo());

                        Thread thread = new Thread(() -> {

                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException ignored) {
                            }
                            ChannelListener.teleport(pp.getName(), to.getName(), to.getServer().getInfo());

                        });
                        thread.start();
                        BungeeMain.allThreads.add(thread);
                    } else {
                        sender.sendMessage(new TextComponent(Messages.NOT_ONLINE));
                    }

                    break;
                }
                case 2: {

                    ProxiedPlayer from = BungeeCord.getInstance().getPlayer(args[0]);
                    ProxiedPlayer to = BungeeCord.getInstance().getPlayer(args[1]);

                    if (from != null && to != null) {
                        if (!from.getServer().getInfo().equals(to.getServer().getInfo()))
                            from.connect(to.getServer().getInfo());
                        ChannelListener.teleport(from.getName(), to.getName(), to.getServer().getInfo());
                    } else {
                        sender.sendMessage(new TextComponent(Messages.NOT_ONLINE));
                    }
                    break;
                }
                default:
                    sender.sendMessage(new TextComponent("§cBitte nutze §b/gtp <Spieler> [Spieler]"));
                    break;
                }

            } else {
                sender.sendMessage(new TextComponent(Messages.NO_PERM));
            }
        } else {
            if (args.length == 2) {

                ProxiedPlayer from = BungeeCord.getInstance().getPlayer(args[0]);
                ProxiedPlayer to = BungeeCord.getInstance().getPlayer(args[1]);

                if (from != null && to != null) {
                    if (!from.getServer().getInfo().equals(to.getServer().getInfo()))
                        from.connect(to.getServer().getInfo());
                    ChannelListener.teleport(from.getName(), to.getName(), to.getServer().getInfo());
                } else {
                    sender.sendMessage(new TextComponent(Messages.NOT_ONLINE));
                }

            } else {
                sender.sendMessage(new TextComponent("§cBitte nutze §b/gtp <Spieler> [Spieler]"));
            }
        }
    }
}