package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
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
        OfflineBungeeBiomiaPlayer bp = BungeeBiomia.getOfflineBiomiaPlayer(sender.getName());
        if (!bp.isSrStaff()) {
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
            return;
        }
        if (args.length >= 1) {
            String name = args[0];
            int id = BungeeBiomia.getOfflineBiomiaPlayer(name).getBiomiaPlayerID();
            int i = (int) BungeeMain.cachedBans.stream().filter(ban -> ban.getBiomiaID() == id).count();
            if (i > 0)
                sender.sendMessage(new TextComponent(String.format("%sDer Spieler %s%s%s wurde bereits %s%dx%s gebannt!", Messages.COLOR_MAIN, Messages.COLOR_SUB, name, Messages.COLOR_MAIN, Messages.COLOR_SUB, i, Messages.COLOR_MAIN)));
            else
                sender.sendMessage(new TextComponent(String.format("%sDer Spieler %s%s%s wurde noch %snie %sgebannt!", Messages.COLOR_MAIN, Messages.COLOR_SUB, name, Messages.COLOR_MAIN, Messages.COLOR_SUB, Messages.COLOR_MAIN)));
        } else {
            sender.sendMessage(new TextComponent(Messages.format("Bitte nutze /wasbanned <Spieler>")));
            sender.sendMessage(new TextComponent(Messages.format("Das ist ein %s!", "Test")));
        }
    }
}
