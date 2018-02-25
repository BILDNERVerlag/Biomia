package de.biomia.bw.gamestates;

import de.biomia.bw.messages.Messages;
import de.biomia.bw.var.Teleport;
import de.biomia.bw.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.api.connect.Connect;
import de.biomia.api.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

class End {

    public static void start() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Biomia.getBiomiaPlayer(p).setBuild(false);
            Biomia.getBiomiaPlayer(p).setDamageEntitys(false);
            Biomia.getBiomiaPlayer(p).setGetDamage(false);
        }

//		Leaderboard.updateThisStats();
        Teleport.teleportAllToWarteLobby();

        new BukkitRunnable() {
            int i = 15;

            @Override
            public void run() {
                while (i >= 0) {
                    if (i == 15) {
                        Bukkit.broadcastMessage(Variables.prefix + Messages.restartCountDown.replaceAll("%t", i + ""));
                    } else if (i == 10) {
                        Bukkit.broadcastMessage(Variables.prefix + Messages.restartCountDown.replaceAll("%t", i + ""));
                    } else if (i <= 5 && i != 0) {
                        Bukkit.broadcastMessage(Variables.prefix + Messages.restartCountDown.replaceAll("%t", i + ""));
                    } else if (i == 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            Connect.connectToRandom(p, "Lobby");
                        }
//						Leaderboard.updateAllRanks();
                        Bukkit.shutdown();
                        return;
                    }
                    i--;
                }

            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }
}
