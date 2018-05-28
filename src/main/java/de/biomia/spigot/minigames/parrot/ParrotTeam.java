package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.configs.ParrotConfig;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Location;

public class ParrotTeam extends GameTeam {

    private final ParrotShip ship;

    public ParrotTeam(TeamColor color, GameMode mode) {
        super(color, mode);
        ship = new ParrotShip(((ParrotConfig) getMode().getConfig()).getShipRegion(color), this);
        switch (color) {
            case RED:
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -17, 36, -25), this);
                break;
            case BLUE:
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 62, 36, -25), this);
                break;
        }
        ship.initRegion();
    }

    public ParrotShip getShip() {
        return ship;
    }

}