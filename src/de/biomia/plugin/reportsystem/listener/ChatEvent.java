package de.biomia.plugin.reportsystem.listener;

import de.biomia.plugin.reportsystem.PlayerBan;
import de.biomia.plugin.reportsystem.PlayerReport;
import de.biomia.plugin.reportsystem.ReportManager;
import de.biomia.plugin.reportsystem.ReportSQL;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class ChatEvent implements Listener {

    public static final HashMap<BiomiaPlayer, PlayerBan> waitForCostumReason = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

        if (ReportManager.waitingForBugReason.contains(p)) {
            ReportManager.waitingForBugReason.remove(p);
            e.setCancelled(true);

            ReportSQL.addBugReport(p, e.getMessage());
            p.sendMessage("§aDanke für deinen Bug Report! Wir werden den Bug so schnell wie möglich beheben!");

        } else if (ReportManager.waitingForName.contains(p)) {
            ReportManager.waitingForName.remove(p);
            e.setCancelled(true);
            new PlayerReport(Biomia.getBiomiaPlayer(p).getBiomiaPlayerID(), BiomiaPlayer.getID(e.getMessage()));
            p.openInventory(Main.grund);
        } else if (waitForCostumReason.containsKey(bp)) {
            e.setCancelled(true);
            waitForCostumReason.get(bp).setReason(e.getMessage());
            waitForCostumReason.remove(bp);
        }
    }

}
