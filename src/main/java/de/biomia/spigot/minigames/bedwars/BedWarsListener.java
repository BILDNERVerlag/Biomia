package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.game.GameDeathEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import de.biomia.spigot.events.game.bedwars.BedWarsUseShopEvent;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.WaitingLobbyListener;
import de.biomia.spigot.minigames.general.ColorType;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.minigames.general.shop.Shop;
import de.biomia.spigot.minigames.general.shop.ShopGroup;
import de.biomia.spigot.minigames.general.shop.ShopItem;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;

public class BedWarsListener extends GameHandler {

    public final ArrayList<Block> destroyableBlocks = new ArrayList<>();

    BedWarsListener(GameMode mode) {
        super(mode);
    }

    @EventHandler
    public void onVillagerDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.VILLAGER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        Player killer = p.getKiller();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        GameTeam team = bp.getTeam();
        e.setKeepInventory(false);
        if (!mode.isSpectator(bp)) {
            if (!((BedWarsTeam) team).hasBed()) {
                e.setDeathMessage(MinigamesMessages.playerDiedFinally.replace("%p", team.getColorcode() + p.getName()));
                team.setDead(bp);
                Bukkit.getPluginManager().callEvent(new GameDeathEvent(bp, Biomia.getBiomiaPlayer(killer), true, mode));
            } else {
                if (killer == null) {
                    e.setDeathMessage(MinigamesMessages.playerDied.replace("%p", team.getColorcode() + p.getName()));
                } else {
                    BiomiaPlayer bpKiller = Biomia.getBiomiaPlayer(killer);
                    e.setDeathMessage(String.format(MinigamesMessages.playerKilledByPlayer, team.getColorcode() + p.getName(), bpKiller.getTeam().getColorcode() + killer.getName()));
                    Bukkit.getPluginManager().callEvent(new GameKillEvent(bpKiller, bp, false, mode));
                }
            }
            Dead.respawn(p);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

        Location loc = ((BedWars) mode).starts.get(bp);
        if (loc != null && loc.distance(e.getTo()) > .5)
            ((BedWars) mode).starts.remove(bp);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        for (ArrayList uuid : ((BedWars) mode).handlerMap.values()) {
            uuid.remove(e.getPlayer());
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                ItemStack iStack = e.getCurrentItem();
                if (e.getInventory().getName().equals(BedWarsMessages.shopInventory)) {
                    for (ShopGroup group : Shop.getGroups()) {
                        if (iStack.equals(group.getIcon())) {
                            e.setCancelled(true);
                            p.openInventory(group.getInventory());
                            return;
                        }
                    }
                } else if (mode.getTeamSwitcher() != null && e.getClickedInventory().getName().equals(mode.getTeamSwitcher().getName())) {
                    mode.getTeamFromData(e.getCurrentItem().getData().getData()).join(bp);
                    e.setCancelled(true);
                    p.closeInventory();
                } else {
                    for (ShopGroup group : Shop.getGroups()) {
                        if (e.getClickedInventory().getName().equals(group.getFullName())) {
                            e.setCancelled(true);
                            if (iStack.hasItemMeta() && iStack.getItemMeta().hasDisplayName()
                                    && iStack.getItemMeta().getDisplayName().equals(BedWarsItemNames.back)) {
                                p.openInventory(Shop.getInventory());
                                return;
                            } else {
                                ShopItem shopItem = group.getShopItem(iStack);
                                if (shopItem != null) {
                                    GameTeam team = bp.getTeam();

                                    ItemStack returnItem = iStack.clone();

                                    if (shopItem.isColorble()) {
                                        if (shopItem.getType() == ColorType.LEATHER) {
                                            LeatherArmorMeta meta = (LeatherArmorMeta) returnItem.getItemMeta();
                                            switch (team.getColor()) {
                                                case BLACK:
                                                    meta.setColor(Color.BLACK);
                                                    break;
                                                case RED:
                                                    meta.setColor(Color.RED);
                                                    break;
                                                case BLUE:
                                                    meta.setColor(Color.BLUE);
                                                    break;
                                                case ORANGE:
                                                    meta.setColor(Color.ORANGE);
                                                    break;
                                                case GREEN:
                                                    meta.setColor(Color.GREEN);
                                                    break;
                                                case WHITE:
                                                    meta.setColor(Color.WHITE);
                                                    break;
                                                case PURPLE:
                                                    meta.setColor(Color.PURPLE);
                                                    break;
                                                case YELLOW:
                                                    meta.setColor(Color.YELLOW);
                                                    break;
                                                default:
                                                    break;
                                            }
                                            returnItem.setItemMeta(meta);
                                        } else {
                                            returnItem.setDurability(team.getColordata());
                                        }
                                    }
                                    if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {

                                        int i = iStack.getMaxStackSize() / iStack.getAmount();
                                        boolean first = true;

                                        for (int j = 0; j < i; j++) {
                                            if (shopItem.take(bp)) {
                                                p.getInventory().addItem(returnItem);
                                                first = false;
                                            } else if (first) {
                                                String name = ItemType.getName(shopItem.getItemType());
                                                p.sendMessage(BedWarsMessages.notEnoughItemsToPay.replace("%n", name));
                                                return;
                                            } else {
                                                return;
                                            }
                                        }

                                    } else if (shopItem.take(bp)) {
                                        p.getInventory().addItem(returnItem);
                                    } else {
                                        String name = ItemType.getName(shopItem.getItemType());
                                        p.sendMessage(BedWarsMessages.notEnoughItemsToPay.replace("%n", name));
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof Villager || e.getRightClicked() instanceof ArmorStand)
            if (e.getRightClicked().getCustomName().equals("Shop")) {
                e.setCancelled(true);
                Bukkit.getPluginManager().callEvent(new BedWarsUseShopEvent(Biomia.getBiomiaPlayer(p), e.getRightClicked() instanceof Villager, mode));
                p.openInventory(Shop.getInventory());
            } else if (e.getRightClicked().getCustomName().contains("Sekunden")) {
                e.setCancelled(true);
                p.openInventory(Shop.getInventory());
                ((BedWars) mode).handlerMap.get(e.getRightClicked().getUniqueId()).add(p);
            }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

        if (WaitingLobbyListener.inLobbyOrSpectator(bp) || !bp.canBuild()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BedWarsMessages.cantPlaceBlock);
            return;
        }

        destroyableBlocks.add(e.getBlock());
        if (e.getBlock().getType() == Material.ENDER_CHEST) {
            GameTeam team = bp.getTeam();
            if (team != null) {
                if (!((BedWars) mode).teamChestsLocs.containsKey(team)) {
                    ((BedWars) mode).teamChestsLocs.put(team, new ArrayList<>());
                }
                ((BedWars) mode).teamChestsLocs.get(team).add(e.getBlock());
            }
        }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (!e.isRepair())
            e.getInventory().setResult(ItemCreator.itemCreate(Material.AIR));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (WaitingLobbyListener.inLobbyOrSpectator(bp) || !bp.canBuild()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BedWarsMessages.cantDestroyThisBlock);
            return;
        }
        if (destroyableBlocks.contains(e.getBlock())) {
            destroyableBlocks.remove(e.getBlock());
        } else if (e.getBlock().getType() == Material.BED_BLOCK) {
            for (GameTeam gt : mode.getTeams()) {
                if (gt instanceof BedWarsTeam) {
                    BedWarsTeam bt = ((BedWarsTeam) gt);
                    if (bt.getBed().contains(e.getBlock())) {
                        if (bt.equals(bp.getTeam())) {
                            e.getPlayer().sendMessage(MinigamesMessages.destroyOwnBed);
                            e.setCancelled(true);
                            return;
                        }
                        bt.destroyBed();
                        Bukkit.broadcastMessage(bt.getColorcode() + ">>\u00A77Das Bett von " + bt.getColorcode() + "Team " + bt.getTeamname() + "\u00A77 wurde zerst\u00f6rt.");
                        e.setDropItems(false);
                        return;
                    }
                }
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawnNoOverload(PlayerRespawnEvent e) {
        e.getPlayer().getInventory().clear();
    }
}