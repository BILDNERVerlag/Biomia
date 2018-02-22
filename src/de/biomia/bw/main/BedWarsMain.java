package de.biomia.bw.main;

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
import de.biomiaAPI.Biomia;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class BedWarsMain {

    public static GameState gameState = GameState.LOBBY;

    public static void initBedWars() {

        Main.getPlugin().getServer().createWorld(new WorldCreator(Variables.name));

        Biomia.TeamManager().initTeams(Variables.playerPerTeam, Variables.teams);

        Config.addDefaults();
        Main.getPlugin().saveDefaultConfig();
        Main.getPlugin().saveConfig();

        Config.loadLocsFromConfig();
        Config.loadTeamJoiner();
        Config.loadSignsFromConfig();
        Config.loadBeds();
        Config.loadSpawner();

        Shop.init();

        initListener();
        initCommands();

        Variables.teamJoiner = JoinTeam.getTeamSwitcher();
        InLobby.start();
    }

    private static void initListener() {
        Bukkit.getPluginManager().registerEvents(new BedWarsListener(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new BedListener(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new SpecialItems(), Main.getPlugin());

    }

    private static void initCommands() {
        Main.getPlugin().getCommand("bw").setExecutor(new BW());
    }
}
