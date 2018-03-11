package de.biomia.spigot.minigames.skywars.gamestates;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.skywars.SkyWarsStartEvent;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.minigames.skywars.SkyWars;
import de.biomia.spigot.minigames.skywars.var.Teleport;
import de.biomia.spigot.tools.SkyWarsKitManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SkyWarsInGameState extends GameStateManager.InGameState {

    public SkyWarsInGameState(GameMode mode) {
        super(mode);
    }

    @Override
    public void start() {
        SkyWars.getSkyWars().getStateManager().setActualGameState(GameStateManager.GameState.WAITING_FOR_START);

        HashMap<BiomiaPlayer, Integer> biomiaPlayerKits = new HashMap<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            KitManager.getManager(bp).setKitInventory();
            biomiaPlayerKits.put(bp, SkyWarsKitManager.getLastSelectedKit(bp));
        }
        Bukkit.getPluginManager().callEvent(new SkyWarsStartEvent(biomiaPlayerKits));
        Teleport.teleportTeamsToMap();
        Teleport.sendCountDown();
    }
}
