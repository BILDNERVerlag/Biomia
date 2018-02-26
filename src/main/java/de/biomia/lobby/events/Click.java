package de.biomia.lobby.events;

import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.biomia.api.Biomia;
import de.biomia.api.connect.Connect;
import de.biomia.lobby.main.LobbyMain;
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
            if (ie.getClickedInventory().equals(LobbyMain.getNavigator())
                    || ie.getClickedInventory().getName().equals("00A7dLobby Switcher"))
                if (ie.getClick().isLeftClick())
                    if (ie.getCurrentItem() != null)
                        if (ie.getCurrentItem().hasItemMeta()) {
                            String itemName = ie.getCurrentItem().getItemMeta().getDisplayName();
                            if (itemName.equalsIgnoreCase("00A76Bau Welt")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 551.5, 80, 285.5, -90, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("00A7eDemo Welt")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 512, 80, 354, -50, 8));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("00A7cSpawn")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 534.5, 67, 193.5));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("00A75Biomia | general")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 473.5, 123, 359.5, -90, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("00A7bSkyWars")) {
                                pl.teleport(new Location(Bukkit.getWorld("SkywarsSignlobby"), 370.5, 82, 264.5, 70, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("00A74BedWars")) {
                                pl.teleport(new Location(Bukkit.getWorld("BedwarsSignlobby"), 370.5, 82, 264.5, 70, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("00A75Mysteri\u00F6se Box")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 605.5, 68, 358, 0, 0));
                                pl.closeInventory();
                            } else if (itemName.equalsIgnoreCase("00A76Freebuild Welt")) {
                                pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 560, 96, 290, 80, 0));
                                pl.closeInventory();
                            } else if (ie.getClickedInventory().getName().equals("00A7dLobby Switcher"))
                                for (ServerObject so : de.biomia.api.main.Main.getUniversalTimoapi().getServerGroup("Lobby")
                                        .getServers())
                                    if (itemName.contains(so.getName()))
                                        if (!so.getName().equals(de.biomia.api.main.Main.getBukkitTimoapi().getThisServer().getName()))
                                            Connect.connect(pl, so.getName());
                                        else
                                            pl.sendMessage("00A7cDu bist schon auf dieser Lobby!");
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
