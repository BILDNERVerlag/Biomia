package de.biomiaAPI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.Quests.DialogMessage;
import de.biomiaAPI.Quests.Quest;
import de.biomiaAPI.Quests.QuestManager;
import de.biomiaAPI.Quests.QuestPlayer;
import de.biomiaAPI.Quests.States;
import de.biomiaAPI.Quests.TIME;
import de.biomiaAPI.Teams.Team;
import de.biomiaAPI.Teams.TeamManager;
import de.biomiaAPI.Teams.Teams;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.mysql.MySQL;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;

public class Biomia {

	private static HashMap<Player, QuestPlayer> qp = new HashMap<>();
	private static HashMap<Player, BiomiaPlayer> bp = new HashMap<>();

	public static void stopWithDelay(int sekunden) {

		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.shutdown();
			}
		}.runTaskLater(Main.plugin, sekunden * 20);
	}
	
	private static void removeBiomiaPlayer(Player p) {
		getBiomiaPlayer(p);
		bp.remove(p);
	}

	private static void removeQuestPlayer(Player p) {
		getQuestPlayer(p);
		qp.remove(p);
	}

	public static void removePlayers(Player p) {
		removeBiomiaPlayer(p);
		removeQuestPlayer(p);
	}

	public static QuestPlayer getQuestPlayer(Player p) {

		QuestPlayer questP = qp.get(p);

		if (questP != null) {
			return questP;
		} else {
			QuestPlayer newqp = new QuestPlayer(p);
			qp.put(newqp.getPlayer(), newqp);
			return newqp;
		}

	}

	public static QuestPlayer getQuestPlayer(BiomiaPlayer bp) {

		Player p = bp.getPlayer();

		if (qp.containsKey(p)) {
			return qp.get(p);
		} else {
			QuestPlayer newqp = new QuestPlayer(p);
			qp.put(newqp.getPlayer(), newqp);
			return newqp;
		}
	}

	public static BiomiaPlayer getBiomiaPlayer(Player p) {

		if (bp.containsKey(p)) {
			return bp.get(p);
		} else {
			BiomiaPlayer newbp = new BiomiaPlayer(p);
			bp.put(newbp.getPlayer(), newbp);
			return newbp;
		}
	}

	public static TeamManager TeamManager() {
		return new TeamManager() {
			
			@Override
			public void initTeams(int playerPerTeam, int teams) {

				switch (teams) {
				case 2:
					registerNewTeam(Teams.BLUE.name(), playerPerTeam);
					registerNewTeam(Teams.RED.name(), playerPerTeam);
					break;
				case 4:
					registerNewTeam(Teams.BLUE.name(), playerPerTeam);
					registerNewTeam(Teams.RED.name(), playerPerTeam);
					registerNewTeam(Teams.GREEN.name(), playerPerTeam);
					registerNewTeam(Teams.YELLOW.name(), playerPerTeam);
					break;
				case 8:
					registerNewTeam(Teams.BLUE.name(), playerPerTeam);
					registerNewTeam(Teams.RED.name(), playerPerTeam);
					registerNewTeam(Teams.GREEN.name(), playerPerTeam);
					registerNewTeam(Teams.YELLOW.name(), playerPerTeam);
					registerNewTeam(Teams.BLACK.name(), playerPerTeam);
					registerNewTeam(Teams.ORANGE.name(), playerPerTeam);
					registerNewTeam(Teams.PURPLE.name(), playerPerTeam);
					registerNewTeam(Teams.WHITE.name(), playerPerTeam);
					break;
				default:
					Bukkit.broadcastMessage("Es sind nur 2, 4 oder 8 Teams verfügbar!");
					stopWithDelay(10);
					break;
				}
			}
			
			@Override
			public String translate(String farbe) {
				switch (farbe.toUpperCase()) {
				case "BLACK":
					farbe = "Schwarz";
					break;
				case "BLUE":
					farbe = "Blau";
					break;
				case "ORANGE":
					farbe = "Orange";
					break;
				case "GREEN":
					farbe = "Grün";
					break;
				case "PURPLE":
					farbe = "Lila";
					break;
				case "RED":
					farbe = "Rot";
					break;
				case "WHITE":
					farbe = "Weiß";
					break;
				case "YELLOW":
					farbe = "Gelb";
					break;
				}
				return farbe;

			}

			@Override
			public Team registerNewTeam(String teamName, int maxPlayer) {
				Team t = new Team() {

					String teamname;
					int maxPlayer;
					short colordata;
					String colorcode;
					ArrayList<Player> players = new ArrayList<>();
					ArrayList<Player> deadPlayers = new ArrayList<>();

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

						if (players.contains(player)) {
							return true;
						}
						return false;
					}
					
					@Override
					public ArrayList<Player> getPlayers() {

						return players;
					}

					public boolean full() {

						if (maxPlayer == players.size()) {
							return true;
						}
						return false;
					}

					@Override
					public void initialize(String teamname, int maxPlayer) {
						short colordata = 0;
						String colorcode = null;

						if (Teams.valueOf(teamname) != null) {

							switch (teamname) {
							case "BLACK":
								colorcode = "§0";
								colordata = 15;
								break;
							case "BLUE":
								colorcode = "§9";
								colordata = 11;
								break;
							case "ORANGE":
								colorcode = "§6";
								colordata = 1;
								break;
							case "GREEN":
								colorcode = "§2";
								colordata = 13;
								break;
							case "PURPLE":
								colorcode = "§d";
								colordata = 10;
								break;
							case "RED":
								colorcode = "§c";
								colordata = 14;
								break;
							case "WHITE":
								colorcode = "§f";
								colordata = 0;
								break;
							case "YELLOW":
								colorcode = "§e";
								colordata = 4;
								break;
							default:
								colorcode = "§f";
								colordata = 0;
								break;
							}
						}

						this.colorcode = colorcode;
						this.colordata = colordata;
						this.maxPlayer = maxPlayer;
						this.teamname = teamname;
					}

					@Override
					public boolean isPlayerDead(Player player) {
						if (deadPlayers.contains(player)) {
							return true;
						}
						return false;
					}

					@Override
					public void setPlayerDead(Player player) {
						if (!deadPlayers.contains(player)) {
							deadPlayers.add(player);
						}
					}

				};
				t.initialize(teamName, maxPlayer);
				allteams.add(t);
				return t;
			}

			@Override
			public Team getTeam(String team) {
				for (Team te : allteams) {
					if (te.getTeamname().equals(team.toUpperCase())) {
						return te;
					}
				}
				return null;
			}

			@Override
			public Team getTeam(Player player) {
				for (Team te : allteams) {
					if (te.playerInThisTeam(player)) {
						return te;
					}
				}
				return null;
			}

			@Override
			public boolean isPlayerInAnyTeam(Player player) {
				for (Team te : allteams) {
					if (te.playerInThisTeam(player)) {
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean isPlayerDead(Player player) {
				for (Team te : allteams) {
					if (te.isPlayerDead(player)) {
						return true;
					}
				}
				return false;
			}

			@Override
			public ArrayList<Team> getTeams() {
				return allteams;
			}

			@Override
			public Team DataToTeam(short data) {
				for (Team te : allteams) {
					if (te.getColordata() == data) {
						return te;
					}
				}
				return null;
			}
		};
	}

	public static QuestManager QuestManager() {
		return new QuestManager() {

			@Override
			public ArrayList<Quest> getQuests() {
				return quests;
			}

			@Override
			public Quest getQuest(String quest) {
				for (Quest q : quests) {
					if (q.getQuestName().equalsIgnoreCase(quest)) {
						return q;
					}
				}
				return null;

			}

			@Override
			public Quest registerNewQuest(String questName0, int Band) {
				Quest q = new Quest() {

					String questName = questName0;
					
					String displayName = questName;
					
					String infoText = null;

					int questid = -1;

					boolean repeatable;

					private int cooldown;

					ArrayList<NPC> npcs = new ArrayList<>();

					List<String> active_Player_UUIDS = getActivePlayerUUIDS();

					HashMap<States, DialogMessage> dialog = new HashMap<>();

					int band = Band;

					boolean removeOnReload = false;

					boolean playableWithParty = false;

					@Override
					public List<String> getActivePlayerUUIDS() {

						if (active_Player_UUIDS == null) {

							ArrayList<String> pls = new ArrayList<>();

							Connection con = MySQL.Connect();

							if (con != null) {
								try {
									ResultSet s = con
											.prepareStatement(
													"SELECT * FROM `Quests_aktuell` WHERE name = '" + questName0 + "'")
											.executeQuery();

									while (s.next()) {
										pls.add(s.getString("uuid"));
									}
									con.close();
									return pls;
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}

						} else {
							return active_Player_UUIDS;
						}

						return null;
					}

					@Override
					public boolean getRemoveOnReload() {
						return removeOnReload;
					}

					@Override
					public void setRemoveOnReload(boolean b) {
						this.removeOnReload = b;
					}

					@Override
					public int getBand() {
						return band;
					}

					@Override
					public boolean getPlayableWithParty() {
						return playableWithParty;
					}

					@Override
					public void setPlayableWithParty(boolean b) {
						this.playableWithParty = b;
					}

					@Override
					public void setCooldown(int cooldown, TIME t) {
						switch (t) {
						case SEKUNDEN:
							this.cooldown = cooldown;
							break;
						case MINUTEN:
							this.cooldown = cooldown * 60;
							break;
						case STUNDEN:
							this.cooldown = cooldown * 60 * 60;
							break;
						case TAGE:
							this.cooldown = cooldown * 60 * 60 * 24;
							break;
						}
					}

					@Override
					public int getCooldown() {
						return cooldown;
					}

					@Override
					public ArrayList<Integer> getNpcIDs() {
						ArrayList<Integer> temp = new ArrayList<>();
						for (NPC npc : npcs) {
							temp.add(npc.getId());
						}
						return temp;
					}

					@Override
					public ArrayList<NPC> getNpcs() {
						return npcs;
					}

					@Override
					public String getQuestName() {
						return questName;
					}

					@Override
					public void setQuestName(String questName0) {
						questName = questName0;
					}

					@Override
					public NPC createNPC(EntityType entity, String name, UUID skinUUID) {
						NPC temp = CitizensAPI.getNPCRegistry().createNPC(entity, name);

						temp.addTrait(CitizensAPI.getTraitFactory().getTraitClass("lookclose"));
						temp.getTrait(LookClose.class).lookClose(true);
						
						temp.data().set("PLAYER_SKIN_UUID_METADATA", skinUUID.toString());

						npcs.add(temp);
						return temp;
					}
					
					@Override
					public NPC createNPC(EntityType entity, String name) {
						NPC temp = CitizensAPI.getNPCRegistry().createNPC(entity, name);

						temp.addTrait(CitizensAPI.getTraitFactory().getTraitClass("lookclose"));
						temp.getTrait(LookClose.class).lookClose(true);

						npcs.add(temp);
						return temp;
					}

					@Override
					public HashMap<States, DialogMessage> getDialogs() {
						return dialog;
					}

					@Override
					public DialogMessage getDialog(States stat) {
						return dialog.get(stat);
					}

					@Override
					public void setDialog(DialogMessage dialogpl, States stat) {
						dialog.put(stat, dialogpl);
					}

					@Override
					public void addPlayer(QuestPlayer qp) {
						active_Player_UUIDS.add(qp.getPlayer().getUniqueId().toString());
					}

					@Override
					public void removePlayer(QuestPlayer qp) {
						active_Player_UUIDS.remove(qp.getPlayer().getUniqueId().toString());
					}

					@Override
					public List<QuestPlayer> getActiveOnlinePlayers() {

						ArrayList<QuestPlayer> onlinePlayers = new ArrayList<>();

						for (String s : active_Player_UUIDS) {

							Player p = Bukkit.getPlayer(UUID.fromString(s));

							if (p != null) {
								onlinePlayers.add(Biomia.getQuestPlayer(p));
							}
						}

						return onlinePlayers;
					}

					@Override
					public int getQuestID() {

						if (questid == -1) {

						}

						return questid;
					}

					@Override
					public void registerQuestIfnotExist() {
						if (MySQL.executeQuery("SELECT name from `Quests` where name = '" + questName + "'",
								"name") == null) {
							MySQL.executeUpdate(
									"INSERT INTO `Quests` (name, band) values ('" + questName + "', " + band + ")");
						}
						questid = MySQL.executeQuerygetint(
								"SELECT name, id from `Quests` where name = '" + questName + "'", "id");
					}

					@Override
					public boolean isRepeatble() {
						return repeatable;
					}

					@Override
					public void setRepeatable(boolean b) {
						this.repeatable = b;
					}

					@Override
					public void setDisplayName(String s) {
						displayName = s;
					}

					@Override
					public void setInfoText(String s) {
						infoText = s;
					}

					@Override
					public String getDisplayName() {
						return displayName;
					}

					@Override
					public String getInfoText() {
						return infoText;
					}

				};
				quests.add(q);
				q.registerQuestIfnotExist();
				return q;
			}
		};
	}
}