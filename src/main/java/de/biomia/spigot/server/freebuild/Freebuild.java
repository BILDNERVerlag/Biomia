package de.biomia.spigot.server.freebuild;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.freebuild.RespawnCommand;
import de.biomia.spigot.general.BiomiaServer;
import de.biomia.spigot.listeners.servers.FreebuildListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

public class Freebuild extends BiomiaServer {

    @Override
    public void start() {
        super.start();
        Bukkit.setDefaultGameMode(GameMode.SURVIVAL);
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
