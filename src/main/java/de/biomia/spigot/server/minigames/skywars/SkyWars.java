package de.biomia.spigot.server.minigames.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.server.minigames.general.GameState;
import de.biomia.spigot.server.minigames.skywars.chests.Chests;
import de.biomia.spigot.server.minigames.skywars.chests.Items;
import de.biomia.spigot.commands.minigames.SWCommand;
import de.biomia.spigot.server.minigames.skywars.gamestates.InLobby;
import de.biomia.spigot.server.minigames.skywars.kits.Kits;
import de.biomia.spigot.listeners.servers.SkyWarsListener;
import de.biomia.spigot.server.minigames.skywars.lobby.JoinTeam;
import de.biomia.spigot.server.minigames.skywars.var.Variables;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import static de.biomia.spigot.configs.Config.saveConfig;

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
