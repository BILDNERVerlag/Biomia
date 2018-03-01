package de.biomia.server.freebuild;

import de.biomia.Main;
import de.biomia.commands.freebuild.BuildForAllCommand;
import de.biomia.commands.freebuild.RespawnCommand;
import de.biomia.server.freebuild.home.Home;
import de.biomia.server.freebuild.home.commands.*;
import de.biomia.server.freebuild.home.homes.HomeManager;
import de.biomia.server.freebuild.listeners.FreebuildListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Freebuild {

    private static Home home;

    public static void init() {
        home = new Home();
        home.onEnable();
        registerEvents();
        loadListeners();
    }

    public static void terminate() {
        home.terminateHomes();
    }

    private static void registerEvents() {
        // GENERAL
        Main.getPlugin().getCommand("respawn").setExecutor(new RespawnCommand());
        Main.getPlugin().getCommand("buildforall").setExecutor(new BuildForAllCommand());
        // HOME
        HomeManager homeManager = home.getHomeManager();
        Main.getPlugin().getCommand("delhome").setExecutor(new DeleteHomeCommand(homeManager));
        Main.getPlugin().getCommand("home").setExecutor(new HomeCommand(homeManager));
        Main.getPlugin().getCommand("sethome").setExecutor(new SetHomeCommand(homeManager));
        Main.getPlugin().getCommand("shreload").setExecutor(new ReloadCommand());
    }

    private static void loadListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new FreebuildListener(), Main.getPlugin());
        home.loadHomeListeners();
    }

}
