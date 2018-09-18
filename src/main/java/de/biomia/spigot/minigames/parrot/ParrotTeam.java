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
    }

    public void init() {
        switch (getColor()) {
            case BLUE:
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -25, 54, -24), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -25, 54, -42), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -28, 50, -59), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -25, 50, -6), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -25, 44, -9), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -24, 44, -19), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -23, 44, -29), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -23, 44, -39), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -24, 44, -49), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), -26, 44, -59), this);
                break;
            case RED:
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 74, 54, -25), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 74, 54, -8), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 74, 50, -44), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 77, 50, -10), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 74, 44, -41), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 73, 44, -31), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 72, 44, -21), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 72, 44, -11), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 73, 44, -1), this);
                new ParrotCannonPoint(new Location(getMode().getInstance().getWorld(), 75, 44, 9), this);
                break;
        }
        ship.initRegion();
    }
    public ParrotShip getShip() {
        return ship;
    }

}