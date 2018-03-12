package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.minigames.BWCommand;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.listeners.servers.BedWarsListener;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.bedwars.listeners.BedListener;
import de.biomia.spigot.minigames.bedwars.listeners.SpecialItems;
import de.biomia.spigot.minigames.bedwars.lobby.TeamSwitcher;
import de.biomia.spigot.minigames.bedwars.var.Variables;
import de.biomia.spigot.minigames.general.SpawnItems;
import de.biomia.spigot.minigames.general.shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import static de.biomia.spigot.configs.Config.saveConfig;

public class BedWars extends GameMode {

    private static BedWars instance;

    public BedWars(GameInstance instance) {
        super(instance);
        BedWars.instance = this;
    }

    @Override
    public void start() {
        super.start(new BedWarsLobbyState(this));

        Main.getPlugin().saveDefaultConfig();
        saveConfig();

        Main.getPlugin().getServer().createWorld(new WorldCreator(Variables.name));

        BedWarsConfig.loadTeamJoiner();
        BedWarsConfig.loadSignsFromConfig(getInstance());
        BedWarsConfig.loadSpawner(getInstance());

        Shop.init();

        Bukkit.getPluginManager().registerEvents(new BedWarsListener(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new BedListener(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new SpecialItems(), Main.getPlugin());

        Main.registerCommand(new BWCommand());

        teamSwitcher = TeamSwitcher.getTeamSwitcher(this);
    }

    public static BedWars getBedWars() {
        return instance;
    }

    public class BedWarsLobbyState extends GameStateManager.LobbyState {

        private BedWarsLobbyState(GameMode mode) {
            super(mode);
        }

        private SpawnItems itemManager;

        @Override
        public void start() {
            super.start();
            itemManager = new SpawnItems(BedWarsConfig.loadSpawner(getInstance()), Bukkit.getWorld(Variables.name));
            itemManager.startSpawning();
        }
    }
}
