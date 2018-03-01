package de.biomia.listeners.lobby;

import de.biomia.server.lobby.Lobby;
import de.biomia.server.lobby.LobbyScoreboard;
import de.biomia.tools.RankManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Join implements Listener {

    @EventHandler
    public static void onQuit(PlayerQuitEvent e) {
        e.getPlayer().getInventory().clear();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent pj) {
        Player p = pj.getPlayer();
        p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(true);
        sendRegMsg(p);
        Inventory.setInventory(p);
        LobbyScoreboard.sendScoreboard(p);

        for (Player pl : Lobby.getSilentLobby()) {
            p.hidePlayer(pl);
            pl.hidePlayer(p);
        }
    }

    private static void sendRegMsg(Player p) {
        if (RankManager.getRank(p).equals("UnregSpieler")) {
            TextComponent register = new TextComponent();
            p.sendMessage(ChatColor.DARK_PURPLE + "Du bist noch nicht registriert!");
            register.setText(ChatColor.BLUE + "Registriere dich jetzt auf: " + ChatColor.GRAY + "www."
                    + ChatColor.DARK_PURPLE + "Bio" + ChatColor.DARK_GREEN + "mia"
                    + ChatColor.GRAY + ".de");
            register.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));
            p.spigot().sendMessage(register);
            p.sendMessage(ChatColor.GRAY + "Oder sp\u00fcter mit " + ChatColor.GOLD + "/register");
        }

    }
}