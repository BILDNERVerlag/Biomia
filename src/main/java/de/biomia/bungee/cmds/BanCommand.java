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
                    sender.sendMessage(new TextComponent(String.format("%sBitte nutze %s/%sban %s<%sSpieler%s>", Messages.COLOR_MAIN, Messages.COLOR_AUX, Messages.COLOR_SUB, Messages.COLOR_AUX, Messages.COLOR_SUB, Messages.COLOR_AUX)));
                } else {
                    if (args[0].length() > 16) {
                        sender.sendMessage(new TextComponent(String.format("%sUngültiger Name! Maximal 16 Zeichen!", Messages.COLOR_MAIN)));
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
                target.getProxiedPlayer().disconnect(new TextComponent(String.format("%sDu wurdest wegen %s%s%s permanent gebannt.", Messages.COLOR_MAIN, Messages.COLOR_SUB, grund, Messages.COLOR_MAIN)));
            TextComponent comp = new TextComponent(String.format("%sDer Spieler %s%s%s wurde permanent gebannt wegen %s%s%s!", Messages.COLOR_MAIN, Messages.COLOR_SUB, target.getName(), Messages.COLOR_MAIN, Messages.COLOR_SUB, grund, Messages.COLOR_MAIN));
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

            TextComponent comp = new TextComponent("§7Der Spieler §c" + targetName + " §7wurde von§b " + bp.getName() + " §7für§c " + Time.toText(sec) + " §7wegen§b " + grund + " §7gebannt!");
            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                if (BungeeBiomia.getOfflineBiomiaPlayer(p.getName()).isSrStaff())
                    p.sendMessage(comp);
            }

            int now = (int) (System.currentTimeMillis() / 1000);

            BungeeMain.activeBans.add(new Bans(false, sec + now,
                    grund, biomiaID, bp.getBiomiaPlayerID(), now));

            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(targetName);
            if (p != null)
                p.disconnect(new TextComponent("§7Du wurdest wegen§c " + grund + " §7für " + Time.toText(sec) + " §7gebannt."));

            MySQL.executeUpdate("INSERT INTO `BanList`(`biomiaID`, `Grund`, `timestamp`, `länge`, `permanent`, von) VALUES ("
                    + biomiaID + ", '" + grund + "', " + System.currentTimeMillis() / 1000 + ", " + sec + ", false, " + bp.getBiomiaPlayerID() + ")", MySQL.Databases.biomia_db);

        } else {
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
        }
    }
}
