package de.biomia.api.Teams;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface Team {

	ArrayList<Player> players = new ArrayList<>();
	
	ArrayList<Player> deadPlayers = new ArrayList<>();

	void initialize(String teamname, int maxPlayer);

	String getTeamname();

	void addPlayer(Player player);

	void removePlayer(Player player);

	int getMaxPlayer();

	short getColordata();

	String getColorcode();

	int getPlayersInTeam();

	boolean playerInThisTeam(Player player);

	boolean full();

	boolean isPlayerDead(Player player);

	void setPlayerDead(Player player);

	ArrayList<Player> getPlayers();

}
