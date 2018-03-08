package de.biomia.spigot.server.freebuild;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.freebuild.RespawnCommand;
import de.biomia.spigot.general.BiomiaServer;
import de.biomia.spigot.listeners.servers.FreebuildListener;
import de.biomia.spigot.server.freebuild.home.Home;
import de.biomia.spigot.server.freebuild.home.commands.DeleteHomeCommand;
import de.biomia.spigot.server.freebuild.home.commands.HomeCommand;
import de.biomia.spigot.server.freebuild.home.commands.SetHomeCommand;
import de.biomia.spigot.server.freebuild.home.homes.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.plugin.PluginManager;

public class Freebuild extends BiomiaServer {

    @Override
    public void start() {
        super.start();
        Bukkit.setDefaultGameMode(GameMode.SURVIVAL);
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        Bukkit.getPluginManager().registerEvents(new FreebuildListener(), Main.getPlugin());
    }

    @Override
    protected void initCommands() {
        super.initCommands();
        Main.registerCommand(new RespawnCommand());
    }

}
