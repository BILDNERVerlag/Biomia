package de.biomiaAPI.Teams;

import org.bukkit.entity.Player;

import java.util.ArrayList;

@SuppressWarnings("SameReturnValue")
public interface TeamManager {

	ArrayList<Team> allteams = new ArrayList<>();

	void initTeams(int playerPerTeam, int teams);
	
	Team registerNewTeam(String s, int maxPlayer);

	Team getTeam(String team);

	Team getTeam(Player player);

	boolean isPlayerInAnyTeam(Player player);

	ArrayList<Team> getTeams();

	Team DataToTeam(short data);

    boolean livesPlayer(Player player);

	String translate(String farbe);

}
