package de.biomia.minigames.sw;

import de.biomia.minigames.GameState;
import de.biomia.minigames.sw.chests.Chests;
import de.biomia.minigames.sw.chests.Items;
import de.biomia.minigames.sw.commands.SWCommand;
import de.biomia.minigames.sw.gamestates.InLobby;
import de.biomia.minigames.sw.kits.Kits;
import de.biomia.minigames.sw.listeners.SkyWarsListener;
import de.biomia.minigames.sw.lobby.JoinTeam;
import de.biomia.general.configs.SkyWarsConfig;
import de.biomia.minigames.sw.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.api.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class SkyWars {

    public static GameState gameState = GameState.LOBBY;

    public static void init() {

        SkyWarsConfig.addDefaults();
        Main.getPlugin().saveDefaultConfig();
        Main.getPlugin().saveConfig();

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
        Main.getPlugin().getCommand("sw").setExecutor(new SWCommand());
    }
}
