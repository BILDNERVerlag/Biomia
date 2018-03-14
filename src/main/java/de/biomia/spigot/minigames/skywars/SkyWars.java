package de.biomia.spigot.minigames.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.minigames.SWCommand;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.events.skywars.SkyWarsStartEvent;
import de.biomia.spigot.listeners.servers.SkyWarsListener;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.bedwars.var.Teleport;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.minigames.skywars.chests.Chests;
import de.biomia.spigot.minigames.skywars.chests.Items;
import de.biomia.spigot.minigames.skywars.var.Variables;
import de.biomia.spigot.tools.SkyWarsKitManager;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static de.biomia.spigot.configs.Config.saveConfig;

public class SkyWars extends GameMode {

    protected SkyWars(GameInstance instance) {
        super(instance);
    }

    private static SkyWars instance;

    public static SkyWars getSkyWars() {
        return instance;
    }

    @Override
    public void start() {
        super.start();

        instance = this;

        Main.getPlugin().saveDefaultConfig();
        saveConfig();

        Main.getPlugin().getServer().createWorld(new WorldCreator(Variables.name));

        Items.init();
        Variables.normalChestsFill = Chests.fillNormalChests();
        Variables.goodChestsFill = Chests.fillGoodChests();

        Bukkit.getPluginManager().registerEvents(new SkyWarsListener(), Main.getPlugin());
        Main.registerCommand(new SWCommand());

        KitManager.initKits();
        getStateManager().setInGameState(new GameStateManager.InGameState(this) {
            @Override
            public void start() {
                super.start();
                SkyWars.getSkyWars().getStateManager().setActualGameState(GameStateManager.GameState.WAITING_FOR_START);

                HashMap<BiomiaPlayer, Integer> biomiaPlayerKits = new HashMap<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
                    KitManager.getManager(bp).setKitInventory();
                    biomiaPlayerKits.put(bp, SkyWarsKitManager.getLastSelectedKit(bp));
                }
                Bukkit.getPluginManager().callEvent(new SkyWarsStartEvent(biomiaPlayerKits));
                Teleport.teleportPlayerToMap(getMode());
                Teleport.sendCountDown(getMode());
            }
        });
    }

    @Override
    protected MinigamesConfig initConfig() {
        return new SkyWarsConfig();
    }

}
