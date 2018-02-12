package de.biomiaAPI;

import de.biomiaAPI.Quests.*;
import de.biomiaAPI.Teams.Team;
import de.biomiaAPI.Teams.TeamManager;
import de.biomiaAPI.Teams.Teams;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.mysql.MySQL;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Biomia {

    private static final HashMap<Player, BiomiaPlayer> bp = new HashMap<>();

    public static void stopWithDelay() {
        new BukkitRunnable() {
            public void run() {
                Bukkit.shutdown();
            }
        }.runTaskLater(Main.plugin, 10 * 20);
    }

    private static void removeBiomiaPlayer(Player p) {
        bp.remove(p);
    }

    public static void removePlayers(Player p) {
        removeBiomiaPlayer(p);
    }

    @Deprecated
    public static QuestPlayer getQuestPlayer(Player p) {
        return getBiomiaPlayer(p).getQuestPlayer();
    }

    @Deprecated
    public static QuestPlayer getQuestPlayer(BiomiaPlayer bp) {
        return bp.getQuestPlayer();
    }

    public static BiomiaPlayer getBiomiaPlayer(Player p) {
        return bp.computeIfAbsent(p, biomiaplayer -> new BiomiaPlayer(p));
    }

    public static int getBiomiaPlayerIDFromString(String playerName) {
        return MySQL.executeQuerygetint("Select id from BiomiaPlayer where name = '" + playerName + "'", "id", MySQL.Databases.biomia_db);
    }

    public static UUID getUUIDFromBiomiaPlayerID(int biomiaID) {
        String s = MySQL.executeQuery("Select uuid from BiomiaPlayer where id = " + biomiaID, "uuid", MySQL.Databases.biomia_db);
        if (s != null)
            return UUID.fromString(s);
        else return null;
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
                        Bukkit.broadcastMessage("Es sind nur 2, 4 oder 8 Teams verf?gbar!");
                        stopWithDelay();
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
                        farbe = "Gr\u00fcn";
                        break;
                    case "PURPLE":
                        farbe = "Lila";
                        break;
                    case "RED":
                        farbe = "Rot";
                        break;
                    case "WHITE":
                        farbe = "Wei\u00df";
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

                    @Override
                    public ArrayList<Player> getPlayers() {

                        return players;
                    }

                    public boolean full() {

                        return maxPlayer == players.size();
                    }

                    @Override
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

                    @Override
                    public boolean isPlayerDead(Player player) {
                        return deadPlayers.contains(player);
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
            public Quest getQuest(int questID) {
                for (Quest q : quests) {
                    if (q.getQuestID() == questID) {
                        return q;
                    }
                }
                return null;

            }

            public Quest getQuest(String s) {
                for (Quest q : quests) {
                    if (q.getQuestName().equalsIgnoreCase(s)) return q;
                }
                return null;
            }

            @Override
            public Quest registerNewQuest(String questName0, int Band) {
                //noinspection unchecked
                Quest q = new Quest() {

                    String questName = questName0;

                    String displayName = questName;

                    String infoText = null;

                    int questid = -1;

                    boolean repeatable;

                    private int cooldown;

                    final ArrayList<NPC> npcs = new ArrayList<>();

                    final List<Integer> active_Player_biomiaIDs = getActivePlayerBiomiaIDs();

                    final HashMap<States, DialogMessage> dialog = new HashMap<>();

                    final int band = Band;

                    boolean removeOnReload = false;

                    boolean playableWithParty = false;

                    @Override
                    public List<Integer> getActivePlayerBiomiaIDs() {

                        if (active_Player_biomiaIDs == null) {

                            ArrayList<Integer> pls = new ArrayList<>();

                            Connection con = MySQL.Connect(MySQL.Databases.quests_db);

                            if (con != null) {
                                try {
                                    ResultSet s = con
                                            .prepareStatement(
                                                    "SELECT * FROM `Quests_aktuell` WHERE questID = '" + questid + "'")
                                            .executeQuery();

                                    while (s.next()) {
                                        pls.add(s.getInt("biomiaID"));
                                    }
                                    con.close();
                                    return pls;
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            return active_Player_biomiaIDs;
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

                    @SuppressWarnings("unchecked")
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
                        Objects.requireNonNull(active_Player_biomiaIDs).add(qp.getBiomiaPlayer().getBiomiaPlayerID());
                    }

                    @Override
                    public void removePlayer(QuestPlayer qp) {
                        Objects.requireNonNull(active_Player_biomiaIDs).remove( new Integer(qp.getBiomiaPlayer().getBiomiaPlayerID()) );
                    }

                    @Override
                    public List<QuestPlayer> getActiveOnlinePlayers() {

                        ArrayList<QuestPlayer> onlinePlayers = new ArrayList<>();

                        for (Integer i : Objects.requireNonNull(active_Player_biomiaIDs)) {

                            UUID uuid = Biomia.getUUIDFromBiomiaPlayerID(i);

                            Player p = Bukkit.getPlayer(uuid);

                            if (p != null) {
                                onlinePlayers.add(Biomia.getBiomiaPlayer(p).getQuestPlayer());
                            }
                        }

                        return onlinePlayers;
                    }

                    @Override
                    public int getQuestID() {

                        return questid;
                    }

                    @Override
                    public void registerQuestIfnotExist() {
                        if (MySQL.executeQuery("SELECT name from `Quests` where name = '" + questName + "'",
                                "name", MySQL.Databases.quests_db) == null) {
                            MySQL.executeUpdate(
                                    "INSERT INTO `Quests` (name, band) values ('" + questName + "', " + band + ")", MySQL.Databases.quests_db);
                        }
                        questid = MySQL.executeQuerygetint(
                                "SELECT name, id from `Quests` where name = '" + questName + "'", "id", MySQL.Databases.quests_db);
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