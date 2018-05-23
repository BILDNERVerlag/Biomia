package de.biomia.spigot.minigames.parrot;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.math.transform.AffineTransform;
import de.biomia.spigot.Main;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.tools.Hologram;
import de.biomia.universal.Messages;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ParrotCanon {

    private CanonType type = CanonType.CANON;
    private final GameTeam team;
    private final ParrotCanonPoint canonPoint;
    private int timeToReload;
    private final ArmorStand cooldownArmorStand;
    private final ArmorStand canonNameArmorStand;

    private int actualCooldown = getCooldown();
    private int actualDamage = 1;
    /**
     * Between 0 and 1
     */
    private int actualScattering = 0;
    private int actualBullets = getBullets();
    private CannonYaw actualYaw = CannonYaw.STRAIGHT;
    private CannonPitch actualPitch = CannonPitch.MIDDLE;

    ParrotCanon(GameTeam team, ParrotCanonPoint canonPoint) {
        this.team = team;
        this.canonPoint = canonPoint;

        ArmorStand[] stands = Hologram.newHologram(canonPoint.getLocation().clone().add(.5, -.5, .5), "", "");
        canonNameArmorStand = stands[0];
        cooldownArmorStand = stands[1];

        setTimeToReload(0);
        setName();
    }

    public void setType(CanonType type) {
        this.type = type;
    }

    public void fire() {
        if (timeToReload == 0 && !canonPoint.isDestroyed()) {

            int bullets = actualBullets;

            while (bullets != 0) {

                Vector vector;
                switch (type) {
                    default:
                    case CANON:
                        vector = new Vector().setY(1.05);
                        break;
                    //TODO vectoren adden
                }

                vector.add(getActualScatteringVector());
                vector.add(actualYaw.toVector(team.getColor()));
                vector.add(actualPitch.toVector(team.getColor()));

                if (type == CanonType.HALBAUTOMATIK) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            shootTNT(vector);
                        }
                    }.runTaskLater(Main.getPlugin(), bullets * 15); // (3/4 sec delay between the shoots)
                } else
                    shootTNT(vector);
                bullets--;
            }

            setTimeToReload(actualCooldown);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (timeToReload <= 0)
                        cancel();
                    else
                        setTimeToReload(getTimeToReload() - 1);
                }
            } .runTaskTimer(Main.getPlugin(), 20, 20);
        }
    }

    private void shootTNT(Vector vector) {
        TNTPrimed tnt = (TNTPrimed) getShootHole().getWorld().spawnEntity(getShootHole(), EntityType.PRIMED_TNT);
        tnt.setFuseTicks(type.getFuseTicks());
        tnt.setVelocity(vector);
        tnt.setMetadata("Damage", new FixedMetadataValue(Main.getPlugin(), actualDamage));
        tnt.setMetadata("FromCannon", new FixedMetadataValue(Main.getPlugin(), true));
        tnt.setMetadata("isShotgun", new FixedMetadataValue(Main.getPlugin(), type == CanonType.SCHROTFLINTE));
    }

    public void spawn() {
        try {
            ClipboardFormat.SCHEMATIC
                    .load(new File(String.format("plugins/WorldEdit/schematics/%s_%s.schematic", type.name(), team.getColor())))
                    .paste(FaweAPI.getWorld(team.getMode().getInstance().getWorld().getName()),
                            BukkitUtil.toVector(canonPoint.getLocation()), false, false,
                            new AffineTransform().rotateY(team.getColor() == TeamColor.RED ? -90 : 90));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setName() {
        canonNameArmorStand.setCustomName(String.format("%s%s", Messages.COLOR_MAIN, type.getName()));
    }

    private void setTimeToReload(int timeToReload) {
        this.timeToReload = timeToReload;
        cooldownArmorStand.setCustomName(timeToReload == 0 ? String.format("%sBereit", Messages.COLOR_SUB) : String.format("%sNachladen%s: %s%s", Messages.COLOR_MAIN, Messages.COLOR_AUX, Messages.COLOR_SUB, timeToReload));
    }

    private int getTimeToReload() {
        return timeToReload;
    }

    public CanonType getType() {
        return type;
    }

    private int getBullets() {
        switch (type) {
            default:
                return 1;
            case HALBAUTOMATIK:
                return 3;
            case SCHROTFLINTE:
                return 8;
        }
    }

    private int getCooldown() {
        switch (type) {
            default:
                return 5;
        }
    }

    public Location getButton() {

        //aus sicht von blau
        int x = 0, y = 0, z = 0;

        switch (type) {
            case CANON:
                x = -2;
                z = 1;
                y = 1;
                break;
            case PANZERFAUST:
                break;
            case SCHROTFLINTE:
                break;
            case GRANATENWERFER:
                break;
            case HALBAUTOMATIK:
                break;
        }

        if (team.getColor() == TeamColor.RED) {
            x *= -1;
            z *= -1;
        }

        return canonPoint.getLocation().clone().add(x, y, z);
    }

    private Location getShootHole() {

        //aus sicht von blau
        int x = 0, y = 0, z = 0;

        switch (type) {
            case CANON:
                x = -7;
                y = 1;
                break;
            case PANZERFAUST:
                break;
            case SCHROTFLINTE:
                break;
            case GRANATENWERFER:
                break;
            case HALBAUTOMATIK:
                break;
        }

        if (team.getColor() == TeamColor.RED) {
            x *= -1;
            z *= -1;
        }

        return canonPoint.getLocation().clone().add(x, y, z);

    }

    public enum CanonType {
        GRANATENWERFER, SCHROTFLINTE, PANZERFAUST, CANON, HALBAUTOMATIK;

        public String getName() {
            switch (this) {
                default:
                case CANON:
                    return "6-Pfünder";
                case PANZERFAUST:
                    return "12-Pfünder";
                case SCHROTFLINTE:
                    return "Bombarde";
                case GRANATENWERFER:
                    return "Mörser";
                case HALBAUTOMATIK:
                    return "Drillings-Kanone";
            }
        }

        public int getFuseTicks() {
            switch (this) {
                default:
                    return 3;
            }
        }
    }

    public enum CannonYaw {

        STRONG_LEFT, LEFT, STRAIGHT, RIGHT, STRONG_RIGHT;

        public Vector toVector(TeamColor color) {

            double yaw;
            switch (this) {
                case STRONG_LEFT:
                    yaw = .6;
                    break;
                case LEFT:
                    yaw = .3;
                    break;
                default:
                case STRAIGHT:
                    yaw = 0;
                    break;
                case RIGHT:
                    yaw = -.3;
                    break;
                case STRONG_RIGHT:
                    yaw = -.6;
                    break;
            }
            if (color == TeamColor.RED) {
                yaw *= -1;
            }
            return new Vector().setZ(yaw);
        }
    }

    private Vector getActualScatteringVector() {
        return new Vector().setZ((new Random().nextDouble() - .5) / 1.6 * actualScattering);
    }

    public enum CannonPitch {

        SHORT, MIDDLE, LONG;

        public Vector toVector(TeamColor color) {

            double pitch;

            switch (this) {
                case SHORT:
                    pitch = 2.1;
                    break;
                default:
                case MIDDLE:
                    pitch = 2.45;
                    break;
                case LONG:
                    pitch = 2.8;
                    break;
            }
            if (color == TeamColor.RED) {
                pitch *= -1;
            }
            return new Vector().setX(pitch);
        }
    }
}
