package de.biomia.spigot.server.quests;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.quest.QuestCommands;
import de.biomia.spigot.BiomiaServer;
import de.biomia.spigot.server.quests.band1.*;
import de.biomia.spigot.server.quests.general.DialogMessage;
import de.biomia.spigot.server.quests.general.NPCManager;
import de.biomia.spigot.server.quests.general.Quest;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.biomia.spigot.listeners.servers.QuestListener;
import de.biomia.spigot.tools.LastPositionListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;

import static de.biomia.spigot.Main.getPlugin;

public class Quests extends BiomiaServer {

    @Override
    public void start() {
        super.start();
        NPCManager.saveNPCLocations();
    }

    @Override
    public void stop() {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "npc remove all");
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new LastPositionListener(), getPlugin());
        pm.registerEvents(new QuestListener(), getPlugin());

        initQuests(pm);
    }

    @Override
    protected void initCommands() {
        super.initCommands();
        Main.registerCommand(new QuestCommands());
    }

    private void initQuests(PluginManager pm) {
        pm.registerEvents(new Wasserholen(), getPlugin());
        pm.registerEvents(new ReiteDasSchwein(), getPlugin());
        pm.registerEvents(new GehAngeln(), getPlugin());
        pm.registerEvents(new AufDerSucheNachGlueck(), getPlugin());
        pm.registerEvents(new BergHuehner(), getPlugin());
        pm.registerEvents(new BlumenFuerDieGeliebte(), getPlugin());
        // pm.registerEvents(new RomanUndJulchen2(), getPlugin());
        pm.registerEvents(new StillePost(), getPlugin());
        pm.registerEvents(new WirFeiernEinFest(), getPlugin());
        pm.registerEvents(new FinteMitTinte(), getPlugin());
        pm.registerEvents(new Kuerbissuche(), getPlugin());
        pm.registerEvents(new Geheimnis(), getPlugin());
        pm.registerEvents(new RitterGoldFuss(), getPlugin());
        pm.registerEvents(new RitterGoldhelm(), getPlugin());
        pm.registerEvents(new HolzfaellerInDerHolzfalle(), getPlugin());
        pm.registerEvents(new Wiederaufbau(), getPlugin());
        pm.registerEvents(new Allgemeinwissen(), getPlugin());
        pm.registerEvents(new Forsthilfe(), getPlugin());

        pm.registerEvents(new Intro(), getPlugin());
    }

    public static void restartQuestIfTimeOver(QuestPlayer qp, Quest q, DialogMessage startDialog, DialogMessage endDialog) {
        if (qp.hasFinished(q)) {
            if (qp.checkCooldown(q)) {
                qp.unfinish(q);
                qp.setDialog(startDialog);
            } else qp.setDialog(endDialog);
        } else qp.setDialog(startDialog);
    }

}
