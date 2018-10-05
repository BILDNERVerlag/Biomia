package de.biomia.spigot.minigames.parrot;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.math.transform.AffineTransform;
import de.biomia.spigot.Main;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.tools.Hologram;
import de.biomia.universal.Messages;
import lombok.Getter;
import lombok.Setter;
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

public class ParrotCannon {

    private CannonType type = CannonType.KANONE;
    private final ParrotTeam team;
    private final ParrotCannonPoint cannonPoint;
    private int timeToReload;
    private final ArmorStand cooldownArmorStand;
    private final ArmorStand cannonNameArmorStand;
    @Setter
    private int actualCooldown = getCooldown();
    @Setter
    private double actualDamage = 2.5;
    /**
     * Between 0 and 10
     */
    @Setter
    private int actualScattering = 0;
    @Setter
    private int actualBullets = getBullets();
    @Setter
    @Getter
    private CannonYaw actualYaw = CannonYaw.STRAIGHT;
    @Setter
    @Getter
    private CannonPitch actualPitch = CannonPitch.MIDDLE;
    @Getter
    private final ParrotCannonInventory.CannonSettingInventory settingInventory;
    @Getter
    private final ParrotCannonInventory.CannonWeaponChangeInventory weaponChangeInventory;
    @Getter
    private final ParrotCannonInventory.CannonDirectionSettingInventory directionSettingInventory;
    @Getter
    private final ParrotCannonInventory.CannonMainInventory mainInventory;

    ParrotCannon(ParrotTeam team, ParrotCannonPoint cannonPoint) {
        this.team = team;
        this.cannonPoint = cannonPoint;

        Location infoLoc;

        if (team.getColor() == TeamColor.RED) infoLoc = cannonPoint.getLocation().clone().add(-.5, .75, .5);
        else infoLoc = cannonPoint.getLocation().clone().add(1.5, .75, .5);

        ArmorStand[] stands = Hologram.newHologram(infoLoc, "", "");
        cannonNameArmorStand = stands[0];
        cooldownArmorStand = stands[1];

        setTimeToReload(0);
        setName();

        directionSettingInventory = new ParrotCannonInventory.CannonDirectionSettingInventory(this);
        mainInventory = new ParrotCannonInventory.CannonMainInventory(this);
        settingInventory = new ParrotCannonInventory.CannonSettingInventory(this);
        weaponChangeInventory = new ParrotCannonInventory.CannonWeaponChangeInventory(this);
    }

    public void setType(CannonType type) {
        this.type = type;
    }

    public void fire() {
        if (timeToReload == 0 && !cannonPoint.isDestroyed()) {

            setTimeToReload(actualCooldown);
            new BukkitRunnable() {
                @Override
                public void run() {
                    setTimeToReload(getTimeToReload() - 1);
                    if (getTimeToReload() <= 0)
                        cancel();
                }
            }.runTaskTimer(Main.getPlugin(), 20, 20);

            int bullets = actualBullets;
            while (bullets != 0) {
                bullets--;
                Vector vector = actualPitch.toVector(team.getColor());

                switch (type) {
                    default:
                    case KANONE:
                        vector.setY(1.05);
                        break;
                    case PANZERFAUST:
                    case HALBAUTOMATIK:
                    case SCHROTFLINTE:
                        vector.setY(0.3);
                        break;
                    case GRANATENWERFER:
                        vector.setY(3);
                        break;
                    //TODO vectoren adden
                }

                vector.add(actualYaw.toVector(team.getColor()));
                vector.add(getActualScatteringVector());

                if (type != CannonType.SCHROTFLINTE) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            shootTNT(vector);
                        }
                    }.runTaskLater(Main.getPlugin(), bullets * 15); // (3/4 sec delay between the shoots)
                } else
                    shootTNT(vector);
            }
        }
    }

    private void shootTNT(Vector vector) {
        TNTPrimed tnt = (TNTPrimed) getShootHole().getWorld().spawnEntity(getShootHole(), EntityType.PRIMED_TNT);
        tnt.setFuseTicks((int) (type.getFuseTicks() * 20));
        tnt.setVelocity(vector);
        tnt.setMetadata("FromCannon", new FixedMetadataValue(Main.getPlugin(), true));
        tnt.setMetadata("Damage", new FixedMetadataValue(Main.getPlugin(), actualDamage));
        tnt.setMetadata("isShotgun", new FixedMetadataValue(Main.getPlugin(), type == CannonType.SCHROTFLINTE));
    }

    public void reset() {
        actualBullets = getBullets();
        actualScattering = 0;
        actualDamage = 1;
        actualCooldown = getCooldown();
        actualYaw = CannonYaw.STRAIGHT;
        actualPitch = CannonPitch.MIDDLE;
    }

    public void spawn() {
        try {
            ClipboardFormat.SCHEMATIC
                    .load(new File(String.format("plugins/WorldEdit/schematics/%s_%s.schematic", type.name(), team.getColor())))
                    .paste(FaweAPI.getWorld(team.getMode().getInstance().getWorld().getName()),
                            BukkitUtil.toVector(cannonPoint.getLocation()),
                            false, false, new AffineTransform());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setName() {
        cannonNameArmorStand.setCustomName(Messages.format(type.getName()));
    }

    void setTimeToReload(int timeToReload) {
        this.timeToReload = timeToReload;
        cooldownArmorStand.setCustomName(cannonPoint.isDestroyed() ? Messages.format(Messages.format("Zerstört")) :
                timeToReload == 0 ? String.format("%sBereit", Messages.COLOR_SUB) :
                        Messages.format("Nachladen: %s", timeToReload));
    }

    private int getTimeToReload() {
        return timeToReload;
    }

    public CannonType getType() {
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

    private Location getShootHole() {
        return cannonPoint.getLocation().clone().add(team.getColor() == TeamColor.BLUE ? 6 : -6, 1, 0);
    }

    public enum CannonType {
        GRANATENWERFER, SCHROTFLINTE, PANZERFAUST, KANONE, HALBAUTOMATIK;

        public String getName() {
            switch (this) {
                default:
                case KANONE:
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

        double getFuseTicks() {
            switch (this) {
                default:
                case KANONE:
                    return 2.7;
            }
        }
    }

    private Vector getActualScatteringVector() {
        return new Vector().setZ((new Random().nextDouble() - .5) * (actualScattering / 10));
    }

    public enum CannonYaw {

        STRONG_LEFT, LEFT, STRAIGHT, RIGHT, STRONG_RIGHT;

        Vector toVector(TeamColor color) {

            double yaw;
            switch (this) {
                case STRONG_LEFT:
                    yaw = -.6;
                    break;
                case LEFT:
                    yaw = -.3;
                    break;
                default:
                case STRAIGHT:
                    yaw = 0;
                    break;
                case RIGHT:
                    yaw = .3;
                    break;
                case STRONG_RIGHT:
                    yaw = .6;
                    break;
            }
            if (color == TeamColor.RED)
                yaw *= -1;
            return new Vector().setZ(yaw);
        }

        public int getSlot() {
            switch (this) {
                case STRONG_LEFT:
                    return 10;
                case LEFT:
                    return 11;
                default:
                case STRAIGHT:
                    return 12;
                case RIGHT:
                    return 13;
                case STRONG_RIGHT:
                    return 14;
            }
        }

        public String getName() {
            switch (this) {
                case STRONG_LEFT:
                    return "Stark Links";
                case LEFT:
                    return "Links";
                case STRAIGHT:
                    return "Gerade";
                case RIGHT:
                    return "Rechts";
                case STRONG_RIGHT:
                    return "Stark Rechts";
            }
            return null;
        }

    }

    public enum CannonPitch {

        SHORT, MIDDLE, LONG;

        Vector toVector(TeamColor color) {

            double pitch;

            switch (this) {
                case SHORT:
                    pitch = 2.2;
                    break;
                default:
                case MIDDLE:
                    pitch = 2.48;
                    break;
                case LONG:
                    pitch = 2.8;
                    break;
            }
            if (color == TeamColor.RED)
                pitch *= -1;
            return new Vector().setX(pitch);
        }

        public int getSlot() {
            switch (this) {
                case LONG:
                    return 7;
                default:
                case MIDDLE:
                    return 16;
                case SHORT:
                    return 25;
            }
        }

        public String getName() {
            switch (this) {
                case SHORT:
                    return "Kurz";
                case MIDDLE:
                    return "Mittel";
                case LONG:
                    return "Lang";
            }
            return null;
        }
    }

    public enum CannonUpgrade {
        FAST_RELOAD, SCATTERING, DAMAGE, BULLET
    }

    public boolean upgrade(CannonUpgrade upgrade) {

        int newValue;

        switch (upgrade) {
            case BULLET:
                switch (type) {
                    default:
                        newValue = 2;
                        break;
                    case HALBAUTOMATIK:
                        newValue = 10;
                        break;
                    case SCHROTFLINTE:
                        newValue = 4;
                        break;
                }
                if (newValue == actualBullets)
                    return false;
                actualBullets = newValue;
                break;
            case DAMAGE:
                //TODO
                switch (type) {
                    default:
                        newValue = 4;
                        break;
                    case HALBAUTOMATIK:
                        newValue = 4;
                        break;
                    case SCHROTFLINTE:
                        newValue = 3;
                        break;
                }
                if (newValue == actualDamage)
                    return false;
                actualDamage = newValue;
                break;
            case SCATTERING:
                //TODO
                switch (actualScattering) {
                    case 0:
                        newValue = 2;
                        break;
                    case 2:
                        newValue = 5;
                        break;
                    case 5:
                        newValue = 8;
                        break;
                    default:
                    case 8:
                        newValue = 10;
                        break;
                }
                if (newValue == actualScattering)
                    return false;
                actualScattering = newValue;
                break;
            case FAST_RELOAD:
                //TODO
                switch (type) {
                    default:
                        newValue = 3;
                        break;
                    case PANZERFAUST:
                    case GRANATENWERFER:
                        newValue = 10;
                        break;
                    case HALBAUTOMATIK:
                        newValue = 5;
                        break;
                }
                if (newValue == actualCooldown)
                    return false;
                actualCooldown = newValue;
                break;
        }
        return true;
    }
}
