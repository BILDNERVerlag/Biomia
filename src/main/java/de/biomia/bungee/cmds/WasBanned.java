package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.var.Bans;
import de.biomia.spigot.messages.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class WasBanned extends Command {

    public WasBanned(String name) {
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
                    sender.sendMessage(new TextComponent("§bDer Spieler §c" + name + " wurde bereits §c" + i + "x §bgebannt!"));
                else
                    sender.sendMessage(new TextComponent("§bDer Spieler §c" + name + " §bwurde noch §cnie §bgebannt!"));
            } else
                sender.sendMessage(new TextComponent("§cBitte nutze §b/wasbanned <Spieler>"));
        } else
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
    }
}
