package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.configs.ParrotConifg;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;

public class ParrotTeam extends GameTeam {

    private ParrotShip ship;

    public ParrotTeam(TeamColor color, GameMode mode) {
        super(color, mode);
        ship = new ParrotShip(((ParrotConifg) getMode().getConfig()).getShipRegion(color), this);
    }

    public ParrotShip getShip() {
        return ship;
    }
}
