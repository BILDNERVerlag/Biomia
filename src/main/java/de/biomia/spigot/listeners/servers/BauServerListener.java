package de.biomia.spigot.listeners.servers;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.tools.BackToLobby;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class BauServerListener extends BiomiaListener {

    @EventHandler
    public void onJoin_(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setGameMode(GameMode.CREATIVE);
        Biomia.getBiomiaPlayer(p).setBuild(true);
        BackToLobby.getLobbyItem(p, 8);
        Scoreboards.setTabList(e.getPlayer());
    }

}
