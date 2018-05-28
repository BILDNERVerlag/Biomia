package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.tools.ItemStackSaver;
import de.biomia.universal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SaveItemCommand extends BiomiaCommand {

    public SaveItemCommand() {
        super("si");
    }

    public void onCommand(CommandSender sender, String label, String[] args) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);
        if (!bp.isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "save":
                ItemStackSaver.saveItemStack(bp.getPlayer().getInventory().getItemInMainHand());
                break;
            case "load":
                ItemStack is;
                try {
                    is = ItemStackSaver.getItemStack(Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    bp.getPlayer().sendMessage("Bitte gib eine ID ein!");
                    break;
                }
                bp.getPlayer().getInventory().addItem(is);
                bp.getPlayer().sendMessage(String.format("%s loaded from DB and added to your inventory.", is.getItemMeta().getDisplayName()));
                break;
            default:
                break;
        }
    }

}
