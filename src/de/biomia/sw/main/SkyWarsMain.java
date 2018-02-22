package de.biomia.sw.main;

import de.biomia.sw.chests.Chests;
import de.biomia.sw.chests.Items;
import de.biomia.sw.commands.SW;
import de.biomia.sw.gamestates.GameState;
import de.biomia.sw.gamestates.InLobby;
import de.biomia.sw.kits.Kits;
import de.biomia.sw.listeners.SkyWarsListener;
import de.biomia.sw.lobby.JoinTeam;
import de.biomia.sw.var.Config;
import de.biomia.sw.var.Variables;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class SkyWarsMain {

    public static GameState gameState = GameState.LOBBY;

    public static void initSkyWars() {

        Main.getPlugin().getServer().createWorld(new WorldCreator(Variables.name));

        Biomia.TeamManager().initTeams(Variables.playerPerTeam, Variables.teams);

        Config.addDefaults();
        Main.getPlugin().saveDefaultConfig();
        Main.getPlugin().saveConfig();

        Config.loadChestsFromConfig();
        Config.loadLocsFromConfig();
        Config.loadTeamJoiner();
        Config.loadSignsFromConfig();
        Items.init();
        Variables.normalChestsFill = Chests.fillNormalChests();
        Variables.goodChestsFill = Chests.fillGoodChests();

        initListener();
        initCommands();

        Kits.initKits();
        Variables.teamJoiner = JoinTeam.getTeamSwitcher();

        InLobby.start();
    }

    private static void initListener() {
        Bukkit.getPluginManager().registerEvents(new SkyWarsListener(), Main.getPlugin());
    }

    private static void initCommands() {
        Main.getPlugin().getCommand("sw").setExecutor(new SW());
    }
}
