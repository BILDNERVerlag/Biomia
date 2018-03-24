package de.biomia.spigot.events.game;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class GameEndEvent extends GameEvent {

    private static final HandlerList handlerList = new HandlerList();

    private ArrayList<BiomiaPlayer> winner = new ArrayList<>();
    private TeamColor winnerTeam = null;


    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public GameEndEvent(GameMode mode) {
        super(mode);
        for (GameTeam teams : getMode().getTeams()) {
            for (BiomiaPlayer bp : teams.getPlayers()) {
                if (teams.lives(bp)) {
                    winner.add(bp);
                    winnerTeam = teams.getColor();
                }
            }
        }
    }

    public TeamColor getWinnerTeam() {
        return winnerTeam;
    }

    public ArrayList<BiomiaPlayer> getWinner() {
        return winner;
    }
}
