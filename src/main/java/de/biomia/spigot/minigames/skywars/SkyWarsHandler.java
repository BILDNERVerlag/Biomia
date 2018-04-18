package de.biomia.spigot.minigames.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.game.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.messages.SkyWarsMessages;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.WarteLobbyListener;
import de.biomia.spigot.minigames.general.chests.Chests;
import de.biomia.spigot.minigames.general.kits.Kit;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;

public class SkyWarsHandler extends GameHandler {

    private static final ItemStack kitItem = ItemCreator.itemCreate(Material.CHEST, SkyWarsItemNames.kitItemName);

    SkyWarsHandler(de.biomia.spigot.minigames.GameMode mode) {
        super(mode);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        super.onJoin(e);
        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY)
            e.getPlayer().getInventory().setItem(0, kitItem);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getHitEntity().getWorld())) return;
        if (e.getEntityType() != EntityType.SNOWBALL) {
            if (e.getEntityType() != EntityType.EGG) {
                return;
            }
        }
        Projectile projectile = e.getEntity();
        ProjectileSource shooter = projectile.getShooter();
        if (shooter == null) {
            return;
        }
        if ((shooter instanceof Player)) {
            if (!(e.getHitEntity() instanceof Damageable)) {
                return;
            }
            Player pShooter = (Player) shooter;
            pShooter.playSound(pShooter.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);

            if (projectile.getCustomName() != null && projectile.getCustomName().equals(SkyWarsItemNames.oneHitSnowball)) {
                ((Damageable) e.getHitEntity()).damage(0.5D, pShooter);
                ((Damageable) e.getHitEntity()).setHealth(0);
                ((Damageable) e.getHitEntity()).damage(0.5D, pShooter);
            } else if (projectile.getCustomName().equals(SkyWarsItemNames.gummipfeil)) {
                projectile.remove();
                ((Damageable) e.getHitEntity()).damage(0.1D, pShooter);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getWhoClicked().getWorld())) return;
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                    String name = e.getCurrentItem().getItemMeta().getDisplayName();

                    if (e.getClickedInventory().getName().equals(mode.getTeamSwitcher().getName())) {
                        mode.getTeamFromData(e.getCurrentItem().getData().getData()).join(bp);
                        e.setCancelled(true);
                        p.closeInventory();
                        return;
                    }

                    Kit kit = null;
                    String invName = e.getInventory().getName();
                    for (Kit allKits : KitManager.allKits.values()) {
                        if (invName.equals("Kits")) {
                            if (name.equals(allKits.getDisplayName())) {
                                p.openInventory(allKits.getSetupInv(p));
                                return;
                            }
                        }
                        if (invName.contains(allKits.getName())) {
                            kit = allKits;
                            break;
                        }
                    }
                    if (kit != null) {
                        switch (name) {
                            case SkyWarsItemNames.purchaseKit:
                                p.closeInventory();
                                KitManager kitManager = KitManager.getManager(bp);
                                if (kitManager.buy(kit)) {
                                    kitManager.selectSkyWarsKit(kit);
                                    p.sendMessage(SkyWarsMessages.youChoseKit.replace("%k", kit.getName()));
                                }
                                e.setCancelled(true);
                                break;
                            case SkyWarsItemNames.selectKit:
                                final ArrayList<Kit> kits = KitManager.getManager(bp).getAvailableKits();
                                if (kits.contains(kit)) {
                                    p.closeInventory();
                                    if (!KitManager.getManager(bp).selectSkyWarsKit(kit)) {
                                        p.sendMessage(SkyWarsMessages.kitAlreadyChosen);
                                    } else {
                                        p.sendMessage(SkyWarsMessages.youChoseKit.replace("%k", kit.getName()));
                                    }
                                } else {
                                    p.closeInventory();
                                    p.sendMessage(SkyWarsMessages.kitNotBought);
                                }
                                e.setCancelled(true);
                                break;
                            case SkyWarsItemNames.showKit:
                                KitManager.getManager(bp).showInventory(kit);
                                p.sendMessage(SkyWarsMessages.nowLookingAtKit.replace("%k", kit.getName()));
                                e.setCancelled(true);
                                break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        super.onPlayerInteract(e);
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (e.getItem() != null) {
            if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {

                String displayname = e.getItem().getItemMeta().getDisplayName();
                switch (displayname) {
                    case SkyWarsItemNames.playerTracker:
                        if (e.getItem().getType() == Material.COMPASS) {
                            for (Entity entity : p.getNearbyEntities(500, 500, 500)) {
                                if (entity instanceof Player) {
                                    Player nearest = (Player) entity;
                                    BiomiaPlayer nearestbp = Biomia.getBiomiaPlayer(nearest);
                                    if (!mode.isSpectator(nearestbp) && bp.getTeam() != null && !bp.getTeam().containsPlayer(nearestbp)) {
                                        p.setCompassTarget(nearest.getLocation());
                                        p.sendMessage(SkyWarsMessages.compassMessages.replace("%p", nearest.getName()).replace("%d", (int) p.getLocation().distance(nearest.getLocation()) + ""));
                                        return;
                                    }
                                }
                            }
                        }
                        break;
                    case SkyWarsItemNames.oneHitSnowball:
                        if (e.getItem().getType() == Material.SNOW_BALL) {
                            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                e.setCancelled(true);
                                Projectile ball = p.launchProjectile(Snowball.class);
                                ball.setCustomName(SkyWarsItemNames.oneHitSnowball);
                                ball.setShooter(p);
                                p.getInventory().remove(e.getItem());
                            }
                        }
                        break;
                    case SkyWarsItemNames.gummibogen:
                        if (e.getItem().getType() == Material.BOW) {
                            e.setCancelled(true);
                            Projectile arrow = p.launchProjectile(Arrow.class);
                            arrow.setCustomName(SkyWarsItemNames.gummipfeil);
                            arrow.setShooter(p);
                            p.getInventory().remove(e.getItem());
                        }
                        break;
                    case SkyWarsItemNames.kitItemName:
                        KitManager.getManager(bp).openKitMenu();
                        break;
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.CHEST) {


                Chest chest = (Chest) e.getClickedBlock().getState();
                boolean firstOpen = false;
                SkyWarsOpenChestEvent.ChestType chestType;

                Chests chests = ((SkyWars) mode).getChests();

                if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {

                    if (chests.fillChest(chest.getLocation())) {
                        firstOpen = true;
                    }
                    if (chests.isNormalChest(chest.getLocation())) {
                        chestType = SkyWarsOpenChestEvent.ChestType.NormalChest;
                    } else {
                        chestType = SkyWarsOpenChestEvent.ChestType.GoodChest;
                    }
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        Bukkit.getPluginManager().callEvent(new SkyWarsOpenChestEvent(Biomia.getBiomiaPlayer(p), firstOpen, chestType, mode));
                        e.setCancelled(true);
                        p.openInventory(chest.getInventory());
                    }
                }

            }
        }

        if (!(bp.canBuild()) && WarteLobbyListener.inLobbyOrSpectator(bp)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        if (mode.getInstance().containsPlayer(Biomia.getBiomiaPlayer(e.getPlayer())) && mode.getStateManager().getActualGameState() == GameStateManager.GameState.WAITING_FOR_START) {
            e.setCancelled(true);
        }
        super.onPlayerMove(e);
    }
}