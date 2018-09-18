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
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 30,74,2), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 30,74,20), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 30,70,-16), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 33,70,37), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 30,64,-13), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 29,64,-3), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 28,64,7), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 28,64,17), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 29,64,27), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), 31,64,37), this);
                break;
            case BLUE:
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -38,74,-6), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -38,74,-24), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -38,70,12), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -40,70,-40), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -38,64,9), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -37,64,-1), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -36,64,-11), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -36,64,-21), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -37,64,-31), this);
                new ParrotCannonPoint(new Location(mode.getInstance().getWorld(), -39,64,-41), this);
                break;

        }
        ship.initRegion();
    }

    public ParrotShip getShip() {
        return ship;
    }

}