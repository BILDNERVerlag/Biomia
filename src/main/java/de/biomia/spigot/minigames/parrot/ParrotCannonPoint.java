package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class ParrotCannonPoint {

    ParrotCannonPoint(Location location, GameTeam team) {
        this.location = location;
        canon = new ParrotCanon(team, this);
        location.clone().subtract(team.getColor() == TeamColor.RED ? -1 : 1, 1, 0).getBlock().setType(Material.ENDER_CHEST);
        canon.spawn();
        canonier = (ArmorStand) team.getMode().getInstance().getWorld().spawnEntity(location.clone().add(1.5, 0, 2.5), EntityType.ARMOR_STAND);
        canonier.setGravity(false);
        canonier.setCustomName(ParrotItemNames.canonier);
        canonier.setCustomNameVisible(true);
        ((Parrot) team.getMode()).registerPoint(this);
    }

    private Location location;
    private final ParrotCanon canon;
    private boolean destroyed;
    private final ArmorStand canonier;

    public ArmorStand getCanonier() {
        return canonier;
    }

    public void setDestroyed() {
        destroyed = true;
        canonier.remove();
        canon.setTimeToReload(0);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public ParrotCanon getCanon() {
        return canon;
    }

    public Location getLocation() {
        return location;
    }

}