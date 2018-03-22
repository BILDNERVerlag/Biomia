package de.biomia.spigot.minigames.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.events.skywars.SkyWarsStartEvent;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.general.Teleport;
import de.biomia.spigot.minigames.general.chests.Chests;
import de.biomia.spigot.minigames.general.chests.Items;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.tools.SkyWarsKitManager;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static de.biomia.spigot.configs.Config.saveConfig;

public class SkyWars extends GameMode {

    private final Chests chests = new Chests(this);

    @Override
    protected GameHandler initHandler() {
        return new SkyWarsListener(this);
    }

    protected SkyWars(GameInstance instance) {
        super(instance);
    }

    @Override
    public void start() {
        super.start();

        Main.getPlugin().saveDefaultConfig();
        saveConfig();

        Main.getPlugin().getServer().createWorld(new WorldCreator(MinigamesConfig.getMapName()));
        Items.init();
        KitManager.initKits();
        getStateManager().setInGameState(new GameStateManager.InGameState(this) {
            @Override
            public void start() {
                super.start();
                getMode().getStateManager().setActualGameState(GameStateManager.GameState.WAITING_FOR_START);

                HashMap<BiomiaPlayer, Integer> biomiaPlayerKits = new HashMap<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
                    KitManager.getManager(bp).setKitInventory();
                    biomiaPlayerKits.put(bp, SkyWarsKitManager.getLastSelectedKit(bp));
                }
                Bukkit.getPluginManager().callEvent(new SkyWarsStartEvent(biomiaPlayerKits));
                Teleport.sendCountDown(getMode());
            }
        });
    }

    public Chests getChests() {
        return chests;
    }

    @Override
    protected MinigamesConfig initConfig() {
        return new SkyWarsConfig(this);
    }

}
