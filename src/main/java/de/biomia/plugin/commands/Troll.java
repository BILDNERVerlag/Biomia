package de.biomia.plugin.commands;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.msg.Messages;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutExplosion;
import net.minecraft.server.v1_12_R1.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Troll implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

            if (cmd.getName().equalsIgnoreCase("troll")) {
                if (sender.hasPermission("biomia.trollmod")) {
                    if (bp.isInTrollmode()) {
                        bp.setTrollmode(false);
                        sender.sendMessage("§cDu bist nun nicht mehr im Troll Modus");
                    } else {
                        bp.setTrollmode(true);
                        sender.sendMessage("§aDu bist nun im Troll Modus");
                    }
                } else {
                    sender.sendMessage(Messages.noperm);
                }
            } else if (cmd.getName().equalsIgnoreCase("crash")) {
                if (args.length >= 1) {
                    Player target = Bukkit.getPlayer(args[0]);

                    if (bp.isInTrollmode()) {
                        crashPlayer(target);
                        sender.sendMessage("§aDu hast den Spieler§c " + target.getName() + " §agecrashed!");
                    } else {
                        sender.sendMessage("§cDu bist nicht im Troll Modus!");
                    }
                }
            }

        } else {
            sender.sendMessage("noplayer");
        }
        return false;
    }

    private void crashPlayer(Player p) {
        EntityPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
        PacketPlayOutExplosion packet = new PacketPlayOutExplosion(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE,
                Float.MAX_VALUE, Collections.emptyList(),
                new Vec3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
        nmsPlayer.playerConnection.sendPacket(packet);
    }

}
