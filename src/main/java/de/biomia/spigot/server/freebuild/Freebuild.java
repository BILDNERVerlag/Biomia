package de.biomia.spigot.server.freebuild;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.freebuild.RespawnCommand;
import de.biomia.spigot.listeners.servers.FreebuildListener;
import de.biomia.spigot.server.freebuild.home.Home;
import de.biomia.spigot.server.freebuild.home.commands.DeleteHomeCommand;
import de.biomia.spigot.server.freebuild.home.commands.HomeCommand;
import de.biomia.spigot.server.freebuild.home.commands.SetHomeCommand;
import de.biomia.spigot.server.freebuild.home.homes.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.plugin.PluginManager;

public class Freebuild {

    private static Home home;

    public static void init() {
        home = new Home();
        home.onEnable();
        registerEvents();
        loadListeners();
        Bukkit.setDefaultGameMode(GameMode.SURVIVAL);
    }

    public static void terminate() {
        home.terminateHomes();
    }

    private static void registerEvents() {
        // GENERAL
        Main.registerCommand(new RespawnCommand());
        // HOME
        HomeManager homeManager = home.getHomeManager();
        Main.getPlugin().getCommand("delhome").setExecutor(new DeleteHomeCommand(homeManager));
        Main.getPlugin().getCommand("home").setExecutor(new HomeCommand(homeManager));
        Main.getPlugin().getCommand("sethome").setExecutor(new SetHomeCommand(homeManager));
    }

    private static void loadListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new FreebuildListener(), Main.getPlugin());
        home.loadHomeListeners();
    }

}