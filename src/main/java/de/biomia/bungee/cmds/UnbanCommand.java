package de.biomia.bungee.cmds;

import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.var.BanManager;
import de.biomia.bungee.var.Bans;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class UnbanCommand extends Command {

    public UnbanCommand(String name) {
        super(name);
    }

    private static boolean unban(OfflineBungeeBiomiaPlayer von, int biomiaID) {

        List<Bans> ban = new ArrayList<>();

        BungeeMain.activeBans.forEach(eachBan -> {
            if (eachBan.getBiomiaID() == biomiaID)
                ban.add(eachBan);
        });
        ban.forEach(each -> BanManager.moveToCache(each, von));
        return !ban.isEmpty();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("biomia.unban"))
            if (args.length == 1) {
                if (unban(BungeeBiomia.getOfflineBiomiaPlayer(sender.getName()), BungeeBiomia.getOfflineBiomiaPlayer(args[0]).getBiomiaPlayerID())) {
                    sender.sendMessage(new TextComponent("\u00A7c" + args[0] + " wurde erfolgreich entbannt!"));
                } else
                    sender.sendMessage(new TextComponent("\u00A7c" + args[0] + " ist nicht gebannt!"));
            } else
                sender.sendMessage(new TextComponent("\u00A7cBitte nutze \u00A77/\u00A7bunban \u00A77<\u00A7cSpieler\u00A77>"));
    }
}