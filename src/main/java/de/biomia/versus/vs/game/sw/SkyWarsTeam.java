package de.biomia.versus.vs.game.sw;

import de.biomia.versus.vs.game.GameTeam;
import de.biomia.versus.vs.game.TeamColor;
import de.biomia.api.BiomiaPlayer;
import org.bukkit.Location;

import java.util.ArrayList;

class SkyWarsTeam extends GameTeam {

    SkyWarsTeam(SkyWars skyWars, TeamColor color, ArrayList<BiomiaPlayer> player, Location home) {
        super(color, player, home, skyWars);
    }
}