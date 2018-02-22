package de.biomia.sw.ingame;

import de.biomia.sw.gamestates.GameState;
import de.biomia.sw.gamestates.InGame;
import de.biomia.sw.main.SkyWarsMain;
import de.biomia.sw.var.Scoreboards;
import de.biomia.sw.var.Variables;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.Teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Dead {

    public static void setDead(Player target, Player killer) {

        Variables.livingPlayer.remove(target);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(target);

        Biomia.TeamManager().getTeam(target).setPlayerDead(target);
        bp.addCoins(Variables.playReward, true);

        // Hide
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (Biomia.TeamManager().livesPlayer(all))
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
//					if (!Biomia.TeamManager().getTeam(killer).playerInThisTeam(target))
//						stat.kills++;
//		}
        // Scoreboard
        Scoreboards.spectatorSB.getTeam("spectator").addEntry(target.getName());
        Scoreboards.setSpectatorSB(target);

        // Check if only one or less Team(s) left
        if (SkyWarsMain.gameState != GameState.END) {

            ArrayList<Team> livingTeams = new ArrayList<>();
            if (Variables.livingPlayer.size() <= 1) {
                InGame.end();
                return;
            }
            for (Player player : Variables.livingPlayer) {
                Team t = Biomia.TeamManager().getTeam(player);
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
