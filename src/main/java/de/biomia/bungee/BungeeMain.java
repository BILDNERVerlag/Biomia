package de.biomia.bungee;

import de.biomia.bungee.cmds.*;
import de.biomia.bungee.events.ChannelListener;
import de.biomia.bungee.events.Login;
import de.biomia.bungee.msg.Broadcasts;
import de.biomia.bungee.var.BanManager;
import de.biomia.bungee.var.Bans;
import de.biomia.universal.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class BungeeMain extends Plugin {

    public static final ArrayList<Thread> allThreads = new ArrayList<>();

    private final int bugRewardMoney = 1000;
    public static final int playerReportRewardMoney = 500;

    public static int actualFakePlayers = 0;

    public static Plugin plugin;

    public static ArrayList<Bans> activeBans = new ArrayList<>();
    public static ArrayList<Bans> cachedBans = new ArrayList<>();

    @Override
    public void onEnable() {

        plugin = this;

        ProxyServer.getInstance().registerChannel("BiomiaChannel");

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
                            obp.getProxiedPlayer().sendMessage(new TextComponent("\u00A75Bug erfolgreich bearbeitet, du erh00e4ltst \u00A72" + bugRewardMoney + " \u00A75Coins!"));
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

        Thread second = new Thread(() -> {
            while (true) {
                try {

                    String motd = MySQL.executeQuery("Select * from Motd", "text", MySQL.Databases.biomia_db);

                    Login.MOTD = new TextComponent(motd);

                    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) / 3;

                    double multiplicator = 1;

                    switch (hour) {
                    case 0:
                    case 1:
                    case 2:
                    default:
                        multiplicator *= 1;
                        break;
                    case 3:
                    case 4:
                        multiplicator *= 3;
                        break;
                    case 5:
                    case 7:
                        multiplicator *= 4;
                        break;
                    case 6:
                        multiplicator *= 7;
                        break;
                    case 8:
                        multiplicator *= 2;
                        break;
                    }

                    int min = 2 * (int) multiplicator;
                    int max = min + 5;

                    if (actualFakePlayers < min) {
                        actualFakePlayers += 1;
                    } else if (actualFakePlayers > max) {
                        actualFakePlayers -= 1;
                    } else {
                        if (new Random().nextBoolean()) {
                            actualFakePlayers += 1;
                        } else {
                            actualFakePlayers -= 1;
                        }
                    }
                    Thread.sleep(1000 * (new Random().nextInt(15) + 25));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        second.start();
        allThreads.add(second);
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
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BlistCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand("ban"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCommand("unban"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WasBannedCommand("wasbanned"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new RemoveReportCommand("removereport"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PingCommand("ping"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HelpCommand("help"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BiomiaCommand("biomia"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WorkloadCommand("workload"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BroadcastCommand("broadcast"));
    }

    private void registerEvents() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Login());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ChannelListener());
    }
}
