package de.biomia.spigot.commands.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.minigames.kitpvp.KitPVPManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitPVPCommand extends BiomiaCommand {

    public KitPVPCommand() {
        super("edit", "editkit", "kit", "selectkit", "select");
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {
        KitPVPManager.openSelectorInventory(Biomia.getBiomiaPlayer((Player) sender));
    }
}
