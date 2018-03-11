package de.biomia.spigot.minigames.versus.games.skywars;

import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Location;

class SkyWarsTeam extends GameTeam {

    SkyWarsTeam(VersusSkyWars versusSkyWars, TeamColor color, Location home) {
        super(color, home, versusSkyWars);
    }
}