package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
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

        ProxiedPlayer pp = (ProxiedPlayer) sender;
        if (BungeeBiomia.getOfflineBiomiaPlayer(pp.getName()).isSrStaff()) {
            if (args.length == 1) {
                String switcher = args[0];
                if (switcher.toLowerCase().equals("on")) {
                    wartungsModus = true;
                    setModus(true);
                    TextComponent component = new TextComponent(Messages.format("Der Server ist jetzt im Wartungsmodus!"));
                    for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers())
                        if (!p.hasPermission("biomia.join"))
                            p.disconnect(component);
                    ProxyServer.getInstance().broadcast(component);
                } else if (switcher.toLowerCase().equals("off")) {
                    wartungsModus = false;
                    setModus(false);
                    ProxyServer.getInstance().broadcast(new TextComponent(Messages.format("Der Server ist nicht länger im Wartungsmodus!")));
                }
            } else
                pp.sendMessage(new TextComponent(Messages.format("/modus <on | off>")));
        } else {
            pp.sendMessage(new TextComponent(Messages.NO_PERM));
        }
    }


    private static void setModus(boolean b) {
        MySQL.executeUpdate(String.format("UPDATE Modus SET Wert = %s", b), MySQL.Databases.biomia_db);
    }

    private static boolean getModus() {
        return MySQL.executeQuerygetint("SELECT Wert FROM Modus", "Wert", MySQL.Databases.biomia_db) == 1;
    }
}