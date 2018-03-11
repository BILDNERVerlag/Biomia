//package de.biomia.spigot.minigames.general.teams;
//
//import de.biomia.spigot.Biomia;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//
//import java.util.ArrayList;
//
//public class TeamManager {
//
//    // TODO remake (see minigames)
//
//    private final ArrayList<Team> allteams = new ArrayList<>();
//
//    public void initTeams(int playerPerTeam, int teams) {
//
//        switch (teams) {
//        case 2:
//            registerNewTeam(Teams.BLUE.name(), playerPerTeam);
//            registerNewTeam(Teams.RED.name(), playerPerTeam);
//            break;
//        case 4:
//            registerNewTeam(Teams.BLUE.name(), playerPerTeam);
//            registerNewTeam(Teams.RED.name(), playerPerTeam);
//            registerNewTeam(Teams.GREEN.name(), playerPerTeam);
//            registerNewTeam(Teams.YELLOW.name(), playerPerTeam);
//            break;
//        case 8:
//            registerNewTeam(Teams.BLUE.name(), playerPerTeam);
//            registerNewTeam(Teams.RED.name(), playerPerTeam);
//            registerNewTeam(Teams.GREEN.name(), playerPerTeam);
//            registerNewTeam(Teams.YELLOW.name(), playerPerTeam);
//            registerNewTeam(Teams.BLACK.name(), playerPerTeam);
//            registerNewTeam(Teams.ORANGE.name(), playerPerTeam);
//            registerNewTeam(Teams.PURPLE.name(), playerPerTeam);
//            registerNewTeam(Teams.WHITE.name(), playerPerTeam);
//            break;
//        default:
//            Bukkit.broadcastMessage("Es sind nur 2, 4 oder 8 Teams verf\u00fcgbar!");
//            Biomia.stopWithDelay();
//            break;
//        }
//    }
//
//    public String translate(String farbe) {
//        switch (farbe.toUpperCase()) {
//        case "BLACK":
//            farbe = "Schwarz";
//            break;
//        case "BLUE":
//            farbe = "Blau";
//            break;
//        case "ORANGE":
//            farbe = "Orange";
//            break;
//        case "GREEN":
//            farbe = "Gr\u00fcn";
//            break;
//        case "PURPLE":
//            farbe = "Lila";
//            break;
//        case "RED":
//            farbe = "Rot";
//            break;
//        case "WHITE":
//            farbe = "Wei\u00df";
//            break;
//        case "YELLOW":
//            farbe = "Gelb";
//            break;
//        }
//        return farbe;
//    }
//
//    //GETTER AND SETTER
//    private void registerNewTeam(String teamName, int maxPlayer) {
//        Team t = new Team();
//        t.initialize(teamName, maxPlayer);
//        allteams.add(t);
//    }
//
//    public Team getTeam(String team) {
//        for (Team te : allteams) {
//            if (te.getTeamname().equals(team.toUpperCase())) {
//                return te;
//            }
//        }
//        return null;
//    }
//
//    public Team getTeam(Player player) {
//        for (Team te : allteams) {
//            if (te.playerInThisTeam(player)) {
//                return te;
//            }
//        }
//        return null;
//    }
//
//    public boolean isPlayerInAnyTeam(Player player) {
//        for (Team te : allteams) {
//            if (te.playerInThisTeam(player)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean isPlayerAlive(Player player) {
//        for (Team te : allteams) {
//            if (te.isPlayerDead(player)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public ArrayList<Team> getTeams() {
//        return allteams;
//    }
//
//    public Team getTeamFromData(short data) {
//        for (Team te : allteams) {
//            if (te.getColordata() == data) {
//                return te;
//            }
//        }
//        return null;
//    }
//}
