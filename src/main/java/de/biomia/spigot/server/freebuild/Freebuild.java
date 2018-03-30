package de.biomia.spigot.server.freebuild;

import de.biomia.spigot.BiomiaServer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.freebuild.RespawnCommand;
import de.biomia.spigot.listeners.servers.FreebuildListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

public class Freebuild extends BiomiaServer {

    public Freebuild(BiomiaServerType type) {
        super(type);
    }

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
