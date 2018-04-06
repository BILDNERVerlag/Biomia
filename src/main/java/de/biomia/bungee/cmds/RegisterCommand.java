package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.events.ChannelListener;
import de.biomia.bungee.msg.BungeeMessages;
import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import de.biomia.universal.Ranks;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RegisterCommand extends Command {

    public RegisterCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;

            if (MySQL.executeQuerygetint("SELECT * FROM `ConnectedPlayers` where uuid = '" + p.getUniqueId().toString() + "'", "wert", MySQL.Databases.biomia_db) == 1) {
                p.sendMessage(new TextComponent("\u00A7aDu bist bereits registriert!"));
                return;
            }

            if (args.length == 0) {
                TextComponent register = new TextComponent();
                register.setText(BungeeMessages.registerAt);
                register.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));
                p.sendMessage(register);
                TextComponent klicker = new TextComponent("\u00A77Klick mich^^");
                klicker.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));
                p.sendMessage(klicker);
            } else if (args.length == 1) {
                String code = MySQL.executeQuery("SELECT * FROM `ConnectedPlayers` where uuid = '" + p.getUniqueId().toString() + "'", "code", MySQL.Databases.biomia_db);
                if (code != null && code.equals(args[0])) {
                    MySQL.executeUpdate("UPDATE `ConnectedPlayers` set `wert` = true where uuid = '" + p.getUniqueId().toString() + "'", MySQL.Databases.biomia_db);
                    OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(p.getName());
                    bp.setRank(Ranks.RegSpieler);
                    p.sendMessage(new TextComponent(Messages.PREFIX + "\u00A7aDu bist jetzt registriert!"));
                } else if (code == null) {
                    p.sendMessage(new TextComponent(
                            Messages.PREFIX + "\u00A7cDu musst dich erst auf der Biomia Internetseite registrieren!"));
                } else if (!code.equals(args[0])) {
                    p.sendMessage(new TextComponent(Messages.PREFIX + "\u00A7cFalscher Code!"));
                }
            }
        }
    }
}
