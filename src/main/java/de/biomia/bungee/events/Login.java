package de.biomia.bungee.events;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.cmds.ModusCommand;
import de.biomia.bungee.specialEvents.WinterEvent;
import de.biomia.bungee.var.BanManager;
import de.biomia.bungee.var.Bans;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.universal.MySQL;
import de.biomia.universal.UniversalBiomia;
import de.biomia.universal.UniversalBiomiaPlayer;
import me.philipsnostrum.bungeepexbridge.BungeePexBridge;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;

public class Login implements Listener {

    private static boolean isLobbyServerOnline;
    private final TextComponent wartungsmodus = new TextComponent(ChatColor.AQUA + "Der Server ist im Wartungsmodus.\n" + ChatColor.RED + "Bitte versuche es in einer Weile erneut!");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(ServerConnectEvent evt) {

        ProxiedPlayer pp = evt.getPlayer();
        OfflineBungeeBiomiaPlayer bp;

        if (!UniversalBiomiaPlayer.isPlayerRegistered(pp.getUniqueId())) {
            MySQL.executeUpdate("INSERT INTO `BiomiaPlayer` (`uuid`, `name`) VALUES ('" + pp.getUniqueId().toString() + "','" + pp.getName() + "')", MySQL.Databases.biomia_db);
            bp = BungeeBiomia.getOfflineBiomiaPlayer(pp.getUniqueId());
            MySQL.executeUpdate("INSERT INTO `BiomiaCoins` (`ID`, `coins`) VALUES (" + bp.getBiomiaPlayerID() + ", 0)", MySQL.Databases.biomia_db);
        } else {
            MySQL.executeUpdate("UPDATE `BiomiaPlayer` SET `name`='" + pp.getName() + "' WHERE uuid = '" + pp.getUniqueId().toString() + "'", MySQL.Databases.biomia_db);
            bp = BungeeBiomia.getOfflineBiomiaPlayer(pp.getUniqueId());
        }

        if (WinterEvent.isEnabled) {
            WinterEvent.isWinner(bp);
        }

        isLobbyServerOnline = false;

        TimoCloudAPI.getUniversalInstance().getServerGroup(BiomiaServerType.Lobby.name()).getServers().forEach(each -> {
            if (each.getState().equalsIgnoreCase("ONLINE")) {
                isLobbyServerOnline = true;
            }
        });

        if (!isLobbyServerOnline) {
            pp.disconnect(new TextComponent("\u00A7cDer Server startet gerade!"));
            evt.setCancelled(true);
            return;
        }

        if (pp.getPendingConnection().getVersion() < 340) {
            pp.disconnect(new TextComponent("\u00A7cBitte nutze mindestens die Minecraft Version 1.12.2!"));
            evt.setCancelled(true);
            return;
        }

        if (pp.isForgeUser()) {
            pp.disconnect(new TextComponent("\u00A7cBitte nutze die Minecraft ohne Forge!"));
            return;
        }

        ArrayList<Bans> unbans = new ArrayList<>();
        BungeeMain.activeBans.forEach(eachBan -> {
            if (bp.getBiomiaPlayerID() == eachBan.getBiomiaID()) {
                if (eachBan.isPerm()) {
                    pp.disconnect(new TextComponent(
                            "\u00A7cDu wurdest von der Biomia Tec. f\u00fcr immmer verbannt!\n\n\u00A7cMit freundlichen Gr\u00fc\u00dfen, dein \u00A75Bio\u00A72mia\u00A77 Tec.\u00A7c Team!"));
                    evt.setCancelled(true);
                } else {
                    if (eachBan.getBis() > System.currentTimeMillis() / 1000) {
                        pp.disconnect(new TextComponent(
                                "\u00A7cDu wurdest von der Biomia Tec. verbannt!\nZeit bis du wieder eine Chance hast, auf unserem Netzwerk zu spielen:\n\u00A7e"
                                        + Time.toText((int) (eachBan.getBis() - System.currentTimeMillis() / 1000))
                                        + "\n\n\u00A7cMit freundlichen Gr\u00fc\u00dfen, dein \u00A75Bio\u00A72mia\u00A77 Tec.\u00A7c Team!"));
                        evt.setCancelled(true);
                    } else {
                        unbans.add(eachBan);
                    }
                }
            }
        });

        unbans.forEach(each -> BanManager.moveToCache(each, null));

        if (BungeeMain.plugin.getProxy().getOnlineCount() == 520) {

            ArrayList<ProxiedPlayer> lvl1 = new ArrayList<>();
            ArrayList<ProxiedPlayer> lvl2 = new ArrayList<>();

            try {
                BungeeCord.getInstance().getPlayers().forEach(each -> {

                    int lvl;
                    try {
                        lvl = UniversalBiomia.getRankLevel(BungeePexBridge.getPerms().getPlayerGroups(each).get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    if (lvl == 1)
                        if (lvl1.size() < 20)
                            lvl1.add(each);
                        else
                            throw new BreakException();
                    else if (lvl == 2)
                        if (lvl1.size() < 20 || lvl2.size() < 20)
                            lvl2.add(each);
                });
            } catch (BreakException ignored) {
            }

            int i = 0;

            for (ProxiedPlayer ppl : lvl1) {
                if (i < 20) {
                    ppl.disconnect(new TextComponent(
                            "\u00A7cDu wurdest gekickt, um einem Spieler mit einem h\u00f6heren Rang Platz zu machen.\n\u00A75Kauf dir Premium auf \n\u00A72www.biomia.de\n\u00A75um nicht mehr gekickt zu werden!"));
                    i++;
                } else
                    break;
            }
            if (i < 20) {
                for (ProxiedPlayer ppl : lvl2) {
                    if (i < 20) {
                        ppl.disconnect(new TextComponent(
                                "\u00A7cDu wurdest gekickt, um einem Spieler mit einem h\u00f6heren Rang Platz zu machen.\n\u00A75Kauf dir Premium auf \n\u00A72www.biomia.de\n\u00A75um nicht mehr gekickt zu werden!"));
                        i++;
                    } else
                        break;
                }
            }
        }
        if (ModusCommand.wm) {
            if (!pp.hasPermission("biomia.join")) {
                TextComponent msg = new TextComponent(wartungsmodus);
                pp.disconnect(msg);
                evt.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPingEvent(ProxyPingEvent e) {
        int maxPlayers = e.getResponse().getPlayers().getMax();
        ServerPing ping = e.getResponse();
        ping.setPlayers(new ServerPing.Players(maxPlayers, BungeeCord.getInstance().getOnlineCount() + BungeeMain.actualFakePlayers, new ServerPing.PlayerInfo[0]));
        e.setResponse(ping);
    }
}