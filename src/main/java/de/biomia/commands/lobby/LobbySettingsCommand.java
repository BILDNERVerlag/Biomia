package de.biomia.commands.lobby;

import de.biomia.commands.BiomiaCommand;
import de.biomia.messages.BiomiaMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LobbySettingsCommand extends BiomiaCommand {

    public static final ArrayList<Player> targetarmorstands = new ArrayList<>();

    public LobbySettingsCommand() {
        super("destroyarmorstands");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender.hasPermission("biomia.lobbysettings")) {
            if ((sender instanceof Player)) {
                Player p = (Player) sender;
                if (!targetarmorstands.contains(p)) {
                    targetarmorstands.add(p);
                    p.sendMessage(BiomiaMessages.PREFIX + "\u00A7aDu kannst nun ArmorStands auf der Lobby abbauen!");
                    return true;
                } else {
                    targetarmorstands.remove(p);
                    p.sendMessage(
                            BiomiaMessages.PREFIX + "\u00A7cDu kannst nun keine ArmorStands mehr auf der Lobby abbauen!");
                    return true;
                }
            }
        } else {
            sender.sendMessage(BiomiaMessages.NO_PERM);
        }
        return false;
    }

}
