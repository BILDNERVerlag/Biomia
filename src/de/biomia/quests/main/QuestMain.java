package de.biomia.quests.main;

import de.biomia.quests.band1.*;
import de.biomia.quests.cmds.Commands;
import de.biomia.quests.listeners.QuestListener;
import de.biomia.quests.quests.band1.*;
import de.biomiaAPI.Quests.DialogMessage;
import de.biomiaAPI.Quests.Quest;
import de.biomiaAPI.Quests.QuestPlayer;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.msg.Scoreboards;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class QuestMain {

    public void initQuests() {

        registerCommands();
        registerQuestEvents();

        Bukkit.getOnlinePlayers().forEach(Scoreboards::setTabList);

        NPCManager.saveNPCLocations();

    }

    public void terminateQuests() {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "npc remove all");
    }

    private void registerQuestEvents() {

        PluginManager pm = Bukkit.getPluginManager();
        Main pl = Main.getPlugin();

        pm.registerEvents(new QuestListener(), pl);

        pm.registerEvents(new Wasserholen(), pl);
        pm.registerEvents(new ReiteDasSchwein(), pl);
        pm.registerEvents(new GehAngeln(), pl);
        pm.registerEvents(new AufDerSucheNachGlueck(), pl);
        pm.registerEvents(new BergHuehner(), pl);

        pm.registerEvents(new BlumenFuerDieGeliebte(), pl);
        // pm.registerEvents(new RomanUndJulchen2(), pl);

        pm.registerEvents(new StillePost(), pl);
        pm.registerEvents(new WirFeiernEinFest(), pl);
        pm.registerEvents(new FinteMitTinte(), pl);
        pm.registerEvents(new Kuerbissuche(), pl);
        pm.registerEvents(new Geheimnis(), pl);
        pm.registerEvents(new RitterGoldFuss(), pl);
        pm.registerEvents(new RitterGoldhelm(), pl);
        pm.registerEvents(new HolzfaellerInDerHolzfalle(), pl);
        pm.registerEvents(new Wiederaufbau(), pl);
        pm.registerEvents(new Allgemeinwissen(), pl);

        pm.registerEvents(new Intro(), pl);

        // unfertige
        pm.registerEvents(new Forsthilfe(), pl);
    }

    private void registerQuestCommands() {

        getCommand("q").setExecutor(new Commands());
        getCommand("qr").setExecutor(new Commands());
        getCommand("qlist").setExecutor(new Commands());
        getCommand("tagebuch").setExecutor(new Commands());
        getCommand("qinfo").setExecutor(new Commands());
        getCommand("qalign").setExecutor(new Commands());
        getCommand("qrestore").setExecutor(new Commands());
        getCommand("aion").setExecutor(new Commands());
        getCommand("aioff").setExecutor(new Commands());
        getCommand("aitoggle").setExecutor(new Commands());
        getCommand("qhelp").setExecutor(new Commands());
        getCommand("qfilldiary").setExecutor(new Commands());
        getCommand("qstats").setExecutor(new Commands());
        getCommand("respawn").setExecutor(new Commands());
        getCommand("qupdatebook").setExecutor(new Commands());
        getCommand("qlog").setExecutor(new Commands());
        getCommand("qtest").setExecutor(new Commands());
        getCommand("qreset").setExecutor(new Commands());

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
