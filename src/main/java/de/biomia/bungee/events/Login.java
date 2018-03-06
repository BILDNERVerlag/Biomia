package de.biomia.bungee.events;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.cmds.Modus;
import de.biomia.bungee.specialEvents.WinterEvent;
import de.biomia.bungee.var.BanManager;
import de.biomia.bungee.var.Bans;
import de.biomia.universal.MySQL;
import de.biomia.universal.UniversalBiomia;
import de.biomia.universal.UniversalBiomiaPlayer;
import me.philipsnostrum.bungeepexbridge.BungeePexBridge;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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

        TimoCloudAPI.getUniversalInstance().getServerGroup("Lobby").getServers().forEach(each -> {
            if (each.getState().equalsIgnoreCase("ONLINE")) {
                isLobbyServerOnline = true;
            }
        });

        if (!isLobbyServerOnline) {
            pp.disconnect(new TextComponent("�cDer Server startet gerade!"));
            evt.setCancelled(true);
            return;
        }

        if (pp.getPendingConnection().getVersion() < 340) {
            pp.disconnect(new TextComponent("�cBitte nutze mindestens die Minecraft Version 1.12.2!"));
            evt.setCancelled(true);
            return;
        }

        if (pp.isForgeUser()) {
            pp.disconnect(new TextComponent("�cBitte nutze die Minecraft ohne Forge!"));
            return;
        }

        ArrayList<Bans> unbans = new ArrayList<>();
        BungeeMain.activeBans.forEach(eachBan -> {
            if (bp.getBiomiaPlayerID() == eachBan.getBiomiaID()) {
                if (eachBan.isPerm()) {
                    pp.disconnect(new TextComponent(
                            "�cDu wurdest von der Biomia Tec. f�r immmer verbannt!\n\n�cMit freundlichen Gr��en, dein �5Bio�2mia�7 Tec.�c Team!"));
                    evt.setCancelled(true);
                } else {
                    if (eachBan.getBis() > System.currentTimeMillis() / 1000) {
                        pp.disconnect(new TextComponent(
                                "�cDu wurdest von der Biomia Tec. verbannt!\nZeit bis du wieder eine Chance hast, auf unserem Netzwerk zu spielen:\n�e"
                                        + Time.toText((int) (eachBan.getBis() - System.currentTimeMillis() / 1000))
                                        + "\n\n�cMit freundlichen Gr��en, dein �5Bio�2mia�7 Tec.�c Team!"));
                        evt.setCancelled(true);
                    } else {
                        unbans.add(eachBan);
                    }
                }
            }
        });

        unbans.forEach(each -> BanManager.moveToCache(each, null));

        if (BungeeMain.plugin.getProxy().getOnlineCount() == 520) {

            ArrayList<ProxiedPlayer> lvl0 = new ArrayList<>();
            ArrayList<ProxiedPlayer> lvl1 = new ArrayList<>();

            try {
                BungeeCord.getInstance().getPlayers().forEach(each -> {

                    int lvl = -1;
                    try {
                        lvl = Integer.valueOf(UniversalBiomia.getRankLevel(BungeePexBridge.getPerms().getPlayerGroups(each).get(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (lvl == 0)
                        if (lvl0.size() < 20)
                            lvl0.add(each);
                        else
                            throw new BreakException();
                    else if (lvl == 1)
                        if (lvl0.size() < 20 || lvl1.size() < 20)
                            lvl1.add(each);
                });
            } catch (BreakException ignored) {
            }

            int i = 0;

            for (ProxiedPlayer ppl : lvl0) {
                if (i < 20) {
                    ppl.disconnect(new TextComponent(
                            "�cDu wurdest gekickt, um einem Spieler mit einem h�heren Rang Platz zu machen.\n�5Kauf dir Premium auf \n�2www.biomia.de\n�5um nicht mehr gekickt zu werden!"));
                    i++;
                } else
                    break;
            }
            if (i < 20) {
                for (ProxiedPlayer ppl : lvl1) {
                    if (i < 20) {
                        ppl.disconnect(new TextComponent(
                                "�cDu wurdest gekickt, um einem Spieler mit einem h�heren Rang Platz zu machen.\n�5Kauf dir Premium auf \n�2www.biomia.de\n�5um nicht mehr gekickt zu werden!"));
                        i++;
                    } else
                        break;
                }
            }
        }
        if (Modus.wm) {
            if (!pp.hasPermission("biomia.join")) {
                TextComponent msg = new TextComponent(wartungsmodus);
                pp.disconnect(msg);
                evt.setCancelled(true);
            }
        }

    }
}