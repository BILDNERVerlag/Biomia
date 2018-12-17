package de.biomia.spigot.quests;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class QuestCommands extends BiomiaCommand {


    QuestCommands() {
        super("q");
    }

    public void onCommand(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            String[] split = args[0].split("_");
            UUID uuid;
            int index;
            try {
                uuid = UUID.fromString(split[0]);
                index = Integer.parseInt(split[1]);
            } catch (Exception ignored) {
                return;
            }
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);
            bp.getDnc().execute(bp, index, uuid);
        }
    }
}
