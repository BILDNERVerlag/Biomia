package de.biomia.spigot.listeners.servers;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.tools.InventorySave;
import de.biomia.spigot.tools.PlayerToServerConnector;
import de.biomia.spigot.tools.RewardItems;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FreebuildListener extends BiomiaListener {

    @EventHandler
    public static void onJoin_(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        InventorySave.setInventory(p, BiomiaServerType.Freebuild);
        p.sendMessage(Messages.PREFIX + "§6Willkommen auf dem FreebuildServer, " + p.getName() + "!");
        RewardItems.giveItems(bp, BiomiaServerType.Freebuild);
        Scoreboards.setTabList(p, true, false);
        bp.setInBuildmode(true);
    }

    @EventHandler
    public static void onLeave_(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        InventorySave.saveInventory(p, BiomiaServerType.Freebuild);
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        double x = p.getLocation().getX();
        double y = p.getLocation().getY();
        double z = p.getLocation().getZ();

        if ((-194 <= x) && (x <= -191) && (64 <= y) && (y <= 67)) {
            if ((384 <= z) && (z <= 387)) {
                // ZUM FARMSERVER
                p.teleport(new Location(Bukkit.getWorld("world"), -198, 64, 385, 90, 0));
                if (Biomia.getBiomiaPlayer(p).isStaff()) {
                    PlayerToServerConnector.connectToRandom(p, BiomiaServerType.FreebuildFarm);
                } else
                    p.sendMessage(
                            "§cDas Portal zur Farmwelt ist offenbar noch nicht richtig ausgerichtet! Probier es am besten in ein paar Tagen erneut!");
            } else if ((390 <= z) && (z <= 393)) {
                // ZUR LOBBY
                p.teleport(new Location(Bukkit.getWorld("world"), -198, 64, 391, 120, 0));
                PlayerToServerConnector.connectToRandom(p, BiomiaServerType.Lobby);
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        EntityType type = e.getEntityType();
        int spawnX = e.getLocation().getBlockX();
        int spawnZ = e.getLocation().getBlockZ();
        if (spawnX > -434 && spawnX < -102 && spawnZ > 80 && spawnZ < 547) {
            e.setCancelled(true);
        }
    }
}