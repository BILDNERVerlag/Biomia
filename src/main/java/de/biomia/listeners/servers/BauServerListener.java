package de.biomia.listeners.servers;

import de.biomia.Biomia;
import de.biomia.messages.manager.Scoreboards;
import de.biomia.tools.BackToLobby;
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
        Scoreboards.setTabList(p);
        BackToLobby.getLobbyItem(p, 8);
    }

}
