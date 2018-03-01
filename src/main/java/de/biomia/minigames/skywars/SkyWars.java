package de.biomia.minigames.skywars;

import de.biomia.minigames.GameState;
import de.biomia.minigames.skywars.chests.Chests;
import de.biomia.minigames.skywars.chests.Items;
import de.biomia.minigames.skywars.commands.SWCommand;
import de.biomia.minigames.skywars.gamestates.InLobby;
import de.biomia.minigames.skywars.kits.Kits;
import de.biomia.minigames.skywars.listeners.SkyWarsListener;
import de.biomia.minigames.skywars.lobby.JoinTeam;
import de.biomia.general.configs.SkyWarsConfig;
import de.biomia.minigames.skywars.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.Main;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class SkyWars {

    public static GameState gameState = GameState.LOBBY;

    public static void init() {

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
