package de.biomia.spigot.minigames.skywars;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.minigames.SWCommand;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.listeners.servers.SkyWarsListener;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.bedwars.lobby.TeamSwitcher;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.minigames.skywars.chests.Chests;
import de.biomia.spigot.minigames.skywars.chests.Items;
import de.biomia.spigot.minigames.skywars.gamestates.SkyWarsInGameState;
import de.biomia.spigot.minigames.skywars.var.Variables;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import static de.biomia.spigot.configs.Config.saveConfig;

public class SkyWars extends GameMode {

    protected SkyWars(GameInstance instance) {
        super(instance);
    }

    private static SkyWars instance;

    public static SkyWars getSkyWars() {
        return instance;
    }

    @Override
    public void start() {
        super.start();

        instance = this;

        Main.getPlugin().saveDefaultConfig();
        saveConfig();

        Main.getPlugin().getServer().createWorld(new WorldCreator(Variables.name));

        //TODO init Teams
        //Biomia.getTeamManager().initTeams(Variables.playerPerTeam, Variables.teams);

        SkyWarsConfig.loadChestsFromConfig();
        SkyWarsConfig.loadLocsFromConfig();
        SkyWarsConfig.loadTeamJoiner();
        SkyWarsConfig.loadSignsFromConfig();
        Items.init();
        Variables.normalChestsFill = Chests.fillNormalChests();
        Variables.goodChestsFill = Chests.fillGoodChests();

        Bukkit.getPluginManager().registerEvents(new SkyWarsListener(), Main.getPlugin());
        Main.registerCommand(new SWCommand());

        KitManager.initKits();
        getStateManager().setInGameState(new SkyWarsInGameState(this));
    }
}
