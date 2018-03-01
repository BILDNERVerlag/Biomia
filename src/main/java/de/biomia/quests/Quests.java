package de.biomia.quests;

import de.biomia.quests.band1.*;
import de.biomia.quests.commands.QuestCommands;
import de.biomia.quests.general.NPCManager;
import de.biomia.quests.listeners.QuestListener;
import de.biomia.quests.general.DialogMessage;
import de.biomia.quests.general.Quest;
import de.biomia.quests.general.QuestPlayer;
import de.biomia.api.tools.LastPositionListener;
import de.biomia.api.messages.Scoreboards;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import static de.biomia.Main.getPlugin;

public class Quests {

    public static void initQuests() {

        registerQuestEvents();
        registerQuestCommands();

        Bukkit.getOnlinePlayers().forEach(Scoreboards::setTabList);

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

        QuestCommands questCommands = new QuestCommands();
        getPlugin().getCommand("q").setExecutor(questCommands);
        getPlugin().getCommand("qr").setExecutor(questCommands);
        getPlugin().getCommand("qlist").setExecutor(questCommands);
        getPlugin().getCommand("tagebuch").setExecutor(questCommands);
        getPlugin().getCommand("qinfo").setExecutor(questCommands);
        getPlugin().getCommand("qalign").setExecutor(questCommands);
        getPlugin().getCommand("qrestore").setExecutor(questCommands);
        getPlugin().getCommand("qhelp").setExecutor(questCommands);
        getPlugin().getCommand("qfilldiary").setExecutor(questCommands);
        getPlugin().getCommand("qstats").setExecutor(questCommands);
        getPlugin().getCommand("respawn").setExecutor(questCommands);
        getPlugin().getCommand("qupdatebook").setExecutor(questCommands);
        getPlugin().getCommand("qlog").setExecutor(questCommands);
        getPlugin().getCommand("qtest").setExecutor(questCommands);
        getPlugin().getCommand("qreset").setExecutor(questCommands);
        getPlugin().getCommand("aion").setExecutor(questCommands);
        getPlugin().getCommand("aioff").setExecutor(questCommands);
        getPlugin().getCommand("aitoggle").setExecutor(questCommands);

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
