package de.biomia.bungee.cmds;

import de.biomia.Biomia;
import de.biomia.OfflineBiomiaPlayer;
import de.biomia.bungee.Main;
import de.biomia.bungee.main.BungeeBiomiaPlayer;
import de.biomia.bungee.var.Bans;
import de.biomia.messages.Messages;
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
                int id = Biomia.getOfflineBiomiaPlayer(name).getBiomiaPlayerID();
                for (Bans ban : Main.cachedBans)
                    if (ban.getBiomiaID() == id)
                        i++;
                if (i > 0)
                    sender.sendMessage(new TextComponent("�bDer Spieler �c" + name + " wurde bereits �c" + i + "x �bgebannt!"));
                else
                    sender.sendMessage(new TextComponent("�bDer Spieler �c" + name + " �bwurde noch �cnie �bgebannt!"));
            } else
                sender.sendMessage(new TextComponent("�cBitte nutze �b/wasbanned <Spieler>"));
        } else
            sender.sendMessage(new TextComponent(Messages.NO_PERM));
    }
}
