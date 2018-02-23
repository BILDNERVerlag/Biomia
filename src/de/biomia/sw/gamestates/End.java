package de.biomia.sw.gamestates;

import de.biomia.sw.messages.Messages;
import de.biomia.sw.var.Teleport;
import de.biomia.sw.var.Variables;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.connect.Connect;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.tools.BackToLobby;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

class End {

    public static void start() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                p2.showPlayer(p);
            }
            p.getInventory().clear();
            BackToLobby.getLobbyItem(p, 8);
            p.setWalkSpeed(0.2f);
            Biomia.getBiomiaPlayer(p).setBuild(false);
            Biomia.getBiomiaPlayer(p).setDamageEntitys(false);
            Biomia.getBiomiaPlayer(p).setGetDamage(false);
            p.setFlying(false);
            p.setAllowFlight(false);
            p.setGameMode(GameMode.ADVENTURE);
        }
//        Leaderboard.updateThisStats();
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
//                        Leaderboard.updateAllRanks();
                        Bukkit.shutdown();
                        return;
                    }
                    i--;
                }

            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }
}