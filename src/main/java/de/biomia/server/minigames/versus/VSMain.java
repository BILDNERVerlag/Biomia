package de.biomia.server.minigames.versus;

import de.biomia.Main;
import de.biomia.server.minigames.versus.bw.commands.BW;
import de.biomia.server.minigames.versus.sw.chests.Items;
import de.biomia.server.minigames.versus.sw.commands.SW;
import de.biomia.server.minigames.versus.sw.kits.KitManager;
import de.biomia.server.minigames.versus.vs.commands.VSCommands;
import de.biomia.server.minigames.versus.vs.lobby.Lobby;
import de.biomia.server.minigames.versus.vs.main.VSManager;
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

        Main.registerCommand(new VSCommands("accept"));
        Main.registerCommand(new VSCommands("decline"));
        Main.registerCommand(new VSCommands("request"));
        Main.registerCommand(new VSCommands("spawn"));

        Main.registerCommand(new BW());
        Main.registerCommand(new SW());
    }

}