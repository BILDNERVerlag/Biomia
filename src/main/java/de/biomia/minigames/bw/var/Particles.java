package de.biomia.minigames.bw.var;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Particles {

    private final EnumParticle particletype;
    private final boolean longdistance;
    private final Location location;
    private final float offsetx;
    private final float offsety;
    private final float offsetz;
    private final float speed;
    private final int amount;


    public Particles(EnumParticle particletype, Location location, boolean longdistance, float offsetx, float offsety, float offsetz, float speed, int amount) {
        this.particletype = particletype;
        this.location = location;
        this.longdistance = longdistance;
        this.offsetx = offsetx;
        this.offsety = offsety;
        this.offsetz = offsetz;
        this.speed = speed;
        this.amount = amount;
    }

    public void sendAll() {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particletype, this.longdistance, (float) this.location.getX(), (float) this.location.getY(), (float) this.location.getZ(), this.offsetx, this.offsety, this.offsetz, this.speed, this.amount, 0);

        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

        }
    }

    public void sendPlayer(Player player) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particletype, this.longdistance, (float) this.location.getX(), (float) this.location.getY(), (float) this.location.getZ(), this.offsetx, this.offsety, this.offsetz, this.speed, this.amount);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
