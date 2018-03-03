package de.biomia.bungee.cmds;

import de.biomia.Biomia;
import de.biomia.bungee.Main;
import de.biomia.bungee.var.Bans;
import de.biomia.general.reportsystem.ReportSQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class Unban extends Command {

    public Unban(String name) {
        super(name);
    }

    private static boolean unban(int biomiaID) {

        List<Bans> ban = new ArrayList<>();

        Main.activeBans.forEach(eachBan -> {
            if (eachBan.getBiomiaID() == biomiaID)
                ban.add(eachBan);
        });

        ban.forEach(ReportSQL::moveToCache);
        return !ban.isEmpty();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("biomia.unban"))
            if (args.length == 1) {

                if (unban(Biomia.getOfflineBiomiaPlayer(args[0]).getBiomiaPlayerID())) {
                    sender.sendMessage(new TextComponent("§c" + args[0] + " wurde erfolgreich entbannt!"));
                } else
                    sender.sendMessage(new TextComponent("§c" + args[0] + " ist nicht gebannt!"));

            } else
                sender.sendMessage(new TextComponent("§cBitte nutze /unban <Spieler>"));
    }
}