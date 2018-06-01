package de.biomia.spigot.minigames.general;

import de.biomia.spigot.BiomiaPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Dead {

    public static void setDead(BiomiaPlayer target) {
        target.setInBuildmode(false);
        target.setDangerous(false);
        target.setDamageable(false);
        target.getPlayer().setSilent(true);
        target.getPlayer().setGameMode(GameMode.SPECTATOR);
        Scoreboards.setSpectatorSB(target.getPlayer());
    }

    public static void respawn(Player p) {
        ((CraftPlayer) p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
    }
}