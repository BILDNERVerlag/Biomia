package de.biomia.listeners;

import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.biomia.Biomia;
import de.biomia.BiomiaPlayer;
import de.biomia.messages.manager.Scoreboards;
import de.biomia.tools.InventorySave;
import de.biomia.tools.PlayerToServerConnector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class FreebuildListener extends BiomiaListener {

    @EventHandler
    public static void onJoin_(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        InventorySave.setInventory(p, "FreebuildServer");
        p.sendMessage("\u00A77[\u00A75Bio\u00A72mia\u00A77] \u00A76Willkommen auf dem FreebuildServer, " + p.getName() + "!");
        bp.setBuild(true);
        Scoreboards.setTabList(p);
    }

    @EventHandler
    public static void onLeave_(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        InventorySave.saveInventory(p, "FreebuildServer");
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        ArrayList<ServerObject> servers = new ArrayList<>();
        Player p = event.getPlayer();

        double x = p.getLocation().getX();
        double y = p.getLocation().getY();
        double z = p.getLocation().getZ();

        if ((-194 <= x) && (x <= -191) && (64 <= y) && (y <= 67)) {
            if ((384 <= z) && (z <= 387)) {
                // ZUM FARMSERVER
                p.teleport(new Location(Bukkit.getWorld("world"), -198, 64, 385, 90, 0));
                if (Biomia.getBiomiaPlayer(p).isStaff()) {
                        PlayerToServerConnector.connectToRandom(p, "FarmServer");
                } else
                    p.sendMessage(
                            "\u00A7cDas Portal zur Farmwelt ist offenbar noch nicht richtig ausgerichtet! Probier es am besten in ein paar Tagen erneut!");
            } else if ((390 <= z) && (z <= 393)) {
                // ZUR LOBBY
                p.teleport(new Location(Bukkit.getWorld("world"), -198, 64, 391, 120, 0));
                    PlayerToServerConnector.connectToRandom(p, "Lobby");
            }
        }
    }
}