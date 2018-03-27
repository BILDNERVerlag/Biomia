package de.biomia.spigot.tools;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Particles {

    private final PacketPlayOutWorldParticles packet;


    public Particles(EnumParticle particletype, Location location, boolean longdistance, float offsetx, float offsety, float offsetz, float speed, int amount) {
        this.packet = new PacketPlayOutWorldParticles(particletype, longdistance, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetx, offsety, offsetz, speed, amount);
    }

    public void sendAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPlayer(player);
        }
    }

    private void sendPlayer(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}