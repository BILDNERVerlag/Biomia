package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.minigames.GameTeam;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class ParrotCanonPoint {

    public ParrotCanonPoint(Location location, GameTeam team) {
        canon = new ParrotCanon(team, this);
        location.subtract(0, 1, 0).getBlock().setType(Material.ENDER_CHEST);
        canon.spawn(1);
        canonier = (ArmorStand) team.getMode().getInstance().getWorld().spawnEntity(location.add(0, 3, 0), EntityType.ARMOR_STAND);
        canonier.setGravity(false);
        canonier.setCustomName(ParrotItemNames.canonier);
        canonier.setCustomNameVisible(true);
    }

    private final Location location = null;
    private final ParrotCanon canon;
    private boolean destroyed;
    private final ArmorStand canonier;

    public ArmorStand getCanonier() {
        return canonier;
    }

    public void setDestroyed() {
        destroyed = true;
        canonier.remove();
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