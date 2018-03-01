package de.biomia.server.minigames.versus.vs.game;

import de.biomia.BiomiaPlayer;
import de.biomia.server.minigames.versus.VSMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameMode {

    private final GameInstance instance;
    private final ArrayList<BiomiaPlayer> players;
    protected GameTeam team1;
    protected GameTeam team2;
    protected GameHandler handler;
    private ArrayList<GameTeam> teams;

    protected GameMode(GameInstance instance) {
        this.instance = instance;
        this.players = instance.getPlayers();
    }

    protected static HashMap<Integer, ArrayList<BiomiaPlayer>> splitPlayers(ArrayList<BiomiaPlayer> players) {

        HashMap<Integer, ArrayList<BiomiaPlayer>> map = new HashMap<>();

        ArrayList<BiomiaPlayer> team1 = new ArrayList<>();
        ArrayList<BiomiaPlayer> team2 = new ArrayList<>();
        map.put(1, team1);
        map.put(2, team2);

        boolean b = true;
        for (BiomiaPlayer bp : players) {
            if (b)
                team1.add(bp);
            else
                team2.add(bp);
            b = !b;
        }
        return map;
    }

    public void start() {
    }

    public void stop() {
        players.forEach(each -> VSMain.getManager().moveToLobby(each.getPlayer()));
        instance.getRequest().finish();
        handler.unregister();
        instance.startDeleting();
    }

    public GameInstance getInstance() {
        return instance;
    }

    public ArrayList<BiomiaPlayer> getPlayers() {
        return players;
    }

    public boolean containsPlayer(BiomiaPlayer bp) {
        return getPlayers().contains(bp);
    }

    public boolean canStop() {

        boolean isDead = true;

        for (BiomiaPlayer bp : team1.getPlayers())
            if (team1.lives(bp)) {
                isDead = false;
                break;
            }
        for (BiomiaPlayer bp : team2.getPlayers())
            if (team2.lives(bp))
                return isDead;
        return true;
    }

    public ArrayList<GameTeam> getTeams() {
        return teams != null ? teams : (teams = new ArrayList<>(Arrays.asList(team1, team2)));
    }

    public GameTeam getTeam(BiomiaPlayer bp) {
        return containsPlayer(bp) ? (team1.containsPlayer(bp) ? team1 : team2) : null;
    }

}
