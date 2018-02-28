package de.biomia.lobby.events;

import de.biomia.api.messages.Messages;
import de.biomia.lobby.Lobby;
import de.biomia.lobby.scoreboard.ChatColors;
import de.biomia.lobby.scoreboard.ScoreboardClass;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent pj) {
        Player p = pj.getPlayer();
        p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(true);
        ChatColors.sendRegMsg(p);
        Inventory.setInventory(p);
        ScoreboardClass.sendScoreboard(p);

        for (Player pl : Lobby.getSilentLobby()) {
            p.hidePlayer(pl);
            pl.hidePlayer(p);
        }
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent e) {
        e.getPlayer().getInventory().clear();
    }

}