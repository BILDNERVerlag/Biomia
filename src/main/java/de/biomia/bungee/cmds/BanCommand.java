package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.events.ChannelListener;
import de.biomia.bungee.events.Time;
import de.biomia.bungee.var.Bans;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
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
                    sender.sendMessage(new TextComponent("\u00A7cBitte nutze \u00A77/\u00A7bban \u00A77<\u00A7bSpieler\u00A77>"));
                } else {
                    if (args[0].length() > 16) {
                        sender.sendMessage(new TextComponent("\u00A7cUng\u00fcltiger Name! Maximal 16 Zeichen!"));
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
            TextComponent comp = new TextComponent("\u00A7aDer Spieler " + target.getName() + " wurde von " + bp.getName() + " permanent wegen " + grund + " gebannt!");
            sender.sendMessage(comp);

            BungeeMain.activeBans.add(new Bans(true, -1, grund, biomiaID, bp.getBiomiaPlayerID(), (int) System.currentTimeMillis() / 1000));
        } else {
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
        }

        if (target.isOnline())
            target.getProxiedPlayer().disconnect(new TextComponent("\u00A7cDu wurdest wegen \u00A7b" + grund + " \u00A7cpermanent gebannt."));

        MySQL.executeUpdate("INSERT INTO `BanList`(`biomiaID`, `Grund`, `timestamp`, `l\u00e4nge`, `permanent`, von) VALUES ("
                + biomiaID + ", '" + grund + "', " + System.currentTimeMillis() / 1000 + ", -1, true, " + bp.getBiomiaPlayerID() + ")", MySQL.Databases.biomia_db);
    }

    public static void banTemp(ProxiedPlayer sender, int biomiaID, int sec, String grund) {
        OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(sender.getName());
        String targetName = BungeeBiomia.getOfflineBiomiaPlayer(biomiaID).getName();

        if (sender.hasPermission("biomia.ban.temp")) {

            TextComponent comp = new TextComponent("\u00A7aDer Spieler " + targetName + " wurde von " + bp.getName() + " für " + Time.toText(sec) + " \u00A7awegen " + grund + " gebannt!");
            for (ProxiedPlayer p : BungeeCord.getInstance().getPlayers()) {
                if (p.hasPermission("biomia.ban.getmessages"))
                    p.sendMessage(comp);
            }

            int now = (int) (System.currentTimeMillis() / 1000);

            BungeeMain.activeBans.add(new Bans(false, sec + now,
                    grund, biomiaID, bp.getBiomiaPlayerID(), now));
        } else {
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
        }

        ProxiedPlayer p = BungeeCord.getInstance().getPlayer(targetName);
        if (p != null)
            p.disconnect(new TextComponent("\u00A7cDu wurdest wegen \u00A76" + grund + " \u00A7cf\u00fcr " + Time.toText(sec) + " \u00A7cgebannt."));

        MySQL.executeUpdate("INSERT INTO `BanList`(`biomiaID`, `Grund`, `timestamp`, `l\u00e4nge`, `permanent`, von) VALUES ("
                + biomiaID + ", '" + grund + "', " + System.currentTimeMillis() / 1000 + ", " + sec + ", false, " + bp.getBiomiaPlayerID() + ")", MySQL.Databases.biomia_db);

    }
}
