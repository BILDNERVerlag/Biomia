package de.biomia.spigot.quests;

import de.biomia.spigot.BiomiaServer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import org.bukkit.Bukkit;

import static de.biomia.spigot.Main.registerCommand;

public class Quests extends BiomiaServer {
    public Quests() {
        super(BiomiaServerType.Quest);
    }

    @Override
    public void start() {
        super.start();
        new Wasserholen();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        Bukkit.getPluginManager().registerEvents(new QuestListener(), Main.getPlugin());
    }

    @Override
    protected void initCommands() {
        super.initCommands();
        registerCommand(new QuestCommands());
    }
}
