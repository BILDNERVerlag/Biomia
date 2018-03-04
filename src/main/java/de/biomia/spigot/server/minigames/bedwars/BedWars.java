package de.biomia.spigot.server.minigames.bedwars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.commands.minigames.BWCommand;
import de.biomia.spigot.server.minigames.bedwars.gamestates.InLobby;
import de.biomia.spigot.server.minigames.bedwars.listeners.BedListener;
import de.biomia.spigot.listeners.servers.BedWarsListener;
import de.biomia.spigot.server.minigames.bedwars.listeners.SpecialItems;
import de.biomia.spigot.server.minigames.bedwars.lobby.JoinTeam;
import de.biomia.spigot.server.minigames.bedwars.shop.Shop;
import de.biomia.spigot.server.minigames.bedwars.var.Variables;
import de.biomia.spigot.server.minigames.general.GameState;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import static de.biomia.spigot.configs.Config.saveConfig;

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
