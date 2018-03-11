package de.biomia.spigot.minigames.versus.games.bedwars;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class BedWarsScoreboard {

    private final VersusBedWars versusBedWars;
    private final Scoreboard sb;

    BedWarsScoreboard(VersusBedWars versusBedWars) {
        this.versusBedWars = versusBedWars;
        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        initScoreboard();
    }

    public void setScoreboard(BiomiaPlayer bp, boolean spectator) {
        String team;
        if (spectator)
            team = "spectator";
        else
            team = versusBedWars.getTeam(bp).getColor().name();
        sb.getTeam(team).addEntry(bp.getPlayer().getName());
    }

    private void initScoreboard() {
        for (TeamColor colors : TeamColor.values())
            sb.registerNewTeam(colors.name()).setPrefix(colors.getColorcode());
        sb.registerNewTeam("spectator").setPrefix("\u00A77\u00A7o");
    }
}
