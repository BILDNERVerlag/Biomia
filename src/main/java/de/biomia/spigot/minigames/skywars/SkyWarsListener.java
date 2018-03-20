package de.biomia.spigot.minigames.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.skywars.SkyWarsDeathEvent;
import de.biomia.spigot.events.skywars.SkyWarsKillEvent;
import de.biomia.spigot.events.skywars.SkyWarsLeaveEvent;
import de.biomia.spigot.events.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.messages.SkyWarsMessages;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.general.chests.Chests;
import de.biomia.spigot.minigames.general.kits.Kit;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.minigames.versus.games.skywars.VersusSkyWars;
import de.biomia.spigot.tools.BackToLobby;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.RankManager;
import de.biomia.universal.UniversalBiomia;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
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

        Player p = e.getPlayer();
        BackToLobby.getLobbyItem(p, 8);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDamageEntitys(false);
        bp.setGetDamage(false);

        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {
            p.getInventory().setItem(0, kitItem);
        }

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {

            GameStateManager.GameState state = mode.getStateManager().getActualGameState();

            if (state == GameStateManager.GameState.LOBBY) {

                String rank = RankManager.getRank(e.getPlayer());
                int i = Integer.valueOf(UniversalBiomia.getRankLevel(rank));

                if (i < 15) {

                    ArrayList<Player> player = new ArrayList<>(Bukkit.getOnlinePlayers());
                    player.forEach(eachPlayer -> {
                        if (Integer.valueOf(UniversalBiomia.getRankLevel(RankManager.getRank(eachPlayer))) > i) {
                            e.allow();
                            eachPlayer.sendMessage(MinigamesMessages.kickedForPremium);
                            eachPlayer.kickPlayer("");
                        }
                    });
                }
            } else if (state == GameStateManager.GameState.WAITING_FOR_START || state == GameStateManager.GameState.INGAME) {
                e.allow();
            }
        }
    }

    @EventHandler
    public void onHungerSwitch(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
                p.setFoodLevel(20);
                e.setCancelled(true);
            } else if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                p.setFoodLevel(20);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        } else if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {

        if (e.getEntity() instanceof Player) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getEntity());
            if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
                e.setCancelled(true);
            } else if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSwap(PlayerSwapHandItemsEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        } else if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        Player killer = p.getKiller();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
            BiomiaPlayer bpKiller;
            if (killer != null) {
                bpKiller = Biomia.getBiomiaPlayer(killer);
                Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(bpKiller, bp));
                e.setDeathMessage(MinigamesMessages.playerKilledByPlayer.replace("%p1", bp.getTeam().getColorcode() + p.getName()).replace("%p2", bpKiller.getTeam().getColorcode() + killer.getName()));
            } else {
                bpKiller = null;
                e.setDeathMessage(MinigamesMessages.playerDied.replace("%p",
                        bp.getTeam().getColorcode() + p.getName()));
            }
            bp.getTeam().setDead(bp);
            Bukkit.getPluginManager().callEvent(new SkyWarsDeathEvent(bp, bpKiller));
        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {

        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            if (e.getPlayer().getKiller() != null) {
                e.setRespawnLocation(e.getPlayer().getKiller().getLocation().add(0, 2, 0));
            } else {
                e.setRespawnLocation(new Location(Bukkit.getWorld(Variables.name), 0, 100, 0));
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

    @EventHandler
    public void onProjectileHit(PlayerEggThrowEvent event) {
        event.setHatching(false);
    }

    @EventHandler
    public void onProjectileThrow(ProjectileLaunchEvent event) {
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            event.setCancelled(true);
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
                    case SkyWarsItemNames.teamWaehlerItem:
                        p.openInventory(mode.getTeamSwitcher());
                        break;
                    case SkyWarsItemNames.startItem:
                        if (mode.getStateManager().getLobbyState().getCountDown() > 5)
                            mode.getStateManager().getLobbyState().setCountDown(5);
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
                    Bukkit.getPluginManager().callEvent(new SkyWarsOpenChestEvent(Biomia.getBiomiaPlayer(p), firstOpen, chestType));
                    e.setCancelled(true);
                    p.openInventory(chest.getInventory());
                }
            }
        }


        if (!(Biomia.getBiomiaPlayer(p).canBuild()) && !mode.getInstance().containsPlayer(bp) && !bp.getTeam().lives(bp)) {
            e.setCancelled(true);
        }
        if (!(Biomia.getBiomiaPlayer(p).canBuild()) && mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (mode.getStateManager().getActualGameState() == (GameStateManager.GameState.INGAME)) {
            if (mode.getInstance().containsPlayer(bp)) {
                Bukkit.getPluginManager().callEvent(new SkyWarsLeaveEvent(bp));
            }
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
                    if (killerbp.getTeam().containsPlayer(bp)) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }

                if (p.getHealth() <= e.getFinalDamage()) {

                    // Check if Player is instatnce of the act round
                    if (mode.getInstance().containsPlayer(bp)) {
                        e.setCancelled(true);

                        Bukkit.getPluginManager().callEvent(new SkyWarsDeathEvent(bp, killerbp));
                        Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(killerbp, bp));

                        Bukkit.broadcastMessage(String.format(MinigamesMessages.playerKilledByPlayer, bp.getTeam().getColorcode() + p.getName(), killerbp.getTeam().getColorcode() + killer.getName()));

                        Location loc = p.getLocation().add(0, 1, 0);

                        Arrays.asList(p.getInventory().getContents()).forEach(each -> p.getWorld().dropItem(loc, each));
                        Arrays.asList(p.getInventory().getArmorContents()).forEach(each -> p.getWorld().dropItem(loc, each));
                        p.getInventory().clear();
                        p.setHealth(20);

                    }
                }
            }
        }
    }

//    @EventHandler
//    public void onSignChange(SignChangeEvent e) {
//
//        if (e.getPlayer().hasPermission("biomia.leaderboard") && SkyWars.gameState != GameState.INGAME) {
//            if (e.getLine(0).equalsIgnoreCase("leaderboard")) {
//
//                String second = e.getLine(1);
//                            int i = 0;
//                try {
//                    second = second.replaceAll(" ", "");
//                                   i = Integer.valueOf(second);
//                } catch (Exception ex) {
//                    e.getPlayer().sendMessage(SkyWarsMessages.fillSecondLine);
//                }

//                Stats stat = Leaderboard.getStat(i);
//
//                if (stat != null) {
//
//                    double kd = stat.kills / stat.deaths;
//                    kd = ((double) Math.round(kd * 100) / 100);
//
//                    e.setLine(0, BedWarsMessages.rank.replaceAll("%rank", i + "").replaceAll("%p", stat.name));
//                    e.setLine(1, BedWarsMessages.wunGames + stat.wins);
//                    e.setLine(2, BedWarsMessages.kd + kd);
//                    e.setLine(3, BedWarsMessages.playedGames + stat.played_games);
//
//                    org.bukkit.material.Sign signData = (org.bukkit.material.Sign) e.getBlock().getState().getData();
//
//                    Block b = e.getBlock().getLocation().add(0, 1, 0).getBlock();
//
//                    b.setTypeIdAndData(Material.SKULL.getId(), Leaderboard.getFacingDirectionByte(signData.getFacing()),
//                            true);
//
//                    Skull s = (Skull) b.getState();
//                    s.setSkullType(SkullType.PLAYER);
//                    s.setOwner(stat.name);
//                    s.update(true);
//
//                    BedWarsVersusConfig.addSignsLocation(e.getBlock().getLocation(), i);
//
//                }
//            }
//        }
//    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInvClick(InventoryClickEvent ie) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) ie.getWhoClicked());
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            if (ie.getCurrentItem() != null) {
                if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                    if (!bp.canBuild()) {
                        ie.setCancelled(true);
                        ie.setCursor(new ItemStack(Material.AIR));
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