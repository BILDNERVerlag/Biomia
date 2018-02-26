package de.biomia.versus.bw.var;

import de.biomia.versus.vs.game.TeamColor;
import de.biomia.versus.vs.game.bw.BedWars;
import de.biomia.api.BiomiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class BedWarsScoreboard {

    private final BedWars bedWars;
    private final Scoreboard sb;

    public BedWarsScoreboard(BedWars bedWars) {
        this.bedWars = bedWars;
        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        initScoreboard();
    }

    public void setScoreboard(BiomiaPlayer bp, boolean spectator) {
        String team;
        if (spectator)
            team = "spectator";
        else
            team = bedWars.getTeam(bp).getColor().name();
        sb.getTeam(team).addEntry(bp.getPlayer().getName());
    }

    private void initScoreboard() {
        for (TeamColor colors : TeamColor.values())
            sb.registerNewTeam(colors.name()).setPrefix(colors.getColorcode());
        sb.registerNewTeam("spectator").setPrefix("00A7700A7o");
    }
}
