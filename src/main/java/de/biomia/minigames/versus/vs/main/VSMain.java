package de.biomia.minigames.versus.vs.main;

import de.biomia.minigames.versus.bw.commands.BW;
import de.biomia.minigames.versus.sw.chests.Items;
import de.biomia.minigames.versus.sw.commands.SW;
import de.biomia.minigames.versus.sw.kits.KitManager;
import de.biomia.minigames.versus.vs.commands.VSCommands;
import de.biomia.minigames.versus.vs.lobby.Lobby;
import de.biomia.api.main.Main;
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