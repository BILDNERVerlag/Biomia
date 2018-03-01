package de.biomia.minigames.bedwars.listeners;

import de.biomia.minigames.bedwars.gamestates.InLobby;
import de.biomia.general.messages.BedWarsMessages;
import de.biomia.minigames.bedwars.var.Variables;
import de.biomia.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CountDown {

    private int countdown = 60;
    private BukkitTask bukkitTask;

    public CountDown() {
        startCountDown();
    }

    private void startCountDown() {

        bukkitTask = new BukkitRunnable() {
            int zuWenig = 0;

            @Override
            public void run() {

                int onlinePlayer = Bukkit.getOnlinePlayers().size();

                if (getCountdown() >= 0) {

                    if (onlinePlayer >= Variables.minPlayers) {

                        if (getCountdown() > 20)
                            if (onlinePlayer == Variables.maxPlayers)
                                setCountdown(20);

                        if (getCountdown() == 0) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.setLevel(0);
                            }

                            setCountdown(-1);
                            InLobby.end();
                            return;
                        }

                        if (getCountdown() == 45 || getCountdown() == 30 || getCountdown() == 20 || getCountdown() == 15
                                || getCountdown() == 10 || (getCountdown() <= 5 && getCountdown() > 0)) {
                            Bukkit.broadcastMessage(BedWarsMessages.lobbyCountDown.replaceAll("%t", getCountdown() + ""));
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 2);
                            }
                        }

                        if (getCountdown() > 0)
                            for (Player p : Bukkit.getOnlinePlayers())
                                p.setLevel(getCountdown());
                        setCountdown(getCountdown() - 1);

                    } else if (onlinePlayer > 0)
                        if (zuWenig == 30) {
                            Bukkit.broadcastMessage(BedWarsMessages.notEnoughPlayerToStart);
                            zuWenig = 0;
                        } else
                            zuWenig++;

                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);

    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

}
