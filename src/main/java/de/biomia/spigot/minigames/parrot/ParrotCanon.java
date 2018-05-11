package de.biomia.spigot.minigames.parrot;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.math.transform.AffineTransform;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;

import java.io.File;
import java.io.IOException;

public class ParrotCanon {

    private final CanonType type;
    private int level = 0;
    private final GameTeam team;
    private final ParrotCanonPoint canonPoint;

    public ParrotCanon(CanonType type, GameTeam team, ParrotCanonPoint canonPoint) {
        this.team = team;
        this.canonPoint = canonPoint;
        this.type = type;
    }

    public void setLevel(int level) {
        this.level = level;
        spawn();
    }

    public void spawn() {
        try {
            ClipboardFormat.SCHEMATIC.load(new File(type.name() + "_" + level)).paste(FaweAPI.getWorld(team.getMode().getInstance().getWorld().getName()), BukkitUtil.toVector(canonPoint.getLocation()), false, false, new AffineTransform().rotateY(team.getColor() == TeamColor.RED ? 90 : -90));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum CanonType {
        SHOTGUN, BAZOOKA, SNIPER, CANON, AK_HALBAUTOMATIK
    }



}
