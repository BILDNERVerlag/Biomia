package de.biomia.bungee.events;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import de.biomia.bungee.BungeeBiomia;
import de.biomia.bungee.BungeeMain;
import de.biomia.bungee.OfflineBungeeBiomiaPlayer;
import de.biomia.bungee.cmds.ModusCommand;
import de.biomia.bungee.var.BanManager;
import de.biomia.bungee.var.Bans;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.universal.*;
import net.md_5.bungee.api.ProxyServer;
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

    public static TextComponent MOTD;

    private final TextComponent wartungsmodus = new TextComponent(String.format("%sDer Server ist im Wartungsmodus.\n%sBitte versuche es in einer Weile erneut!", Messages.COLOR_MAIN, Messages.COLOR_SUB));

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(ServerConnectEvent evt) {

        ProxiedPlayer pp = evt.getPlayer();
        OfflineBungeeBiomiaPlayer bp;


        if (UniversalBiomiaPlayer.getBiomiaPlayerID(pp.getUniqueId()) == -1) {
            MySQL.executeUpdate(String.format("INSERT INTO `BiomiaPlayer` (`uuid`, `name`) VALUES ('%s','%s')", pp.getUniqueId().toString(), pp.getName()), MySQL.Databases.biomia_db);
            bp = BungeeBiomia.getOfflineBiomiaPlayer(pp.getUniqueId());
            MySQL.executeUpdate(String.format("INSERT INTO `BiomiaCoins` (`ID`, `coins`) VALUES (%d, 0)", bp.getBiomiaPlayerID()), MySQL.Databases.biomia_db);
        } else {
            MySQL.executeUpdate(String.format("UPDATE `BiomiaPlayer` SET `name`='%s' WHERE uuid = '%s'", pp.getName(), pp.getUniqueId().toString()), MySQL.Databases.biomia_db);
            bp = BungeeBiomia.getOfflineBiomiaPlayer(pp.getUniqueId());
        }

        SkinValue.addToDatabase(bp);

        boolean isLobbyServerOnline = TimoCloudAPI.getUniversalAPI().getServerGroup(BiomiaServerType.Lobby.name()).getServers().stream().anyMatch(serverObject -> serverObject.getState().equalsIgnoreCase("ONLINE"));

        if (!isLobbyServerOnline) {
            pp.disconnect(new TextComponent("§cDer Server startet gerade!"));
            evt.setCancelled(true);
            return;
        }

        if (pp.getPendingConnection().getVersion() < 340) {
            pp.disconnect(new TextComponent("§cBitte nutze mindestens die Minecraft Version 1.12.2!"));
            evt.setCancelled(true);
            return;
        }

        ArrayList<Bans> unbans = new ArrayList<>();
        BungeeMain.activeBans.forEach(eachBan -> {
            if (bp.getBiomiaPlayerID() == eachBan.getBiomiaID()) {
                if (eachBan.isPerm()) {
                    pp.disconnect(new TextComponent(
                            "§cDu wurdest von der Biomia Tec. für immmer verbannt!\n\n§cMit freundlichen Grüßen, dein §5Bio§2mia§7 Tec.§c Team!"));
                    evt.setCancelled(true);
                } else {
                    if (eachBan.getLength() + eachBan.getTimestamp() > System.currentTimeMillis() / 1000) {
                        pp.disconnect(new TextComponent(
                                "§cDu wurdest von der Biomia Tec. verbannt!\nZeit bis du wieder eine Chance hast, auf unserem Netzwerk zu spielen:\n§e"
                                        + Time.toText((int) (eachBan.getLength() + eachBan.getTimestamp() - System.currentTimeMillis() / 1000))
                                        + "\n\n§cMit freundlichen Grüßen, dein §5Bio§2mia§7 Tec.§c Team!"));
                        evt.setCancelled(true);
                    } else {
                        unbans.add(eachBan);
                    }
                }
            }
        });

        unbans.forEach(each -> BanManager.moveToCache(each, null));

        if (ModusCommand.wartungsModus) {
            if (!bp.isSrStaff()) {
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
        ping.setPlayers(new ServerPing.Players(maxPlayers, ProxyServer.getInstance().getOnlineCount() + BungeeMain.actualFakePlayers, new ServerPing.PlayerInfo[0]));
        ping.setDescriptionComponent(MOTD);
        e.setResponse(ping);
    }
}