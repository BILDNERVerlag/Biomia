package de.biomia.bungee;

import de.biomia.bungee.cmds.*;
import de.biomia.bungee.events.ChannelListener;
import de.biomia.bungee.events.Login;
import de.biomia.bungee.msg.Broadcasts;
import de.biomia.bungee.specialEvents.Winter;
import de.biomia.bungee.specialEvents.WinterEvent;
import de.biomia.bungee.var.BanManager;
import de.biomia.bungee.var.Bans;
import de.biomia.universal.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;

public class BungeeMain extends Plugin {

    public static final ArrayList<Thread> allThreads = new ArrayList<>();

    private final int bugRewardMoney = 1000;
    public static final int playerReportRewardMoney = 500;

    public static Plugin plugin;

    public static ArrayList<Bans> activeBans = new ArrayList<>();
    public static ArrayList<Bans> cachedBans = new ArrayList<>();

    @Override
    public void onEnable() {

        plugin = this;

        BungeeCord.getInstance().registerChannel("BiomiaChannel");

        BanManager.getAllBans();
        BanManager.getAllCachedBans();

        registerCommands();
        registerEvents();

        Broadcasts.startBroadcast();


        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(900000);
                    for (String name : BanManager.getAllFinishedBugReports()) {
                        OfflineBungeeBiomiaPlayer obp = BungeeBiomia.getOfflineBiomiaPlayer(name);
                        obp.addCoins(bugRewardMoney, false);
                        if (obp.isOnline())
                            obp.getProxiedPlayer().sendMessage(new TextComponent("�5Bug erfolgreich bearbeitet, du erh�ltst �2" + bugRewardMoney + " �5Coins!"));
                    }
                    BanManager.removeAllFinishedBugReports();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        thread.start();
        allThreads.add(thread);
    }

    @Override
    public void onDisable() {
        MySQL.closeConnections();
        allThreads.forEach(Thread::interrupt);
    }

    private void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ModusCommand("modus"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new RegisterCommand("register"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new GTPCommand("gtp"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand("ban"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCommand("unban"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WasBannedCommand("wasbanned"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new RemoveReportCommand("removereport"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PingCommand("ping"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HelpCommand("help"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BiomiaCommand("biomia"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WorkloadCommand("workload"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BroadcastCommand("broadcast"));

        if (WinterEvent.isEnabled)
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new Winter("winterwinner"));
    }

    private void registerEvents() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Login());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ChannelListener());
    }
}
