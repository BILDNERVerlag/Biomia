package de.biomia.spigot.listeners.servers;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.events.skywars.*;
import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.messages.SkyWarsMessages;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.general.chests.Chests;
import de.biomia.spigot.minigames.general.kits.Kit;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.minigames.skywars.SkyWars;
import de.biomia.spigot.minigames.skywars.var.Scoreboards;
import de.biomia.spigot.minigames.skywars.Variables;
import de.biomia.spigot.tools.BackToLobby;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.RankManager;
import de.biomia.spigot.tools.SkyWarsKitManager;
import de.biomia.universal.UniversalBiomia;
import org.bukkit.*;
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
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class SkyWarsListener extends GameHandler {

    public SkyWarsListener(de.biomia.spigot.minigames.GameMode mode) {
        super(mode);
    }

    @EventHandler
    public void onJoin_(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        BackToLobby.getLobbyItem(p, 8);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDamageEntitys(false);
        bp.setGetDamage(false);

        if (SkyWars.getSkyWars().getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {

            // Hide
            for (Player all : Bukkit.getOnlinePlayers()) {
                BiomiaPlayer allbp = Biomia.getBiomiaPlayer(all);
                GameTeam team = allbp.getTeam();
                if (team != null && team.lives(allbp)) {
                    all.hidePlayer(p);
                } else {
                    all.showPlayer(all);
                }
            }

            // Disable Damage / Build
            p.getInventory().clear();
            bp.setGetDamage(false);
            bp.setDamageEntitys(false);
            bp.setBuild(false);
            p.setGameMode(GameMode.ADVENTURE);

            // Fly settings
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setFlySpeed(0.4F);

            Scoreboards.setSpectatorSB(p);
            Scoreboards.spectatorSB.getTeam("spectator").addEntry(p.getName());

            p.teleport(new Location(Bukkit.getWorld(Variables.name), 0, 100, 0));

        } else if (SkyWars.getSkyWars().getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {

            p.teleport(Variables.warteLobbySpawn);
            if (p.hasPermission("biomia.sw.start")) {
                p.getInventory().setItem(1, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, SkyWarsItemNames.startItem));
            }

            int lastKit = SkyWarsKitManager.getLastSelectedKit(bp);

            if (lastKit != -1) {
                for (int entry : Variables.kits.keySet()) {
                    Kit kit = Variables.kits.get(entry);
                    if (kit.getID() == lastKit)
                        KitManager.getManager(bp).selectSkyWarsKit(kit);
                }
            } else {
                KitManager.getManager(bp).selectSkyWarsKit(Variables.standardKit);
            }

            p.getInventory().setItem(0, Variables.kitItem);
            p.getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, SkyWarsItemNames.teamWaehlerItem));

            bp.getPlayer().setLevel(SkyWars.getSkyWars().getStateManager().getLobbyState().getCountDown());

            if (bp.isPremium()) {
                Bukkit.broadcastMessage("\u00A76" + p.getName() + SkyWarsMessages.joinedTheGame);
            } else {
                Bukkit.broadcastMessage("\u00A77" + p.getName() + SkyWarsMessages.joinedTheGame);
            }
            SkyWars.getSkyWars().partyJoin(bp);
            Scoreboards.setLobbyScoreboard(p);
            Scoreboards.lobbySB.getTeam("xnoteam").addEntry(p.getName());

        }

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {

            GameStateManager.GameState state = SkyWars.getSkyWars().getStateManager().getActualGameState();

            if (state == GameStateManager.GameState.LOBBY) {

                String rank = RankManager.getRank(e.getPlayer());
                int i = Integer.valueOf(UniversalBiomia.getRankLevel(rank));

                if (i < 15) {

                    ArrayList<Player> player = new ArrayList<>(Bukkit.getOnlinePlayers());
                    player.forEach(eachPlayer -> {
                        if (Integer.valueOf(UniversalBiomia.getRankLevel(RankManager.getRank(eachPlayer))) > i) {
                            e.allow();
                            eachPlayer.sendMessage(SkyWarsMessages.kickedForPremium);
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
            if (SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
                p.setFoodLevel(20);
                e.setCancelled(true);
            } else if (!SkyWars.getSkyWars().getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                p.setFoodLevel(20);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        } else if (!SkyWars.getSkyWars().getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {

        if (e.getEntity() instanceof Player) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getEntity());
            if (SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
                e.setCancelled(true);
            } else if (!SkyWars.getSkyWars().getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSwap(PlayerSwapHandItemsEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        } else if (!SkyWars.getSkyWars().getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        Player killer = p.getKiller();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (SkyWars.getSkyWars().getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
            BiomiaPlayer bpKiller;
            if (killer != null) {
                bpKiller = Biomia.getBiomiaPlayer(killer);
                Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(bpKiller, bp));
                e.setDeathMessage(SkyWarsMessages.playerKilledByPlayer.replace("%p1", bp.getTeam().getColorcode() + p.getName()).replace("%p2", bpKiller.getTeam().getColorcode() + killer.getName()));
            } else {
                bpKiller = null;
                e.setDeathMessage(SkyWarsMessages.playerDied.replace("%p",
                        bp.getTeam().getColorcode() + p.getName()));
            }
            bp.getTeam().setDead(bp);
            Bukkit.getPluginManager().callEvent(new SkyWarsDeathEvent(bp, bpKiller));
        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {

        if (SkyWars.getSkyWars().getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            if (e.getPlayer().getKiller() != null) {
                e.setRespawnLocation(e.getPlayer().getKiller().getLocation().add(0, 2, 0));
            } else {
                e.setRespawnLocation(new Location(Bukkit.getWorld(Variables.name), 0, 100, 0));
            }
        } else {
            e.setRespawnLocation(Variables.warteLobbySpawn);
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
        if (SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
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

                    Kit kit = Variables.standardKit;

                    for (int entry : Variables.kits.keySet()) {
                        Kit k = Variables.kits.get(entry);
                        if (name.contains(k.getName())) {

                            if (e.getInventory().getName().contains("Kits")) {
                                p.openInventory(k.getSetupInv(p));
                                return;
                            }
                        }
                        if (e.getInventory().getName().substring(2).equals(k.getName()))
                            kit = k;
                    }

                    if (e.getInventory().getName().equals(SkyWars.getSkyWars().getTeamSwitcher().getName())) {

                        SkyWars.getSkyWars().getTeamFromData(e.getCurrentItem().getData().getData()).join(bp);
                        p.updateInventory();
                        p.closeInventory();

                    } else if (name.contains(SkyWarsItemNames.purchaseKitWithoutColors)
                            && kit.getSetupInv(p).equals(e.getInventory())) {
                        p.closeInventory();
                        if (KitManager.getManager(bp).buy(kit)) {
                            p.sendMessage(SkyWarsMessages.youChoseKit.replace("%k", kit.getName()));
                        }
                    } else if (name.contains(SkyWarsItemNames.selectKitWithoutColors)
                            && kit.getSetupInv(p).equals(e.getInventory())) {
                        if (Variables.availableKits.get(p) != null && Variables.availableKits.get(p).contains(kit)) {
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
                    } else if (name.contains(SkyWarsItemNames.showKitWithoutColors)
                            && kit.getSetupInv(p).equals(e.getInventory())) {
                        p.openInventory(kit.getDemoInv());
                        p.sendMessage(SkyWarsMessages.nowLookingAtKit.replace("%k", kit.getName()));
                        Bukkit.getPluginManager().callEvent(new KitShowEvent(Biomia.getBiomiaPlayer(p), kit.getID()));
                    }
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
                                    if (!SkyWars.getSkyWars().getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp) && bp.getTeam().containsPlayer(Biomia.getBiomiaPlayer(nearest))) {
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
                        p.openInventory(SkyWars.getSkyWars().getTeamSwitcher());
                        break;
                    case SkyWarsItemNames.startItem:
                        if (SkyWars.getSkyWars().getStateManager().getLobbyState().getCountDown() > 5)
                            SkyWars.getSkyWars().getStateManager().getLobbyState().setCountDown(5);
                        break;
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.CHEST) {
                Chest chest = (Chest) e.getClickedBlock().getState();
                boolean firstOpen = false;
                SkyWarsOpenChestEvent.ChestType chestType = SkyWarsOpenChestEvent.ChestType.NormalChest;
                if (!Variables.opendChests.contains(chest.getLocation()) && SkyWars.getSkyWars().getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
                    if (Variables.normalChestsFill.containsKey(chest)) {
                        chest.getInventory().setContents(Variables.normalChestsFill.get(chest));
                        Variables.opendChests.add(chest.getLocation());
                        firstOpen = true;
                        chestType = SkyWarsOpenChestEvent.ChestType.NormalChest;
                    } else if (Variables.goodChestsFill.containsKey(chest)) {
                        chest.getInventory().setContents(Variables.goodChestsFill.get(chest));
                        Variables.opendChests.add(chest.getLocation());
                        firstOpen = true;
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


        if (!(Biomia.getBiomiaPlayer(p).canBuild()) && !SkyWars.getSkyWars().getInstance().containsPlayer(bp) && !bp.getTeam().lives(bp)) {
            e.setCancelled(true);
        }
        if (!(Biomia.getBiomiaPlayer(p).canBuild()) && SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        }

        // Add Chests
        if (Variables.chestAddMode) {
            // Add Normal Chest
            if (e.getAction() == Action.LEFT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST
                    && !(p.isSneaking())) {
                Location loc = e.getClickedBlock().getLocation();
                Chest c = Chests.getChestByLoc(loc);

                e.setCancelled(true);

                if (!Variables.normalChests.contains(c)) {
                    new SkyWarsConfig().addChestLocation(loc, SkyWarsOpenChestEvent.ChestType.NormalChest);
                    p.sendMessage(SkyWarsItemNames.normaleTruheHinzu);
                } else {
                    p.sendMessage(SkyWarsItemNames.truheBereitsHinzu);
                }
                // Add Better Chest
            } else if ((e.getAction() == Action.LEFT_CLICK_BLOCK) && (e.getClickedBlock().getType() == Material.CHEST)
                    && (p.isSneaking())) {
                Location loc = e.getClickedBlock().getLocation();
                Chest c = Chests.getChestByLoc(loc);

                e.setCancelled(true);

                if (!Variables.goodChests.contains(c)) {
                    new SkyWarsConfig().addChestLocation(loc, SkyWarsOpenChestEvent.ChestType.GoodChest);
                    p.sendMessage(SkyWarsItemNames.bessereTruheHinzu);
                } else {
                    p.sendMessage(SkyWarsItemNames.truheBereitsHinzu);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (SkyWars.getSkyWars().getStateManager().getActualGameState() == (GameStateManager.GameState.INGAME)) {
            // Check if Player is instatnce of the act round
            if (SkyWars.getSkyWars().getInstance().containsPlayer(bp)) {
                bp.getTeam().setDead(bp);
                Bukkit.getPluginManager().callEvent(new SkyWarsLeaveEvent(bp));
            }
        } else if (SkyWars.getSkyWars().getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {
            // Remove Player from Team
            if (SkyWars.getSkyWars().getInstance().containsPlayer(bp)) {
                Scoreboards.lobbySB.getTeam("0" + bp.getTeam().getTeamname())
                        .removeEntry(p.getName());
                bp.getTeam().leave(bp);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            if (e.getDamager() instanceof Player) {
                Player killer = (Player) e.getDamager();
                BiomiaPlayer killerbp = Biomia.getBiomiaPlayer(killer);

                if (SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME)
                    e.setCancelled(true);

                // Check if the Entity in the same team like the damager
                if (SkyWars.getSkyWars().getInstance().containsPlayer(killerbp) && SkyWars.getSkyWars().getInstance().containsPlayer(Biomia.getBiomiaPlayer(p))) {
                    if (killerbp.getTeam().containsPlayer(bp)) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }

                if (p.getHealth() <= e.getFinalDamage()) {

                    // Check if Player is instatnce of the act round
                    if (SkyWars.getSkyWars().getInstance().containsPlayer(bp)) {
                        e.setCancelled(true);

                        Bukkit.getPluginManager().callEvent(new SkyWarsDeathEvent(bp, killerbp));
                        Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(killerbp, bp));

                        Bukkit.broadcastMessage(SkyWarsMessages.killedBy
                                .replace("%t", bp.getTeam().getColorcode() + p.getName())
                                .replace("%k", killerbp.getTeam().getColorcode() + killer.getName()));

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
        if (SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            if (ie.getCurrentItem() != null) {
                if (!SkyWars.getSkyWars().getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                    if (!bp.canBuild()) {
                        ie.setCancelled(true);
                        ie.setCursor(new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler
    public void Interact(PlayerInteractAtEntityEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        }

        if (e.getRightClicked() instanceof ArmorStand) {

//            ItemStack is = p.getInventory().getItemInMainHand();
//            if (is != null) {
//                if (is.hasItemMeta()) {
//                    if (is.getItemMeta().getDisplayName().equals(SkyWarsItemNames.teamJoinerSetter)) {
//
//                        if (is.getType().equals(Material.WOOL)) {
//
//                            GameTeam team = SkyWars.getSkyWars().getTeamFromData(is.getData().getData());
//
//                            if (team != null) {
//                                Entity armorstand = e.getRightClicked();
//                                SkyWarsConfig.addTeamJoiner(armorstand, team.getColor());
//                                armorstand.setCustomName(team.getColorcode() + team.getColor().translate());
//                                armorstand.setCustomNameVisible(true);
//                                p.sendMessage(SkyWarsMessages.teamJoinerSet.replace("%t", team.getTeamname()));
//                            }
//                        }
//                    }
//                }
//            }
            for (GameTeam allteams : SkyWars.getSkyWars().getTeams()) {

                Entity entity = Variables.joiner.get(allteams.getColor());
                if (e.getRightClicked().equals(entity)) {
                    allteams.join(bp);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if (SkyWars.getSkyWars().getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            if (e.getTo().getBlockY() <= 0) {
                e.getPlayer().setHealth(0);
            }
        } else if (e.getTo().getBlockY() <= 20) {
            e.getPlayer().teleport(Variables.warteLobbySpawn);
        }

        if (SkyWars.getSkyWars().getStateManager().getActualGameState() == GameStateManager.GameState.WAITING_FOR_START) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onSpawn(PlayerSpawnLocationEvent e) {
        e.setSpawnLocation(new Location(Bukkit.getWorld("Spawn"), 0.5, 75, -0.5, 40, 0));
    }
}