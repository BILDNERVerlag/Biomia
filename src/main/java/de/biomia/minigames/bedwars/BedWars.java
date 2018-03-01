package de.biomia.minigames.bedwars;

import de.biomia.minigames.bedwars.commands.BWCommand;
import de.biomia.minigames.GameState;
import de.biomia.minigames.bedwars.gamestates.InLobby;
import de.biomia.minigames.bedwars.listeners.BedListener;
import de.biomia.minigames.bedwars.listeners.BedWarsListener;
import de.biomia.minigames.bedwars.listeners.SpecialItems;
import de.biomia.minigames.bedwars.lobby.JoinTeam;
import de.biomia.minigames.bedwars.shop.Shop;
import de.biomia.general.configs.BedWarsConfig;
import de.biomia.minigames.bedwars.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.Main;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class BedWars {

    public static GameState gameState = GameState.LOBBY;

    public static void init() {

        Main.getPlugin().saveDefaultConfig();
        Main.getPlugin().saveConfig();

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
        Main.getPlugin().getCommand("bw").setExecutor(new BWCommand());
    }
}
