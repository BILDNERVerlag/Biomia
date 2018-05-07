package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
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
                int i;
                int id = BungeeBiomia.getOfflineBiomiaPlayer(name).getBiomiaPlayerID();
                i = (int) BungeeMain.cachedBans.stream().filter(ban -> ban.getBiomiaID() == id).count();
                if (i > 0)
                    sender.sendMessage(new TextComponent(String.format("§bDer Spieler §c%s wurde bereits §c%dx §bgebannt!", name, i)));
                else
                    sender.sendMessage(new TextComponent(String.format("§bDer Spieler §c%s §bwurde noch §cnie §bgebannt!", name)));
            } else
                sender.sendMessage(new TextComponent("\u00A7cBitte nutze \u00A7b/wasbanned <Spieler>"));
        } else
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
    }
}
