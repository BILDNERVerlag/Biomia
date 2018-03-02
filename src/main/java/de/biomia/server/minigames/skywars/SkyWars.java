package de.biomia.server.minigames.skywars;

import de.biomia.Biomia;
import de.biomia.Main;
import de.biomia.data.configs.SkyWarsConfig;
import de.biomia.server.minigames.general.GameState;
import de.biomia.server.minigames.skywars.chests.Chests;
import de.biomia.server.minigames.skywars.chests.Items;
import de.biomia.commands.minigames.SWCommand;
import de.biomia.server.minigames.skywars.gamestates.InLobby;
import de.biomia.server.minigames.skywars.kits.Kits;
import de.biomia.listeners.servers.SkyWarsListener;
import de.biomia.server.minigames.skywars.lobby.JoinTeam;
import de.biomia.server.minigames.skywars.var.Variables;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import static de.biomia.data.configs.Config.saveConfig;

public class SkyWars {

    public static GameState gameState = GameState.LOBBY;

    public static void init() {

        Main.getPlugin().saveDefaultConfig();
        saveConfig();

        Main.getPlugin().getServer().createWorld(new WorldCreator(Variables.name));

        Biomia.getTeamManager().initTeams(Variables.playerPerTeam, Variables.teams);

        SkyWarsConfig.loadChestsFromConfig();
        SkyWarsConfig.loadLocsFromConfig();
        SkyWarsConfig.loadTeamJoiner();
        SkyWarsConfig.loadSignsFromConfig();
        Items.init();
        Variables.normalChestsFill = Chests.fillNormalChests();
        Variables.goodChestsFill = Chests.fillGoodChests();

        loadListeners();
        registerCommands();

        Kits.initKits();
        Variables.teamJoiner = JoinTeam.getTeamSwitcher();

        InLobby.start();
    }

    private static void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new SkyWarsListener(), Main.getPlugin());
    }

    private static void registerCommands() {
        Main.registerCommand(new SWCommand());
    }
}
