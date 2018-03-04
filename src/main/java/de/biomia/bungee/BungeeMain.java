package de.biomia.bungee;

import de.biomia.bungee.cmds.*;
import de.biomia.bungee.events.ChannelListener;
import de.biomia.bungee.events.Login;
import de.biomia.bungee.msg.Broadcasts;
import de.biomia.bungee.var.Bans;
import de.biomia.universal.MySQL;
import de.biomia.spigot.general.reportsystem.ReportSQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class BungeeMain extends Plugin {

    public static final ArrayList<Thread> allThreads = new ArrayList<>();

    private final int bugRewardMoney = 1000;
    public static final int playerReportRewardMoney = 500;

    public static Plugin plugin;

    public static final HashMap<String, Integer> ranks = new HashMap<>();
    public static ArrayList<Bans> activeBans = new ArrayList<>();
    public static ArrayList<Bans> cachedBans = new ArrayList<>();

    @Override
    public void onEnable() {

        plugin = this;

        BungeeCord.getInstance().registerChannel("BiomiaChannel");

        ReportSQL.getAllBans();
        ReportSQL.getAllCachedBans();

        registerCommands();
        registerEvents();
        initRanks();

        Broadcasts.startBroadcast();

        // WinterEvent
        // Calendar c = Calendar.getInstance();
        // int lastday;

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(900000);
                    // WinterEvent
//						if (c.get(Calendar.MONTH) == 11)
//							lastday = c.get(Calendar.DAY_OF_MONTH);
//						else
//							lastday = 1;
//						if (c.get(Calendar.MONTH) == 11) {
//							if (lastday < c.get(Calendar.DAY_OF_MONTH)) {
//								switch (lastday) {
//								case 3:
//									WinterEvent.randomWin(lastday);
//									break;
//								case 10:
//									WinterEvent.randomWin(lastday);
//									break;
//								case 17:
//									WinterEvent.randomWin(lastday);
//									break;
//								case 24:
//									WinterEvent.randomWin(lastday);
//									break;
//								default:
//									break;
//								}
//								lastday = c.get(Calendar.DAY_OF_MONTH);
//							}
//						}
                    for (String name : ReportSQL.getAllFinishedBugReports()) {
                        OfflineBungeeBiomiaPlayer obp = BungeeBiomia.getOfflineBiomiaPlayer(name);
                        obp.addCoins(bugRewardMoney, false);
                        if (obp.isOnline())
                            obp.getProxiedPlayer().sendMessage(new TextComponent("§5Bug erfolgreich bearbeitet, du erhältst §2" + bugRewardMoney + " §5Coins!"));
                    }
                    ReportSQL.removeAllFinishedBugReports();
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
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Modus("modus"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Register("register"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BungeeTP("gtp"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Ban("ban"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Unban("unban"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WasBanned("wasbanned"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new RemovePlayerReport("removereport"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Ping("ping"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Help("help"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BiomiaCommand("biomia"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Workload("workload"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Broadcast("broadcast"));
//        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Winter("winterwinner"));
    }

    private void registerEvents() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Login());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ChannelListener());
    }

    private void initRanks() {
        //FIXME
        ranks.put("UnregSpieler", 0);
        ranks.put("RegSpieler", 1);
        ranks.put("PremiumEins", 2);
        ranks.put("PremiumZwei", 3);
        ranks.put("PremiumDrei", 4);
        ranks.put("PremiumVier", 5);
        ranks.put("PremiumFuenf", 6);
        ranks.put("PremiumSechs", 7);
        ranks.put("PremiumSieben", 8);
        ranks.put("PremiumAcht", 9);
        ranks.put("PremiumNeun", 10);
        ranks.put("PremiumZehn", 11);
        ranks.put("YouTube", 12);
        ranks.put("Builder", 13);
        ranks.put("Moderator", 14);
        ranks.put("SrModerator", 15);
        ranks.put("SrBuilder", 16);
        ranks.put("Admin", 17);
        ranks.put("Owner", 18);
    }
}
