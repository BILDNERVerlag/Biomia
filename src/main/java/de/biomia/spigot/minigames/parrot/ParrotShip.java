package de.biomia.spigot.minigames.parrot;

import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.ArrayList;
import java.util.Collections;

public class ParrotShip {

    private final ArrayList<ParrotCannonPoint> shipPoints = new ArrayList<>();
    private static int shipBlocks;
    private final CuboidRegion region;
    private final BossBar bossBar;
    private final GameTeam team;

    private final EditSession session;

    ParrotShip(CuboidRegion region, GameTeam team) {
        this.region = region;
        this.team = team;
        session = new EditSessionBuilder(team.getMode().getInstance().getWorld().getName()).fastmode(true).build();
        this.bossBar = Bukkit.createBossBar("", team.getColor() == TeamColor.RED ? BarColor.RED : BarColor.BLUE, BarStyle.SEGMENTED_20); // in 5% steps
        bossBar.setProgress(1);
    }

    public void initRegion() {
        shipBlocks = region.getArea() - session.countBlock(region, Collections.singleton(0));
        setName();
        bossBar.setVisible(true);
    }

    private void setName() {
        bossBar.setTitle(String.format("%s%s%% %s|%s %d/%d Kanonen",
                team.getColor().getColorcode(),
                String.valueOf((int) (bossBar.getProgress() * 10000) / 100D),
                Messages.COLOR_AUX,
                team.getColor().getColorcode(),
                getShipPoints().size() - getDestroyedCannons(),
                getShipPoints().size()));
    }

    public void setPlayersToBossBar() {
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void update() {
        int actualBlocks = region.getArea() - session.countBlock(region, Collections.singleton(0));
        int destroyedBlocks = shipBlocks - actualBlocks;

        if (destroyedBlocks > shipBlocks * 0.6D) {
            bossBar.setProgress(0);
            setName();
            team.killAll();
        } else if (destroyedBlocks >= 0) {
            // destroyedBlocks / 0.6 to set the destroyed blocks from 60% to 100%
            // 1 - x to reverse the bar | 0 = destroyed | 1 = not-destroyed
            bossBar.setProgress(1 - destroyedBlocks / 0.6D / shipBlocks);
            setName();
        }
    }

    public boolean containsRegionLocation(Location location) {
        return region.contains(BukkitUtil.toVector(location));
    }

    private ArrayList<ParrotCannonPoint> getShipPoints() {
        return shipPoints;
    }

    private int getDestroyedCannons() {
        int destroyed = 0;
        for (ParrotCannonPoint point : shipPoints) {
            if (point.isDestroyed())
                destroyed++;
        }
        return destroyed;
    }

    void updateCannons() {
        if (getDestroyedCannons() == shipPoints.size()) {
            team.killAll();
            team.getMode().stop();
        }
    }

    public void registerPoint(ParrotCannonPoint parrotCannonPoint) {
        shipPoints.add(parrotCannonPoint);
    }
}