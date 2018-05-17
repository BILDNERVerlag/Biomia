package de.biomia.spigot.minigames.parrot;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.math.transform.AffineTransform;
import de.biomia.spigot.Main;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.universal.Messages;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class ParrotCanon {

    private CanonType type = CanonType.CANON;
    private int level = 0;
    private final GameTeam team;
    private final ParrotCanonPoint canonPoint;
    private int cooldown;

    private final ArmorStand cooldownArmorStand;
    private final ArmorStand canonNameArmorStand;

    ParrotCanon(GameTeam team, ParrotCanonPoint canonPoint) {
        this.team = team;
        this.canonPoint = canonPoint;

        cooldownArmorStand = (ArmorStand) canonPoint.getLocation().getWorld().spawnEntity(canonPoint.getLocation(), EntityType.ARMOR_STAND);
        canonNameArmorStand = (ArmorStand) canonPoint.getLocation().getWorld().spawnEntity(canonPoint.getLocation(), EntityType.ARMOR_STAND);

        cooldownArmorStand.setGravity(false);
        canonNameArmorStand.setGravity(false);

        cooldownArmorStand.setCustomNameVisible(true);
        canonNameArmorStand.setCustomNameVisible(true);

        setCooldown(0);
        setName();

    }

    public void setType(CanonType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public boolean fire() {
        if (cooldown == 0) {
            //TODO launch TNT

            setCooldown(10); // TODO nachladezeit je nach level
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (cooldown <= 0)
                        cancel();
                    else
                        setCooldown(getCooldown() - 1);
                }
            }.runTaskTimer(Main.getPlugin(), 20, 20);
            return true;
        } else
            return false;
    }

    private void setName() {
        canonNameArmorStand.setCustomName(type.getName());
    }

    private void setCooldown(int cooldown) {
        this.cooldown = cooldown;
        cooldownArmorStand.setCustomName(cooldown == 0 ? "" : String.format("%sCooldown%s: %s%s", Messages.COLOR_MAIN, Messages.COLOR_AUX, Messages.COLOR_SUB, cooldown));
    }

    private int getCooldown() {
        return cooldown;
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
