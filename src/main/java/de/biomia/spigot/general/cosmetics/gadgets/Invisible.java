package de.biomia.spigot.general.cosmetics.gadgets;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.general.cosmetics.items.CosmeticGadgetItem;
import de.biomia.spigot.tools.Particles;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

class Invisible implements GadgetListener, Listener {

    private static final ArrayList<Player> invisibles = new ArrayList<>();

    @Override
    public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
        Location l = bp.getPlayer().getLocation().clone();

        for (int i = 0; i < 360; i += 6) {
            for (int degree = 0; degree < 360; degree += 6) {

                int a = i - 180;
                double radians = Math.toRadians(degree);
                double x = Math.cos(radians) / 360 * a;
                double z = Math.sin(radians) / 360 * a;

                Location loc = l.clone().add(x, i / 180D, z);
                new Particles(EnumParticle.SMOKE_NORMAL, loc, false, 0f, 0f, 0f, 1f, 1).sendAll();
            }
            l.add(0, 2 / 60D, 0);
        }

        for (Player p : Bukkit.getOnlinePlayers())
            p.hidePlayer(Main.getPlugin(), bp.getPlayer());
        invisibles.add(bp.getPlayer());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers())
                    p.showPlayer(Main.getPlugin(), bp.getPlayer());
                invisibles.remove(bp.getPlayer());
            }
        }.runTaskLater(Main.getPlugin(), 20 * 60);
        item.removeOne(bp, true);
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent e) {
        for (Player p : invisibles)
            e.getPlayer().hidePlayer(Main.getPlugin(), p);
    }
}
