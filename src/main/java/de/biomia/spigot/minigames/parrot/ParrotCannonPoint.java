package de.biomia.spigot.minigames.parrot;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.math.transform.AffineTransform;
import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.messages.manager.Title;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

class ParrotCannonPoint {

    ParrotCannonPoint(Location location, ParrotTeam team) {
        this.location = location;
        this.team = team;
        cannon = new ParrotCannon(team, this);
        cannon.spawn();

        gunner = spawnCanonier();

        team.getShip().registerPoint(this);
        ((Parrot) team.getMode()).registerPoint(this);
    }

    private final Location location;
    private final ParrotCannon cannon;
    private boolean destroyed;
    private ParrotTeam team;
    private final ArmorStand gunner;

    public ArmorStand getGunner() {
        return gunner;
    }

    public ArmorStand spawnCanonier() {
        //spawn canoniers
        World world = team.getMode().getInstance().getWorld();
        Location l = location.clone();
        try {
            if (team.getColor() == TeamColor.RED)
                ClipboardFormat.SCHEMATIC
                        .load(new File("plugins/WorldEdit/schematics/bkanoniero.schematic"))
                        .paste(FaweAPI.getWorld(world.getName()),
                                BukkitUtil.toVector(l.add(.5, 0, 2.5)),
                                false, false, new AffineTransform());
            else
                ClipboardFormat.SCHEMATIC
                        .load(new File("plugins/WorldEdit/schematics/bkanonierw.schematic"))
                        .paste(FaweAPI.getWorld(world.getName()),
                                BukkitUtil.toVector(l.add(.5, 0, -1.5)),
                                false, false, new AffineTransform());
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        ArmorStand g = (ArmorStand) world.getNearbyEntities(l, 1, 1, 1).stream().findFirst().orElse(null);
        if(g == null) throw new NullPointerException();
        g.setGravity(false);
        g.setCustomName(ParrotItemNames.cannonier);
        g.setCustomNameVisible(true);
        return g;

    }

    public void setDestroyed() {
        destroyed = true;
        gunner.remove();
        cannon.setTimeToReload(0);
        team.getShip().updateCannons();
        Bukkit.getOnlinePlayers().forEach(player -> {
            Title.sendTitel("Aye-Aye, Captain!", player);
            Title.sendSubTitel(String.format("%sEine Kanone von Team %s wurde zerstört", team.getColorcode(), team.getColor().translate()), player);
        });
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public ParrotCannon getCannon() {
        return cannon;
    }

    public ParrotTeam getTeam() {
        return team;
    }

    public Location getLocation() {
        return location;
    }

    Location getButtonLocation() {
        if (team.getColor() == TeamColor.BLUE)
            return location.clone().add(1.5, 0.5, .5);
        else
            return location.clone().add(-0.5, 0.5, .5);
    }

}