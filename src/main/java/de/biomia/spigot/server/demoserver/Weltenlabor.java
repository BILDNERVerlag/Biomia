package de.biomia.spigot.server.demoserver;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.weltenlabor.BauWerkCommand;
import de.biomia.spigot.configs.DemoConfig;
import de.biomia.spigot.general.BiomiaServer;
import de.biomia.spigot.listeners.servers.DemoListener;
import de.biomia.spigot.server.demoserver.teleporter.Bauten;
import de.biomia.spigot.server.demoserver.teleporter.ScrollingInventory;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;

public class Weltenlabor extends BiomiaServer {

    private final ArrayList<Bauten> bauten = new ArrayList<>();
    private final HashMap<BiomiaPlayer, ScrollingInventory> scrollingInv = new HashMap<>();

    @Override
    public void start() {
        super.start();
        Main.getPlugin().getConfig().addDefault("lastID", 0);
        Main.getPlugin().saveDefaultConfig();
        DemoConfig.hookInPlugin();

    }

    @Override
    protected void initListeners() {
        super.initListeners();
        Bukkit.getPluginManager().registerEvents(new DemoListener(), Main.getPlugin());
    }


    @Override
    protected void initCommands() {
        super.initCommands();
        Main.registerCommand(new BauWerkCommand());
    }

    public ArrayList<Bauten> getBauten() {
        return bauten;
    }

    public HashMap<BiomiaPlayer, ScrollingInventory> getScrollingInv() {
        return scrollingInv;
    }
}
