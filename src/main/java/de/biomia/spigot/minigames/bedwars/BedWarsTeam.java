package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class BedWarsTeam extends GameTeam {

    private final ArrayList<Block> bed;

    public BedWarsTeam(TeamColor color, GameMode mode) {
        super(color, mode);
        bed = ((BedWarsConfig) mode.getConfig()).loadBeds(mode.getInstance(), this);
    }

    private boolean hasBed = true;

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
