package de.biomia.bungee.cmds;

import de.biomia.universal.Messages;
import de.biomia.universal.MySQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ModusCommand extends Command {

    public static boolean wartungsModus = getModus();

    public ModusCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer) sender;
            if (pp.hasPermission("biomia.setmode") || pp.hasPermission("biomia.*")) {
                if (args.length == 1) {
                    String switcher = args[0];
                    if (switcher.toLowerCase().equals("on")) {
                        wartungsModus = true;
                        setModus(true);
                        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers())
                            if (!p.hasPermission("biomia.join"))
                                p.disconnect(new TextComponent("§cDer Server ist jetzt im §bWartungsmodus§c!"));
                        ProxyServer.getInstance().broadcast(new TextComponent("§cDer Server ist jetzt im §bWartungsmodus§c!"));
                    } else if (switcher.toLowerCase().equals("off")) {
                        wartungsModus = false;
                        setModus(false);
                        ProxyServer.getInstance().broadcast(new TextComponent("§cDer Server ist nicht länger im §bWartungsmodus§c!"));
                    }
                } else
                    pp.sendMessage(new TextComponent("§c/modus <on | off>"));
            } else {
                pp.sendMessage(new TextComponent(Messages.NO_PERM));
            }
        }

    }


    private static void setModus(boolean b) {
        MySQL.executeUpdate("UPDATE Modus SET Wert = " + b, MySQL.Databases.biomia_db);
    }

    private static boolean getModus() {
        return MySQL.executeQuerygetint("SELECT Wert FROM Modus", "Wert", MySQL.Databases.biomia_db) == 1;
    }
}