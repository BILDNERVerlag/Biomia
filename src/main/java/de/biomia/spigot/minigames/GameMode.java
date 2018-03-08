package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.versus.Versus;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameMode {

    private final GameInstance instance;
    private final ArrayList<BiomiaPlayer> players;
    protected GameHandler handler;
    private ArrayList<GameTeam> teams = new ArrayList<>();
    private int teamSize;

    protected GameMode(GameInstance instance) {
        this.instance = instance;
        this.players = instance.getPlayers();

    }

    public GameTeam getTeamFromData(short data) {
        for (GameTeam te : teams) {
            if (te.getColordata() == data) {
                return te;
            }
        }
        return null;
    }

    public int getTeamSize() {
        return teamSize;
    }

    protected static HashMap<Integer, ArrayList<BiomiaPlayer>> splitPlayersInTwoTeams(ArrayList<BiomiaPlayer> players) {

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

    public void registerTeam(GameTeam team) {
        teams.add(team);
    }

    public abstract void start();

    public void stop() {
        players.forEach(each -> ((Versus) Biomia.getSeverInstance()).getManager().moveToLobby(each.getPlayer()));

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
        int teamsWhoLive = 0;
        for (GameTeam team : getTeams()) {
            for (BiomiaPlayer players : team.getPlayers()) {
                if (team.lives(players)) {
                    teamsWhoLive++;
                    break;
                }
            }
        }
        return teamsWhoLive <= 1;
    }

    public ArrayList<GameTeam> getTeams() {
        return teams;
    }

    public GameTeam getTeam(BiomiaPlayer bp) {
        for (GameTeam team : getTeams()) {
            if (team.containsPlayer(bp)) {
                return team;
            }
        }
        return null;
    }

}
