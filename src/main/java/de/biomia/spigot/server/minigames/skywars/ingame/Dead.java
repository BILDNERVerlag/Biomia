package de.biomia.spigot.server.minigames.skywars.ingame;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.server.minigames.general.GameState;
import de.biomia.spigot.server.minigames.general.teams.Team;
import de.biomia.spigot.server.minigames.skywars.SkyWars;
import de.biomia.spigot.server.minigames.skywars.gamestates.InGame;
import de.biomia.spigot.server.minigames.skywars.var.Scoreboards;
import de.biomia.spigot.server.minigames.skywars.var.Variables;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Dead {

    public static void setDead(Player target, Player killer) {

        Variables.livingPlayer.remove(target);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(target);

        Biomia.getTeamManager().getTeam(target).setPlayerDead(target);
        bp.addCoins(Variables.playReward, true);

        // Hide
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (Biomia.getTeamManager().isPlayerAlive(all))
                all.hidePlayer(target);
            else
                all.showPlayer(all);
        }

        // Disable Damage / Build
        target.getInventory().clear();
        bp.setGetDamage(false);
        bp.setDamageEntitys(false);
        bp.setBuild(false);
        target.setGameMode(GameMode.ADVENTURE);
        target.setSilent(true);

        // Fly settings
        target.setAllowFlight(true);
        target.setFlying(true);
        target.setFlySpeed(0.4F);

        // Handle Stats
//		for (Stats stat : Variables.stats) {
//			if (stat.uuid.equals(target.getUniqueId()))
//				stat.deaths++;
//			if (killer != null)
//				if (stat.uuid.equals(killer.getUniqueId()))
//					if (!Biomia.getTeamManager().getTeam(killer).playerInThisTeam(target))
//						stat.kills++;
//		}
        // Scoreboard
        Scoreboards.spectatorSB.getTeam("spectator").addEntry(target.getName());
        Scoreboards.setSpectatorSB(target);

        // Check if only one or less Team(s) left
        if (SkyWars.gameState != GameState.END) {

            ArrayList<Team> livingTeams = new ArrayList<>();
            if (Variables.livingPlayer.size() <= 1) {
                InGame.end();
                return;
            }
            for (Player player : Variables.livingPlayer) {
                Team t = Biomia.getTeamManager().getTeam(player);
                if (!livingTeams.contains(t)) {
                    livingTeams.add(t);
                }
            }
            if (livingTeams.size() <= 1) {
                InGame.end();
            }
        }
    }

}
