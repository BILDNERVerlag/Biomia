package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.configs.ParrotConfig;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Location;

public class ParrotTeam extends GameTeam {

    private ParrotShip ship;

    public ParrotTeam(TeamColor color, GameMode mode) {
        super(color, mode);
        ship = new ParrotShip(((ParrotConfig) getMode().getConfig()).getShipRegion(color), this);

        switch (color) {
            case RED:
                new ParrotCanonPoint(new Location(mode.getInstance().getWorld(), -16, 36, -25), this);
                break;
            case BLUE:
                new ParrotCanonPoint(new Location(mode.getInstance().getWorld(), 61, 36, -25), this);
                break;
        }

    }

    public ParrotShip getShip() {
        return ship;
    }

}