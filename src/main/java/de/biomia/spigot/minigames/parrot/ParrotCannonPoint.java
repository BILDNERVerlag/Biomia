package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.messages.manager.Title;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

class ParrotCannonPoint {

    ParrotCannonPoint(Location location, ParrotTeam team) {
        this.location = location;
        cannon = new ParrotCannon(team, this);
        location.clone().subtract(team.getColor() == TeamColor.RED ? -1 : 1, 1, 0).getBlock().setType(Material.ENDER_CHEST);
        cannon.spawn();
        cannonier = (ArmorStand) team.getMode().getInstance().getWorld().spawnEntity(location.clone().add(1.5, 0, 2.5), EntityType.ARMOR_STAND);
        cannonier.setGravity(false);
        cannonier.setCustomName(ParrotItemNames.cannonier);
        cannonier.setCustomNameVisible(true);
        ((Parrot) team.getMode()).registerPoint(this);
    }

    private final Location location;
    private final ParrotCannon cannon;
    private boolean destroyed;
    private ParrotTeam team;
    private final ArmorStand cannonier;

    public ArmorStand getCannonier() {
        return cannonier;
    }

    public void setDestroyed() {
        destroyed = true;
        cannonier.remove();
        cannon.setTimeToReload(0);
        team.getShip().updateCannons();
        Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitel(String.format("%sEine Kanone von Team %s wurde zerstört", team.getColorcode(), team.getColor().translate()), player));
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public ParrotCannon getcannon() {
        return cannon;
    }

    public Location getLocation() {
        return location;
    }

}