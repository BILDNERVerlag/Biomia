package de.biomia.versus.vs.main;

import de.biomia.versus.bw.commands.BW;
import de.biomia.versus.sw.chests.Items;
import de.biomia.versus.sw.commands.SW;
import de.biomia.versus.sw.kits.KitManager;
import de.biomia.versus.vs.commands.VSCommands;
import de.biomia.versus.vs.lobby.Lobby;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;

public class VSMain {

    private static VSManager manager;

    public static void initVersus() {
        manager = new VSManager();
        loadListener();
        loadCommands();
        Items.init();
        KitManager.initKits();
    }

    private static void loadListener() {
        Bukkit.getPluginManager().registerEvents(new Lobby(), Main.getPlugin());
    }

    public static VSManager getManager() {
        return manager;
    }

    private static void loadCommands() {

        VSCommands cmds = new VSCommands();

        Main.getPlugin().getCommand("accept").setExecutor(cmds);
        Main.getPlugin().getCommand("decline").setExecutor(cmds);
        Main.getPlugin().getCommand("request").setExecutor(cmds);
        Main.getPlugin().getCommand("spawn").setExecutor(cmds);

        Main.getPlugin().getCommand("sw").setExecutor(new SW());
        Main.getPlugin().getCommand("bw").setExecutor(new BW());
    }

}