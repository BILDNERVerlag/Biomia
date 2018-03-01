package de.biomia.server.minigames.bedwars;

import de.biomia.Biomia;
import de.biomia.Main;
import de.biomia.dataManager.configs.BedWarsConfig;
import de.biomia.server.minigames.bedwars.commands.BWCommand;
import de.biomia.server.minigames.bedwars.gamestates.InLobby;
import de.biomia.server.minigames.bedwars.listeners.BedListener;
import de.biomia.server.minigames.bedwars.listeners.BedWarsListener;
import de.biomia.server.minigames.bedwars.listeners.SpecialItems;
import de.biomia.server.minigames.bedwars.lobby.JoinTeam;
import de.biomia.server.minigames.bedwars.shop.Shop;
import de.biomia.server.minigames.bedwars.var.Variables;
import de.biomia.server.minigames.general.GameState;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import static de.biomia.dataManager.configs.Config.saveConfig;

public class BedWars {

    public static GameState gameState = GameState.LOBBY;

    public static void init() {

        Main.getPlugin().saveDefaultConfig();
        saveConfig();

        Main.getPlugin().getServer().createWorld(new WorldCreator(Variables.name));
        Biomia.getTeamManager().initTeams(Variables.playerPerTeam, Variables.teams);

        BedWarsConfig.loadLocsFromConfig();
        BedWarsConfig.loadTeamJoiner();
        BedWarsConfig.loadSignsFromConfig();
        BedWarsConfig.loadBeds();
        BedWarsConfig.loadSpawner();

        Shop.init();

        loadListeners();
        registerCommands();

        Variables.teamJoiner = JoinTeam.getTeamSwitcher();
        InLobby.start();
    }

    private static void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new BedWarsListener(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new BedListener(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new SpecialItems(), Main.getPlugin());

    }

    private static void registerCommands() {
        Main.registerCommand(new BWCommand());
    }
}
