package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Checkpoint extends BiomiaCommand {

    Checkpoint() {
        super("checkpoint", "cp");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("set")) {
                SchnitzelListener.getCheckPoints().put(bp, bp.getPlayer().getLocation());

                bp.sendMessage("§cCheckpoint manuel gespeicher.");
                bp.sendMessage("§cCheckpoint wird nun nicht mehr automatisch gespeichert!");
                bp.sendMessage("§cFür automatisches speichern Reconnecten!");
            } else {
                bp.sendMessage("§c/checkpoint §7<§bset§7>");
            }
        } else {
            bp.sendMessage("§cZu Checkpoint teleportiert!");
            bp.getPlayer().teleport(SchnitzelListener.getCheckPoints().get(bp));
        }
        return true;
    }
}
