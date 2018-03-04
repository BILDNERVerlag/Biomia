package de.biomia.bungee.cmds;

import de.biomia.BungeeBiomia;
import de.biomia.OfflineBungeeBiomiaPlayer;
import de.biomia.OfflineBiomiaPlayer;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.events.ChannelListener;
import de.biomia.bungee.events.Time;
import de.biomia.bungee.var.Bans;
import de.biomia.data.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ban extends Command {

    public Ban(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        // /ban DerJulsn GRUND
        // /ban DerJulsn 4D GRUND

        if (sender.hasPermission("biomia.ban"))
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer pp = (ProxiedPlayer) sender;

                if (args.length == 0) {
                    sender.sendMessage(new TextComponent("§cBitte nutze §b/ban <Spieler>"));
                } else {
                    if (args[0].length() > 16) {
                        sender.sendMessage(new TextComponent("§cUngültiger Name! Maximal 16 Zeichen!"));
                        return;
                    }
                    ChannelListener.sendBanRequest(BungeeBiomia.getOfflineBiomiaPlayer(pp.getName()), BungeeBiomia.getOfflineBiomiaPlayer(args[0]).getBiomiaPlayerID());
                }
            }

    }

    public static void banPerm(ProxiedPlayer sender, int biomiaID, String grund) {

        OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(sender.getName());

        if (sender.hasPermission("biomia.ban.perm")) {
            TextComponent comp = new TextComponent(
                    "§aDer Spieler " + bp.getName() + " wurde permanent wegen " + grund + " gebannt!");
            sender.sendMessage(comp);

            BungeeMain.activeBans.add(new Bans(true, -1, grund, biomiaID, bp.getBiomiaPlayerID(), (int) System.currentTimeMillis() / 1000));
        } else {
            sender.sendMessage(new TextComponent("§cKeine Berechtigung um einen Spieler Permanent zu bannen!"));
        }


        if (bp.isOnline())
            bp.getProxiedPlayer().disconnect(new TextComponent("§cDu wurdest wegen §6" + grund + " §cpermanent gebannt."));

        MySQL.executeUpdate("INSERT INTO `BanList`(`biomiaID`, `Grund`, `timestamp`, `länge`, `permanent`, von) VALUES ("
                + biomiaID + ", '" + grund + "', " + System.currentTimeMillis() / 1000 + ", -1, true, " + bp.getBiomiaPlayerID() + ")", MySQL.Databases.biomia_db);
    }

    public static void banTemp(ProxiedPlayer sender, int biomiaID, int sec, String grund) {


        OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(sender.getName());
        String name = bp.getName();

        if (sender.hasPermission("biomia.ban.temp")) {

            TextComponent comp = new TextComponent("§aDer Spieler " + name + " wurde " + Time.toText(sec) + " §awegen " + grund + " gebannt!");
            for (ProxiedPlayer p : BungeeCord.getInstance().getPlayers()) {
                if (p.hasPermission("biomia.ban.getmessages"))
                    p.sendMessage(comp);
            }

            int now = (int) (System.currentTimeMillis() / 1000);

            BungeeMain.activeBans.add(new Bans(false, sec + now,
                    grund, biomiaID, bp.getBiomiaPlayerID(), now));
        } else {
            sender.sendMessage(new TextComponent("§cKeine Berechtigung um einen Spieler Temporär zu bannen!"));
        }

        ProxiedPlayer p = BungeeCord.getInstance().getPlayer(name);
        if (p != null)
            p.disconnect(new TextComponent("§cDu wurdest wegen §6" + grund + " §cfür " + Time.toText(sec) + " §cgebannt."));

        MySQL.executeUpdate("INSERT INTO `BanList`(`biomiaID`, `Grund`, `timestamp`, `länge`, `permanent`, von) VALUES ("
                + biomiaID + ", '" + grund + "', " + System.currentTimeMillis() / 1000 + ", " + sec + ", false, " + bp.getBiomiaPlayerID() + ")", MySQL.Databases.biomia_db);

    }
}
