package de.biomia.bungee.cmds;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.ServerGroupObject;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.biomia.universal.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BlistCommand extends Command {

    public BlistCommand() {
        super("biomialist", "bungeecord.command.list", "blist");
    }

    private int onlinePlayers = 0;

    @Override
    public void execute(CommandSender sender, String[] strings) {

        boolean detail = false;

        if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("detail")) {
                detail = true;
            }
        }

        for (ServerGroupObject serverGroupObject : TimoCloudAPI.getBungeeAPI().getThisProxy().getGroup().getServerGroups()) {
            onlinePlayers = 0;
            serverGroupObject.getServers().forEach(each -> onlinePlayers += each.getOnlinePlayerCount());
            if (onlinePlayers == 0)
                continue;

            TextComponent component;

            if (!detail) {
                component = new TextComponent(Messages.format("["));
                TextComponent serverName = new TextComponent(Messages.format(serverGroupObject.getName()));
                serverName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + serverGroupObject.getServers().get(0).getName()));
                component.addExtra(serverName);
                component.addExtra(Messages.format("] (%s)", onlinePlayers));
                sender.sendMessage(component);
            } else {
                for (ServerObject serverObject : serverGroupObject.getServers()) {

                    if (serverObject.getOnlinePlayerCount() == 0)
                        continue;

                    component = new TextComponent();
                    component.setText(Messages.format("["));
                    TextComponent serverName = new TextComponent(Messages.format(serverGroupObject.getName()));
                    serverName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + serverObject.getName()));
                    component.addExtra(serverName);
                    component.addExtra(Messages.format("] (%s): ", serverObject.getOnlinePlayerCount()));

                    boolean first = true;
                    for (ProxiedPlayer pp : ProxyServer.getInstance().getServerInfo(serverObject.getName()).getPlayers()) {
                        if (!first) {
                            component.addExtra(Messages.format(", "));
                        }

                        TextComponent playerName = new TextComponent("§7" + pp.getName());
                        playerName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gtp " + pp.getName()));
                        component.addExtra(playerName);
                        first = false;
                    }
                    sender.sendMessage(component);
                }
            }
        }
        sender.sendMessage(new TextComponent(Messages.format("Gesamte Spieleranzahl: %s", TimoCloudAPI.getBungeeAPI().getThisProxy().getGroup().getOnlinePlayerCount())));
    }
}
