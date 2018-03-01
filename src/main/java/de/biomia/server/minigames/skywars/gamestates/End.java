package de.biomia.server.minigames.skywars.gamestates;

import de.biomia.Biomia;
import de.biomia.Main;
import de.biomia.messages.SkyWarsMessages;
import de.biomia.server.minigames.skywars.var.Teleport;
import de.biomia.server.minigames.skywars.var.Variables;
import de.biomia.tools.BackToLobby;
import de.biomia.tools.PlayerToServerConnector;
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
                        Bukkit.broadcastMessage(Variables.prefix + SkyWarsMessages.restartCountDown.replaceAll("%t", i + ""));
                    } else if (i == 10) {
                        Bukkit.broadcastMessage(Variables.prefix + SkyWarsMessages.restartCountDown.replaceAll("%t", i + ""));
                    } else if (i <= 5 && i != 0) {
                        Bukkit.broadcastMessage(Variables.prefix + SkyWarsMessages.restartCountDown.replaceAll("%t", i + ""));
                    } else if (i == 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            PlayerToServerConnector.connectToRandom(p, "Lobby");
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
