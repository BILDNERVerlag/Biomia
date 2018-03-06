package de.biomia.spigot.minigames.bedwars.gamestates;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.universal.Messages;
import de.biomia.spigot.minigames.bedwars.var.Teleport;
import de.biomia.spigot.tools.PlayerToServerConnector;
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
                        Bukkit.broadcastMessage(Messages.PREFIX + BedWarsMessages.restartCountDown.replaceAll("%t", i + ""));
                    } else if (i == 10) {
                        Bukkit.broadcastMessage(Messages.PREFIX + BedWarsMessages.restartCountDown.replaceAll("%t", i + ""));
                    } else if (i <= 5 && i != 0) {
                        Bukkit.broadcastMessage(Messages.PREFIX + BedWarsMessages.restartCountDown.replaceAll("%t", i + ""));
                    } else if (i == 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            PlayerToServerConnector.connectToRandom(p, "Lobby");
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
