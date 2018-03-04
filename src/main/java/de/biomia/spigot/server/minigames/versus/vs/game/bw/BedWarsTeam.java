package de.biomia.spigot.server.minigames.versus.vs.game.bw;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.BedWarsVersusConfig;
import de.biomia.spigot.server.minigames.versus.vs.game.GameTeam;
import de.biomia.spigot.server.minigames.versus.vs.game.TeamColor;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class BedWarsTeam extends GameTeam {

    private final ArrayList<Block> bed;
    private boolean hasBed;

    BedWarsTeam(BedWars bedWars, TeamColor color, ArrayList<BiomiaPlayer> player, Location home) {
        super(color, player, home, bedWars);
        this.hasBed = true;
        this.bed = BedWarsVersusConfig.getBed(getBedWars().getInstance().getMapID(), color.getID(), bedWars.getInstance().getWorld());
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

    private BedWars getBedWars() {
        return (BedWars) mode;
    }
}