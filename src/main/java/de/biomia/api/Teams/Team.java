package de.biomia.api.Teams;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {

    String teamname;
    int maxPlayer;
    short colordata;
    String colorcode;
    final ArrayList<Player> players = new ArrayList<>();
    final ArrayList<Player> deadPlayers = new ArrayList<>();

    public String getTeamname() {
        return teamname;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public short getColordata() {
        return colordata;
    }

    public String getColorcode() {
        return colorcode;
    }

    public int getPlayersInTeam() {
        return players.size();
    }

    public boolean playerInThisTeam(Player player) {

        return players.contains(player);
    }

    public ArrayList<Player> getPlayers() {

        return players;
    }

    public boolean full() {

        return maxPlayer == players.size();
    }

    public void initialize(String teamname, int maxPlayer) {
        short colordata;
        String colorcode;

        switch (teamname) {
            case "BLACK":
                colorcode = "\u00A70";
                colordata = 15;
                break;
            case "BLUE":
                colorcode = "\u00A79";
                colordata = 11;
                break;
            case "ORANGE":
                colorcode = "\u00A76";
                colordata = 1;
                break;
            case "GREEN":
                colorcode = "\u00A72";
                colordata = 13;
                break;
            case "PURPLE":
                colorcode = "\u00A7d";
                colordata = 10;
                break;
            case "RED":
                colorcode = "\u00A7c";
                colordata = 14;
                break;
            case "WHITE":
                colorcode = "\u00A7f";
                colordata = 0;
                break;
            case "YELLOW":
                colorcode = "\u00A7e";
                colordata = 4;
                break;
            default:
                colorcode = "\u00A7f";
                colordata = 0;
                break;
        }

        this.colorcode = colorcode;
        this.colordata = colordata;
        this.maxPlayer = maxPlayer;
        this.teamname = teamname;
    }

    public boolean isPlayerDead(Player player) {
        return deadPlayers.contains(player);
    }

    public void setPlayerDead(Player player) {
        if (!deadPlayers.contains(player)) {
            deadPlayers.add(player);
        }
    }

}

