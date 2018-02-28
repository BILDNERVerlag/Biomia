package de.biomia.bw;

import de.biomia.bw.commands.BW;
import de.biomia.bw.gamestates.GameState;
import de.biomia.bw.gamestates.InLobby;
import de.biomia.bw.listeners.BedListener;
import de.biomia.bw.listeners.BedWarsListener;
import de.biomia.bw.listeners.SpecialItems;
import de.biomia.bw.lobby.JoinTeam;
import de.biomia.bw.shop.Shop;
import de.biomia.bw.var.Config;
import de.biomia.bw.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.api.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class BedWars {

    public static GameState gameState = GameState.LOBBY;

    public static void init() {

        Config.addDefaults();
        Main.getPlugin().saveDefaultConfig();
        Main.getPlugin().saveConfig();

        Main.getPlugin().getServer().createWorld(new WorldCreator(Variables.name));
        Biomia.TeamManager().initTeams(Variables.playerPerTeam, Variables.teams);

        Config.loadLocsFromConfig();
        Config.loadTeamJoiner();
        Config.loadSignsFromConfig();
        Config.loadBeds();
        Config.loadSpawner();

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
        Main.getPlugin().getCommand("bw").setExecutor(new BW());
    }
}
