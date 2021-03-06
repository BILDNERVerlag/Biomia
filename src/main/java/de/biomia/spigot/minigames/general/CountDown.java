package de.biomia.spigot.minigames.general;

import de.biomia.spigot.Main;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CountDown {

    private int countdown = 60;
    private BukkitTask bukkitTask;
    private final GameMode mode;

    public CountDown(GameMode mode) {
        this.mode = mode;
    }

    public void startCountDown() {
        bukkitTask = new BukkitRunnable() {
            int zuWenig = 0;

            @Override
            public void run() {

                int onlinePlayer = Bukkit.getOnlinePlayers().size();

                if (getCountdown() >= 0) {

                    if (mode.getInstance().getTeamSize() == 1)

                        if (onlinePlayer > mode.getInstance().getTeamSize() - ((mode.getInstance().getTeamSize() == 1) ? 0 : 1)) {

                        if (getCountdown() > 20)
                            if (onlinePlayer == mode.getInstance().getTeamSize() * mode.getInstance().getTeamAmount())
                                setCountdown(20);

                        if (getCountdown() == 0) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.setLevel(0);
                            }
                            mode.getStateManager().getLobbyState().stop();
                            return;
                        }

                        boolean send = getCountdown() == 45 || getCountdown() == 30 || getCountdown() == 20 || getCountdown() == 15
                                || getCountdown() == 10 || (getCountdown() <= 5 && getCountdown() > 0);

                        if (send)
                            Bukkit.broadcastMessage(Messages.format(MinigamesMessages.lobbyCountDown, getCountdown()));

                        Bukkit.getOnlinePlayers().forEach(p -> {
                            p.setLevel(getCountdown());
                            p.setExp(getCountdown() / 60f);
                            if (send) p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 2);
                        });
                        setCountdown(getCountdown() - 1);

                    } else if (onlinePlayer > 0)
                        if (zuWenig == 30) {
                            Bukkit.broadcastMessage(MinigamesMessages.notEnoughPlayerToStart);
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

    public void cancel() {
        try {
            bukkitTask.cancel();
        } catch (NullPointerException ignored) {
            //TODO: hat in duell nullpointer ausgelöst, vielleicht mal anschauen wieso bukkitTask null ist
        }
    }

}
