package de.biomia.spigot.configs;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Location;


public class ParrotConifg extends MinigamesConfig {

    public ParrotConifg(GameMode mode) {
        super(mode);
    }

    public static void addShip(Location pos1, Location pos2, TeamColor team) {
        addLocation(pos1, team, "Ship.Pos1", GameType.PARROT);
        addLocation(pos2, team, "Ship.Pos2", GameType.PARROT);
    }

    public CuboidRegion getShipRegion(TeamColor color) {
        Location pos1 = getLocation(color, "Ship.Pos1");
        Location pos2 = getLocation(color, "Ship.Pos2");
        return new CuboidRegion(FaweAPI.getWorld(mode.getInstance().getWorld().getName()), new Vector(pos1.getX(), pos1.getY(), pos1.getZ()), new Vector(pos2.getX(), pos2.getY(), pos2.getZ()));
    }

}
