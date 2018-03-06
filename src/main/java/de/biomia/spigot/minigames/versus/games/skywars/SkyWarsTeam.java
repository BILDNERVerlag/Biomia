package de.biomia.spigot.minigames.versus.games.skywars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Location;

import java.util.ArrayList;

class SkyWarsTeam extends GameTeam {

    SkyWarsTeam(SkyWars skyWars, TeamColor color, ArrayList<BiomiaPlayer> player, Location home) {
        super(color, player, home, skyWars);
    }
}