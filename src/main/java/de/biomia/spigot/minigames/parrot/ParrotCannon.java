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

    private CannonType type = CannonType.CANNON;
    private final GameTeam team;
    private final ParrotCannonPoint cannonPoint;
    private int timeToReload;
    private final ArmorStand cooldownArmorStand;
    private final ArmorStand cannonNameArmorStand;
    @Getter
    private final ParrotCannonInventory.CannonDirectionSettingInventory directionSettingInventory;
    @Getter
    private final ParrotCannonInventory.CannonMainInventory mainInventory;
    @Getter
    private final ParrotCannonInventory.CannonSettingInventory settingInventory;
    @Getter
    private final ParrotCannonInventory.CannonWeaponChangeInventory weaponChangeInventory;
    @Setter
    private int actualCooldown = getCooldown();
    @Setter
    private int actualDamage = 5;
    /**
     * Between 0 and 1
     */
    @Setter
    private double actualScattering = 0;
    @Setter
    private int actualBullets = getBullets();
    @Setter
    @Getter
    private CannonYaw actualYaw = CannonYaw.STRAIGHT;
    @Setter
    @Getter
    private CannonPitch actualPitch = CannonPitch.MIDDLE;

    ParrotCannon(GameTeam team, ParrotCannonPoint cannonPoint) {
        this.team = team;
        this.cannonPoint = cannonPoint;

        ArmorStand[] stands = Hologram.newHologram(cannonPoint.getLocation().clone().add(.5, .5, .5), "", "");
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
                    if (timeToReload <= 0)
                        cancel();
                    else
                        setTimeToReload(getTimeToReload() - 1);
                }
            }.runTaskTimer(Main.getPlugin(), 20, 20);

            int bullets = actualBullets;
            while (bullets != 0) {
                bullets--;
                Vector vector = actualPitch.toVector(team.getColor());

                switch (type) {
                    default:
                    case CANNON:
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
                            BukkitUtil.toVector(cannonPoint.getLocation()), false, false,
                            new AffineTransform().rotateY(team.getColor() == TeamColor.RED ? -90 : 90));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setName() {
        cannonNameArmorStand.setCustomName(String.format("%s%s", Messages.COLOR_MAIN, type.getName()));
    }

    void setTimeToReload(int timeToReload) {
        this.timeToReload = timeToReload;
        cooldownArmorStand.setCustomName(cannonPoint.isDestroyed() ? String.format("%sZerst�rt", Messages.COLOR_MAIN) :
                timeToReload == 0 ? String.format("%sBereit", Messages.COLOR_SUB) :
                        String.format("%sNachladen%s: %s%s", Messages.COLOR_MAIN, Messages.COLOR_AUX, Messages.COLOR_SUB, timeToReload));
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

    public Location getButton() {

        //aus sicht von blau
        int x = 0, y = 0, z = 0;

        switch (type) {
            case CANNON:
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

        return cannonPoint.getLocation().clone().add(x, y, z);
    }

    private Location getShootHole() {

        //aus sicht von blau
        int x = 0, y = 0, z = 0;

        switch (type) {
            case CANNON:
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

        return cannonPoint.getLocation().clone().add(x, y, z);

    }

    public enum CannonType {
        GRANATENWERFER, SCHROTFLINTE, PANZERFAUST, CANNON, HALBAUTOMATIK;

        public String getName() {
            switch (this) {
                default:
                case CANNON:
                    return "6-Pf�nder";
                case PANZERFAUST:
                    return "12-Pf�nder";
                case SCHROTFLINTE:
                    return "Bombarde";
                case GRANATENWERFER:
                    return "M�rser";
                case HALBAUTOMATIK:
                    return "Drillings-Kanone";
            }
        }

        public double getFuseTicks() {
            switch (this) {
                default:
                case CANNON:
                    return 2.7;
            }
        }
    }

    private Vector getActualScatteringVector() {
        return new Vector().setZ((new Random().nextDouble() - .5) / 1.6 * actualScattering);
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
            if (color == TeamColor.RED) yaw *= -1;
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

    }

    public enum CannonPitch {

        SHORT, MIDDLE, LONG;

        public Vector toVector(TeamColor color) {

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
            if (color == TeamColor.BLUE) pitch *= -1;
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
                        newValue = 5;
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
                switch ((int) (actualScattering * 10)) {
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