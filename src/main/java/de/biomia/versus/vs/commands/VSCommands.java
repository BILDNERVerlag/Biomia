package de.biomia.versus.vs.commands;

import de.biomia.versus.vs.main.VSMain;
import de.biomia.versus.vs.settings.VSRequest;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.msg.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VSCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

            if (args.length == 0 && cmd.getName().equalsIgnoreCase("spawn")) {
                VSMain.getManager().moveToLobby(p);
            } else if (args.length == 1) {
                String player = args[0];
                Player parg = Bukkit.getPlayer(player);
                if (parg != null) {
                    BiomiaPlayer bparg = Biomia.getBiomiaPlayer(parg);
                    if (VSRequest.hasRequestSended(bparg, bp)) {
                        VSRequest request = VSRequest.getRequest(bparg);
                        switch (cmd.getName()) {
                            case "accept":
                                assert request != null;
                                request.accept();
                                break;
                            case "decline":
                                assert request != null;
                                request.decline();
                                break;
                            default:
                                break;
                        }
                    } else {
                        switch (cmd.getName()) {
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
                sender.sendMessage("Bitte nutze /" + cmd.getName() + " <Spielername>");
        }
        return true;
    }
}