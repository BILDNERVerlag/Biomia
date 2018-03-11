package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.bedwars.var.Scoreboards;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BedWarsTeam extends GameTeam {

    private final ArrayList<Block> bed;

    BedWarsTeam(TeamColor color, Location loc, GameMode mode) {
        super(color, loc, mode);
        bed = BedWarsConfig.loadBeds(mode.getInstance(), this);
    }

    private boolean hasBed;

    @Override
    public void setDead(BiomiaPlayer bp) {
        super.setDead(bp);

        BedWars.getBedWars().getTeam(bp).setDead(bp);

        // Hide
        for (Player all : Bukkit.getOnlinePlayers()) {
            BiomiaPlayer temp = Biomia.getBiomiaPlayer(all);
            if (BedWars.getBedWars().getTeam(temp).lives(temp))
                all.hidePlayer(bp.getPlayer());
            else
                bp.getPlayer().showPlayer(all);
        }

        // Disable Damage / Build
        bp.setGetDamage(false);
        bp.setDamageEntitys(false);
        bp.setBuild(false);

        bp.getPlayer().setGameMode(org.bukkit.GameMode.SPECTATOR);
        bp.getPlayer().setSilent(true);
        bp.getPlayer().getInventory().clear();

        // Fly settings
        bp.getPlayer().setAllowFlight(true);
        bp.getPlayer().setFlying(true);
        bp.getPlayer().setFlySpeed(0.5F);

        // Scoreboard
        Scoreboards.spectatorSB.getTeam("spectator").addEntry(bp.getName());
        Scoreboards.setSpectatorSB(bp.getPlayer());

        // Check if only one or less Team(s) left
        if (BedWars.getBedWars().canStop()) {
            //TODO renew
            //InGame.end();
            BedWars.getBedWars().stop();
        }


    }

    public void destroyBed() {
        hasBed = false;
    }

    public boolean hasBed() {
        return hasBed;
    }

    public ArrayList<Block> getBed() {
        return bed;
    }

}
