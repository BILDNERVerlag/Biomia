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

    private CanonType type;
    private int level = 0;
    private final GameTeam team;
    private final ParrotCanonPoint canonPoint;
    private long lastShoot;

    ParrotCanon(CanonType type, GameTeam team, ParrotCanonPoint canonPoint) {
        this.team = team;
        this.canonPoint = canonPoint;
        this.type = type;
    }

    public void setType(CanonType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public boolean fire() {

        long now = System.currentTimeMillis();
        if (now - lastShoot > 10 * 1000) { // TODO nachladezeit je nach level
            lastShoot = now;
            //TODO
        }
        return true;
    }

    public void spawn(int level) {
        this.level = level;
        try {
            ClipboardFormat.SCHEMATIC.load(new File(type.name() + "_" + level)).paste(FaweAPI.getWorld(team.getMode().getInstance().getWorld().getName()), BukkitUtil.toVector(canonPoint.getLocation()), false, false, new AffineTransform().rotateY(team.getColor() == TeamColor.RED ? 90 : -90));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CanonType getType() {
        return type;
    }

    public enum CanonType {
        SHOTGUN, BAZOOKA, SNIPER, CANON, AK_HALBAUTOMATIK;


        public String getName() {
            //TODO
            return "Text comes here";
        }

    }



}
