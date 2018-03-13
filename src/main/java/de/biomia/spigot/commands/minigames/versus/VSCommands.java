package de.biomia.spigot.commands.minigames.versus;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.minigames.versus.settings.VSRequest;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VSCommands extends BiomiaCommand {

    public VSCommands(String string) {
        super(string);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

            if (args.length == 0 && getName().equalsIgnoreCase("spawn")) {
                ((Versus) Biomia.getSeverInstance()).getManager().moveToLobby(p);
            } else if (args.length == 1) {
                String player = args[0];
                Player parg = Bukkit.getPlayer(player);
                if (parg != null) {
                    BiomiaPlayer bparg = Biomia.getBiomiaPlayer(parg);
                    if (VSRequest.hasRequestSended(bparg, bp)) {
                        VSRequest request = VSRequest.getRequest(bparg);
                        if (request == null)
                            return true;
                        switch (getName()) {
                        case "accept":
                            request.accept();
                            break;
                        case "decline":
                            request.decline();
                            break;
                        default:
                            break;
                        }
                    } else {
                        switch (getName()) {
                        case "accept":
                            p.sendMessage("Dieser Spieler hat dich nicht herausgefordert!");
                            break;
                        case "decline":
                            p.sendMessage("Dieser Spieler hat dich nicht herausgefordert!");
                            break;
                        case "request":
                            new VSRequest(bp, bparg).sendRequest();
                            break;
                        default:
                            break;
                        }
                    }
                } else
                    sender.sendMessage(Messages.NOT_ONLINE);
            } else
                sender.sendMessage("Bitte nutze /" + getName() + " <Spielername>");
        }
        return true;
    }
}