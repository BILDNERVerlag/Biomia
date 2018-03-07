package de.biomia.spigot.server.quests;

import de.biomia.spigot.Main;
import de.biomia.spigot.commands.quest.QuestCommandsInArbeit;
import de.biomia.spigot.server.quests.band1.*;
import de.biomia.spigot.server.quests.general.DialogMessage;
import de.biomia.spigot.server.quests.general.NPCManager;
import de.biomia.spigot.server.quests.general.Quest;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.biomia.spigot.listeners.servers.QuestListener;
import de.biomia.spigot.tools.LastPositionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import static de.biomia.spigot.Main.getPlugin;

public class Quests {

    public static void initQuests() {

        registerQuestEvents();
        registerQuestCommands();

        NPCManager.saveNPCLocations();

    }

    public static void terminateQuests() {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "npc remove all");
    }

    private static void registerQuestEvents() {

        PluginManager pm = Bukkit.getPluginManager();

        Bukkit.getPluginManager().registerEvents(new LastPositionListener(), getPlugin());

        pm.registerEvents(new QuestListener(), getPlugin());

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

        pm.registerEvents(new Intro(), getPlugin());

        // unfertige
        pm.registerEvents(new Forsthilfe(), getPlugin());
    }

    private static void registerQuestCommands() {

        Main.registerCommand(new QuestCommandsInArbeit());
        //TODO set cmds to args
//        Main.registerCommand(new QuestCommands("q"));
//        Main.registerCommand(new QuestCommands("qr"));
//        Main.registerCommand(new QuestCommands("qlist"));
//        Main.registerCommand(new QuestCommands("tagebuch"));
//        Main.registerCommand(new QuestCommands("qinfo"));
//        Main.registerCommand(new QuestCommands("qalign"));
//        Main.registerCommand(new QuestCommands("qrestore"));
//        Main.registerCommand(new QuestCommands("qhelp"));
//        Main.registerCommand(new QuestCommands("qfilldiary"));
//        Main.registerCommand(new QuestCommands("qstats"));
//        Main.registerCommand(new QuestCommands("respawn"));
//        Main.registerCommand(new QuestCommands("qupdatebook"));
//        Main.registerCommand(new QuestCommands("qlog"));
//        Main.registerCommand(new QuestCommands("qtest"));
//        Main.registerCommand(new QuestCommands("qreset"));
//        Main.registerCommand(new QuestCommands("aion"));
//        Main.registerCommand(new QuestCommands("aioff"));
//        Main.registerCommand(new QuestCommands("aitoggle"));

    }

    public static void restartQuestIfTimeOver(QuestPlayer qp, Quest q, DialogMessage startDialog,
                                              DialogMessage endDialog) {
        if (qp.hasFinished(q)) {
            if (qp.checkCooldown(q)) {
                qp.unfinish(q);
                qp.setDialog(startDialog);
            } else {
                qp.setDialog(endDialog);
            }
        } else {
            qp.setDialog(startDialog);
        }
    }

}
