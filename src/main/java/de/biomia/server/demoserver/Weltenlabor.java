package de.biomia.server.demoserver;

import de.biomia.BiomiaPlayer;
import de.biomia.Main;
import de.biomia.commands.weltenlabor.BauCommand;
import de.biomia.dataManager.configs.DemoConfig;
import de.biomia.listeners.servers.DemoListener;
import de.biomia.messages.manager.Scoreboards;
import de.biomia.server.demoserver.teleporter.Bauten;
import de.biomia.server.demoserver.teleporter.ScrollingInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Weltenlabor {

    public static final ArrayList<Bauten> bauten = new ArrayList<>();
    public static final HashMap<BiomiaPlayer, ScrollingInventory> si = new HashMap<>();

    public static void init() {

        Bukkit.getPluginManager().registerEvents(new DemoListener(), Main.getPlugin());

        Main.registerCommand(new BauCommand());

        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboards.setTabList(p);
        }

        Main.getPlugin().getConfig().addDefault("lastID", 0);
        Main.getPlugin().saveDefaultConfig();
        DemoConfig.hookInPlugin();

    }

}
