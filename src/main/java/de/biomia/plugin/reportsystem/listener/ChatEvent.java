package de.biomia.plugin.reportsystem.listener;

import de.biomia.plugin.reportsystem.PlayerBan;
import de.biomia.plugin.reportsystem.PlayerReport;
import de.biomia.plugin.reportsystem.ReportManager;
import de.biomia.plugin.reportsystem.ReportSQL;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.main.Main;
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
            p.sendMessage("\u00A7aDanke f\u00fcr deinen Bug Report! Wir werden den Bug so schnell wie m\u00F6glich beheben!");

        } else if (ReportManager.waitingForName.contains(p)) {
            ReportManager.waitingForName.remove(p);
            e.setCancelled(true);
            new PlayerReport(Biomia.getBiomiaPlayer(p).getBiomiaPlayerID(), BiomiaPlayer.getBiomiaPlayerID(e.getMessage()));
            p.openInventory(Main.grund);
        } else if (waitForCostumReason.containsKey(bp)) {
            e.setCancelled(true);
            waitForCostumReason.get(bp).setReason(e.getMessage());
            waitForCostumReason.remove(bp);
        }
    }

}
