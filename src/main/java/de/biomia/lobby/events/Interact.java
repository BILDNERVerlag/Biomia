package de.biomia.lobby.events;

import de.biomia.lobby.Lobby;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.cosmetics.mystery.MysteryChest;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Interact implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent pi) {
        Player pl = pi.getPlayer().getPlayer();
        if ((pi.getAction() == Action.RIGHT_CLICK_AIR || pi.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            ItemStack itemstack = pl.getInventory().getItemInMainHand();
            if (itemstack.hasItemMeta()) {
                if ((itemstack.getType().equals(Material.COMPASS)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7cNavigator"))) {
                    pl.openInventory(Lobby.getNavigator());
                } else if ((itemstack.getType().equals(Material.NETHER_STAR)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7dLobby Switcher"))) {
                    pl.openInventory(Lobby.getLobbySwitcher());
                } else if ((itemstack.getType().equals(Material.FIREBALL)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7cSilent Lobby:\u00A78 Off"))) {
                    pl.getInventory().setItem(6,
                            ItemCreator.itemCreate(Material.FIREWORK_CHARGE, "\u00A7aSilent Lobby:\u00A78 On"));

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.hidePlayer(pl);
                        pl.hidePlayer(p);
                    }
                    Lobby.getSilentLobby().add(pl);

                } else if ((itemstack.getType().equals(Material.FIREWORK_CHARGE)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7aSilent Lobby:\u00A78 On"))) {
                    pl.getInventory().setItem(6, ItemCreator.itemCreate(Material.FIREBALL, "\u00A7cSilent Lobby:\u00A78 Off"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!Lobby.getSilentLobby().contains(p)) {
                            p.showPlayer(pl);
                            pl.showPlayer(p);
                        }
                    }
                    Lobby.getSilentLobby().remove(pl);
                } else if ((itemstack.getType().equals(Material.CHEST)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7eCosmetics"))) {
                    de.biomia.api.cosmetics.Cosmetic.openMainInventory(Biomia.getBiomiaPlayer(pl));
                }
            }
        }
        if (pi.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (pi.getClickedBlock().getType() == Material.CHEST) {
                pi.setCancelled(true);

                BiomiaPlayer bp = Biomia.getBiomiaPlayer(pi.getPlayer());

                int coins = bp.getCoins();
                if (coins >= 1000) {
                    bp.takeCoins(1000);
                    MysteryChest.open(bp);
                    bp.getPlayer().sendMessage(Messages.PREFIX
                            + "\u00A7aGl\u00fcckwunsch! Dir wurden 1000 BC abgezogen und du hast ein neues kosmetisches Item erhalten!");
                } else {
                    bp.getPlayer().sendMessage(Messages.PREFIX + "\u00A7aDu hast nicht genug Geld. Dir fehlen noch "
                            + (1000 - coins) + "\u00A7aBC!");
                }
            }
        }
    }
}