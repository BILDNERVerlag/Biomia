package de.biomia.server.lobby.listener;

import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.biomia.Biomia;
import de.biomia.Main;
import de.biomia.server.lobby.Lobby;
import de.biomia.tools.PlayerToServerConnector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class Click implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent ie) {
        Player pl = (Player) ie.getWhoClicked();
        if (ie.getAction() == InventoryAction.PICKUP_ALL)
            if (ie.getClickedInventory().equals(Lobby.getNavigator())
                    || ie.getClickedInventory().getName().equals("\u00A7dLobby Switcher"))
                if (ie.getClick().isLeftClick())
                    if (ie.getCurrentItem() != null)
                        if (ie.getCurrentItem().hasItemMeta()) {
                            String itemName = ie.getCurrentItem().getItemMeta().getDisplayName();
                            if (itemName.equalsIgnoreCase("\u00A76Bau Welt")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 551.5, 80, 285.5, -90, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("\u00A7eDemo Welt")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 512, 80, 354, -50, 8));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("\u00A7cSpawn")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 534.5, 67, 193.5));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("\u00A75Biomia | general")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 473.5, 123, 359.5, -90, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("\u00A7bSkyWars")) {
                                pl.teleport(new Location(Bukkit.getWorld("SkywarsSignlobby"), 370.5, 82, 264.5, 70, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("\u00A74BedWars")) {
                                pl.teleport(new Location(Bukkit.getWorld("BedwarsSignlobby"), 370.5, 82, 264.5, 70, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("\u00A75Mysteri\u00F6se Box")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 605.5, 68, 358, 0, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("\u00A76Freebuild Welt")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 560, 96, 290, 80, 0));
                                pl.closeInventory();
                            } else if (ie.getClickedInventory().getName().equals("\u00A7dLobby Switcher"))
                                for (ServerObject so : Main.getUniversalTimoapi().getServerGroup("Lobby")
                                        .getServers())
                                    if (itemName.contains(so.getName()))
                                        if (!so.getName().equals(Main.getBukkitTimoapi().getThisServer().getName()))
                                            PlayerToServerConnector.connect(pl, so.getName());
                                        else
                                            pl.sendMessage("\u00A7cDu bist schon auf dieser Lobby!");
                        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(InventoryClickEvent ie) {
        if (ie.getCurrentItem() != null) {
            Player p = (Player) ie.getWhoClicked();
            if (!Biomia.getBiomiaPlayer(p).canBuild()) {
                ie.setCancelled(true);
                ie.setCursor(new ItemStack(Material.AIR));
            } else {
                ie.setCancelled(false);
            }

        }
    }
}
