package de.biomia.spigot.minigames.versus.games.bedwars;

import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class VersusBedWarsTeam extends GameTeam {

    private final ArrayList<Block> bed;
    private boolean hasBed;

    VersusBedWarsTeam(VersusBedWars versusBedWars, TeamColor color) {
        super(color, versusBedWars);
        this.hasBed = true;
        this.bed = ((BedWarsConfig) mode.getConfig()).loadBeds(getBedWars().getInstance(), this);
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

    private VersusBedWars getBedWars() {
        return (VersusBedWars) mode;
    }
}