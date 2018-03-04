package de.biomia.spigot.server.demoserver;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.weltenlabor.BauWerkCommand;
import de.biomia.spigot.configs.DemoConfig;
import de.biomia.spigot.listeners.servers.DemoListener;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.server.demoserver.teleporter.Bauten;
import de.biomia.spigot.server.demoserver.teleporter.ScrollingInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Weltenlabor {

    public static final ArrayList<Bauten> bauten = new ArrayList<>();
    public static final HashMap<BiomiaPlayer, ScrollingInventory> si = new HashMap<>();

    public static void init() {

        Bukkit.getPluginManager().registerEvents(new DemoListener(), Main.getPlugin());

        Main.registerCommand(new BauWerkCommand());

        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboards.setTabList(p);
        }

        Main.getPlugin().getConfig().addDefault("lastID", 0);
        Main.getPlugin().saveDefaultConfig();
        DemoConfig.hookInPlugin();

    }

}
