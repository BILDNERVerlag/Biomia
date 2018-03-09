package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.var.Bans;
import de.biomia.universal.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class WasBannedCommand extends Command {

    public WasBannedCommand(String name) {
        super(name);
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("biomia.*") || sender.hasPermission("biomia.wasbanned")) {

            if (args.length >= 1) {
                String name = args[0];
                int i = 0;
                int id = BungeeBiomia.getOfflineBiomiaPlayer(name).getBiomiaPlayerID();
                for (Bans ban : BungeeMain.cachedBans)
                    if (ban.getBiomiaID() == id)
                        i++;
                if (i > 0)
                    sender.sendMessage(new TextComponent("\u00A7bDer Spieler \u00A7c" + name + " wurde bereits \u00A7c" + i + "x \u00A7bgebannt!"));
                else
                    sender.sendMessage(new TextComponent("\u00A7bDer Spieler \u00A7c" + name + " \u00A7bwurde noch \u00A7cnie \u00A7bgebannt!"));
            } else
                sender.sendMessage(new TextComponent("\u00A7cBitte nutze \u00A7b/wasbanned <Spieler>"));
        } else
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
    }
}
