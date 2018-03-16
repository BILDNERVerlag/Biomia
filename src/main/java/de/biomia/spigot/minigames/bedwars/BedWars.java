package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.Main;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.listeners.servers.BedWarsListener;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.general.SpawnItems;
import de.biomia.spigot.minigames.general.TeamSwitcher;
import de.biomia.spigot.minigames.general.shop.Shop;
import org.bukkit.Bukkit;

public class BedWars extends GameMode {

    //TODO: messages anpassen (farben + satzzeichen)
    //TODO: mit den chatlistenern (und vmtl auch n paar anderen listenern) ist irgendetwas echt funky (siehe testnachrichten), bedwarslistener.onChat und gamehandler.onChat werden je zweimal aufgerufen bei jeder msg
    //vmtl ne gute idee onChat in gamehandler nichtmehr final zu machen und in bwlistener das ganze zu überschreiben (würde zumindest schonmal 2 statt 4 nachrichten pro nachricht bedeuten), aber nicht sicher ob damit in SW was kaputtgeht
    //TODO: spawnpunkt zum bett setzen
    //TODO: bei spielende die leute ordentlich rausteleporten
    //TODO: @all-Nachrichten nicht überall gleichzeitig hinschicken (aktuell bekommt man in der lobby die nachrichten auch)

    @Override
    protected GameHandler initHandler() {
        return new BedWarsListener(this);
    }

    private static BedWars instance;

    public BedWars(GameInstance instance) {
        super(instance);
        BedWars.instance = this;
    }

    @Override
    public void start() {
        getStateManager().setLobbyState(new GameStateManager.LobbyState(this) {
            private SpawnItems itemManager;

            @Override
            public void start() {
                super.start();
                itemManager = new SpawnItems(((BedWarsConfig) getConfig()).loadSpawner(getInstance()), getInstance().getWorld());
                itemManager.startSpawning();
            }
        });
        super.start();
        Shop.init();
        Bukkit.getPluginManager().registerEvents(new BedListener(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new SpecialItems(), Main.getPlugin());

        teamSwitcher = TeamSwitcher.getTeamSwitcher(this);
    }

    public static BedWars getBedWars() {
        return instance;
    }

    @Override
    protected MinigamesConfig initConfig() {
        return new BedWarsConfig();
    }
}
