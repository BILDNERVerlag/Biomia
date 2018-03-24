package de.biomia.spigot.minigames.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.events.game.GameDeathEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import de.biomia.spigot.events.game.GameLeaveEvent;
import de.biomia.spigot.events.game.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.messages.MinigamesItemNames;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.messages.SkyWarsMessages;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.WaitingLobbyListener;
import de.biomia.spigot.minigames.general.chests.Chests;
import de.biomia.spigot.minigames.general.kits.Kit;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.minigames.versus.games.skywars.VersusSkyWars;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.Arrays;

public class SkyWarsListener extends GameHandler {

    private static final ItemStack kitItem = ItemCreator.itemCreate(Material.CHEST, SkyWarsItemNames.kitItemName);

    SkyWarsListener(de.biomia.spigot.minigames.GameMode mode) {
        super(mode);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        super.onJoin(e);
        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY)
            e.getPlayer().getInventory().setItem(0, kitItem);
    }


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {

        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            if (e.getPlayer().getKiller() != null) {
                e.setRespawnLocation(e.getPlayer().getKiller().getLocation().add(0, 2, 0));
            } else {
                e.setRespawnLocation(new Location(Bukkit.getWorld(MinigamesConfig.getMapName()), 0, 100, 0));
            }
        } else {
            e.setRespawnLocation(new Location(Bukkit.getWorld("Spawn"), 0.5, 75, -0.5, 45, 0));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.SNOWBALL) {
            if (event.getEntityType() != EntityType.EGG) {
                return;
            }
        }
        Projectile projectile = event.getEntity();
        ProjectileSource shooter = projectile.getShooter();
        if (shooter == null) {
            return;
        }
        if ((shooter instanceof Player)) {
            if (!(event.getHitEntity() instanceof Damageable)) {
                return;
            }
            Player pShooter = (Player) shooter;
            pShooter.playSound(pShooter.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);

            if (projectile.getCustomName().equals(SkyWarsItemNames.oneHitSnowball)) {
                ((Damageable) event.getHitEntity()).damage(0.5D, pShooter);
                ((Damageable) event.getHitEntity()).setHealth(0);
                ((Damageable) event.getHitEntity()).damage(0.5D, pShooter);
            } else if (projectile.getCustomName().equals(SkyWarsItemNames.gummipfeil)) {
                projectile.remove();
                ((Damageable) event.getHitEntity()).damage(0.1D, pShooter);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

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

                    Kit kit = KitManager.getStandardKit();
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
                    switch (name) {
                        case SkyWarsItemNames.purchaseKit:
                            p.closeInventory();
                            KitManager kitManager = KitManager.getManager(bp);
                            if (kitManager.buy(kit)) {
                                kitManager.selectSkyWarsKit(kit);
                                p.sendMessage(SkyWarsMessages.youChoseKit.replace("%k", kit.getName()));
                            }
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
                            break;
                        case SkyWarsItemNames.showKit:
                            KitManager.getManager(bp).showInventory(kit);
                            p.sendMessage(SkyWarsMessages.nowLookingAtKit.replace("%k", kit.getName()));
                            break;
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (e.getItem() != null) {
            if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {

                String displayname = e.getItem().getItemMeta().getDisplayName();
                switch (displayname) {
                case MinigamesItemNames.teamWaehlerItem:
                    p.openInventory(mode.getTeamSwitcher());
                    break;
                case MinigamesItemNames.startItem:
                    if (mode.getStateManager().getLobbyState().getCountDown() > 5)
                        mode.getStateManager().getLobbyState().setCountDown(5);
                    break;
                case SkyWarsItemNames.playerTracker:
                        if (e.getItem().getType() == Material.COMPASS) {

                            for (Entity entity : p.getNearbyEntities(500, 500, 500)) {
                                if (entity instanceof Player) {
                                    Player nearest = (Player) entity;
                                    if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp) && bp.getTeam().containsPlayer(Biomia.getBiomiaPlayer(nearest))) {
                                        p.setCompassTarget(nearest.getLocation());
                                        p.sendMessage(SkyWarsMessages.compassMessages.replace("%p", nearest.getName())
                                                .replace("%d", (int) p.getLocation().distance(nearest.getLocation()) + ""));
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
                SkyWarsOpenChestEvent.ChestType chestType = SkyWarsOpenChestEvent.ChestType.NormalChest;

                Chests chests;

                if (mode instanceof SkyWars) {
                    chests = ((SkyWars) mode).getChests();
                } else {
                    chests = ((VersusSkyWars) mode).getChests();
                }

                if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {

                    if (chests.fillChest(chest.getLocation()))
                        firstOpen = true;
                    if (chests.isNormalChest(chest.getLocation())) {
                        chestType = SkyWarsOpenChestEvent.ChestType.NormalChest;
                    } else {
                        chestType = SkyWarsOpenChestEvent.ChestType.GoodChest;
                    }
                }

                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Bukkit.getPluginManager().callEvent(new SkyWarsOpenChestEvent(Biomia.getBiomiaPlayer(p), firstOpen, chestType, mode));
                    e.setCancelled(true);
                    p.openInventory(chest.getInventory());
                }
            }
        }

        if (!(bp.canBuild()) && WaitingLobbyListener.inLobbyOrSpectator(bp)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (!WaitingLobbyListener.inLobbyOrSpectator(bp)) {
            Bukkit.getPluginManager().callEvent(new GameLeaveEvent(bp, mode));
        }
        super.onDisconnect(e);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            if (e.getDamager() instanceof Player) {
                Player killer = (Player) e.getDamager();
                BiomiaPlayer killerbp = Biomia.getBiomiaPlayer(killer);

                if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME)
                    e.setCancelled(true);

                // Check if the Entity in the same team like the damager
                if (mode.getInstance().containsPlayer(killerbp) && mode.getInstance().containsPlayer(Biomia.getBiomiaPlayer(p))) {
                    if (killerbp.getTeam() != null && killerbp.getTeam().containsPlayer(bp)) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }

                if (p.getHealth() <= e.getFinalDamage()) {

                    // Check if Player is instatnce of the act round
                    if (mode.getInstance().containsPlayer(bp)) {
                        Bukkit.getPluginManager().callEvent(new GameDeathEvent(bp, killerbp, true, mode));
                        Bukkit.getPluginManager().callEvent(new GameKillEvent(killerbp, bp, true, mode));

                        Bukkit.broadcastMessage(String.format(MinigamesMessages.playerKilledByPlayer, bp.getTeam().getColorcode() + p.getName(), killerbp.getTeam().getColorcode() + killer.getName()));

                        Location loc = p.getLocation().add(0, 1, 0);

                        for (ItemStack itemStack : Arrays.asList(p.getInventory().getContents())) {
                            if (itemStack != null)
                                p.getWorld().dropItem(loc, itemStack);
                        }
                        for (ItemStack itemStack : Arrays.asList(p.getInventory().getArmorContents())) {
                            if (itemStack != null)
                                p.getWorld().dropItem(loc, itemStack);
                        }
                        p.getInventory().clear();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.WAITING_FOR_START) {
            e.setCancelled(true);
        }
        super.onPlayerMove(e);
    }
}