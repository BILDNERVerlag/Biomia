package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.events.ChannelListener;
import de.biomia.bungee.var.Bans;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import de.biomia.universal.Time;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

    public BanCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("biomia.ban"))
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer pp = (ProxiedPlayer) sender;

                if (args.length == 0) {
                    sender.sendMessage(new TextComponent(Messages.format("Bitte nutze /ban <Spieler>")));
                } else {
                    if (args[0].length() > 16) {
                        sender.sendMessage(new TextComponent(Messages.format("Ungültiger Name! Maximal 16 Zeichen!")));
                        return;
                    }
                    ChannelListener.sendBanRequest(BungeeBiomia.getOfflineBiomiaPlayer(pp.getName()), BungeeBiomia.getOfflineBiomiaPlayer(args[0]).getBiomiaPlayerID());
                }
            }
    }

    public static void banPerm(ProxiedPlayer sender, int biomiaID, String grund) {
        OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(sender.getName());
        OfflineBungeeBiomiaPlayer target = BungeeBiomia.getOfflineBiomiaPlayer(biomiaID);

        if (sender.hasPermission("biomia.ban.perm")) {
            BungeeMain.activeBans.add(new Bans(true, -1, grund, biomiaID, bp.getBiomiaPlayerID(), (int) System.currentTimeMillis() / 1000));
            if (target.isOnline())
                target.getProxiedPlayer().disconnect(new TextComponent(Messages.format("Du wurdest wegen %s permanent gebannt.", grund)));
            TextComponent comp = new TextComponent(Messages.format("Der Spieler %s wurde permanent gebannt wegen %s!", target.getName(), grund));
            sender.sendMessage(comp);
            MySQL.executeUpdate(String.format("INSERT INTO `BanList`(`biomiaID`, `Grund`, `timestamp`, `länge`, `permanent`, von) VALUES (%d, '%s', %d, -1, true, %d)", biomiaID, grund, System.currentTimeMillis() / 1000, bp.getBiomiaPlayerID()), MySQL.Databases.biomia_db);

        } else {
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
        }
    }

    public static void banTemp(ProxiedPlayer sender, int biomiaID, int sec, String grund) {
        OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(sender.getName());
        String targetName = BungeeBiomia.getOfflineBiomiaPlayer(biomiaID).getName();

        if (sender.hasPermission("biomia.ban.temp")) {

            TextComponent comp = new TextComponent(Messages.format("Der Spieler %s wurde von %s für %s wegen %s gebannt!", targetName, bp.getName(), Time.toText(sec), grund));
            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers())
                if (BungeeBiomia.getOfflineBiomiaPlayer(p.getName()).isSrStaff())
                    p.sendMessage(comp);

            int now = (int) (System.currentTimeMillis() / 1000);

            BungeeMain.activeBans.add(new Bans(false, sec + now,
                    grund, biomiaID, bp.getBiomiaPlayerID(), now));

            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(targetName);
            if (p != null)
                p.disconnect(new TextComponent(Messages.format("Du wurdest wegen %s für %s gebannt.", grund, Time.toText(sec))));

            MySQL.executeUpdate(String.format("INSERT INTO `BanList`(`biomiaID`, `Grund`, `timestamp`, `länge`, `permanent`, von) VALUES (%d, '%s', %d, %d, false, %d)", biomiaID, grund, System.currentTimeMillis() / 1000, sec, bp.getBiomiaPlayerID()), MySQL.Databases.biomia_db);

        } else {
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
        }
    }
}
