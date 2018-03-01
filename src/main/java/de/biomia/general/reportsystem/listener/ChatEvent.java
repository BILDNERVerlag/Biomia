package de.biomia.general.reportsystem.listener;

import de.biomia.Biomia;
import de.biomia.BiomiaPlayer;
import de.biomia.general.reportsystem.PlayerBan;
import de.biomia.general.reportsystem.PlayerReport;
import de.biomia.general.reportsystem.ReportManager;
import de.biomia.general.reportsystem.ReportSQL;
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
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (ReportManager.waitingForBugReason.contains(p)) {
            ReportManager.waitingForBugReason.remove(p);
            e.setCancelled(true);

            ReportSQL.addBugReport(p, e.getMessage());
            p.sendMessage("\u00A7aDanke f\u00fcr deinen Bug Report! Wir werden den Bug so schnell wie m\u00F6glich beheben!");

        } else if (ReportManager.waitingForName.contains(p)) {
            ReportManager.waitingForName.remove(p);
            e.setCancelled(true);
            new PlayerReport(bp, Biomia.getOfflineBiomiaPlayer(e.getMessage()));
            p.openInventory(ReportManager.grund);
        } else if (waitForCostumReason.containsKey(bp)) {
            e.setCancelled(true);
            waitForCostumReason.get(bp).setReason(e.getMessage());
            waitForCostumReason.remove(bp);
        }
    }

}
