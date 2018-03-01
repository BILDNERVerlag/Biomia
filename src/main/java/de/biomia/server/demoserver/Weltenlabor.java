package de.biomia.server.demoserver;

import de.biomia.Main;
import de.biomia.commands.weltenlabor.BauCommand;
import de.biomia.dataManager.configs.DemoConfig;
import de.biomia.messages.manager.Scoreboards;
import de.biomia.server.demoserver.listeners.DemoListener;
import de.biomia.server.demoserver.teleporter.Bauten;
import de.biomia.server.demoserver.teleporter.ScrollingInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Weltenlabor {

    public static final ArrayList<Bauten> bauten = new ArrayList<>();
    public static ScrollingInventory si;

    public static void init() {

        si = new ScrollingInventory("\u00A7eW\u00e4hle dein Ziel!", 3);

        Bukkit.getPluginManager().registerEvents(new DemoListener(), Main.getPlugin());

        Main.getPlugin().getCommand("bauwerk").setExecutor(new BauCommand());

        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboards.setTabList(p);
        }

        Main.getPlugin().getConfig().addDefault("lastID", 0);
        Main.getPlugin().saveDefaultConfig();
        DemoConfig.hookInPlugin();

    }

}
