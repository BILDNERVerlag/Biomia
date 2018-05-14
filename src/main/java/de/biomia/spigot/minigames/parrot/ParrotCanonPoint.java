package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.minigames.GameTeam;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class ParrotCanonPoint {

    public ParrotCanonPoint(Location location, GameTeam team) {
        canon = new ParrotCanon(ParrotCanon.CanonType.CANON, team, this);
        location.subtract(0, 1, 0).getBlock().setType(Material.ENDER_CHEST);
        canon.spawn(1);
        armorStand = (ArmorStand) team.getMode().getInstance().getWorld().spawnEntity(location.add(0, 3, 0), EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setCustomName(ParrotItemNames.canonier);
    }

    private final Location location = null;
    private final ParrotCanon canon;
    private boolean destroyed;
    private final ArmorStand armorStand;

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void setDestroyed() {

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