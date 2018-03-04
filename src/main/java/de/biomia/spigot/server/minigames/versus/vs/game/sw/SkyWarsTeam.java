package de.biomia.spigot.server.minigames.versus.vs.game.sw;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.server.minigames.versus.vs.game.GameTeam;
import de.biomia.spigot.server.minigames.versus.vs.game.TeamColor;
import org.bukkit.Location;

import java.util.ArrayList;

class SkyWarsTeam extends GameTeam {

    SkyWarsTeam(SkyWars skyWars, TeamColor color, ArrayList<BiomiaPlayer> player, Location home) {
        super(color, player, home, skyWars);
    }
}