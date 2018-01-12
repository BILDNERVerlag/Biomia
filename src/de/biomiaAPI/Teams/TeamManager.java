package de.biomiaAPI.Teams;

import java.util.ArrayList;

import org.bukkit.entity.Player;

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

	boolean isPlayerDead(Player player);

	String translate(String farbe);

}
