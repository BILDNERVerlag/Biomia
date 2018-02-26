package de.biomia.sw.listeners;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.Teams.Team;
import de.biomia.api.achievements.statEvents.skywars.*;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.mysql.MySQL;
import de.biomia.api.pex.Rank;
import de.biomia.api.tools.BackToLobby;
import de.biomia.sw.chests.Chests;
import de.biomia.sw.gamestates.GameState;
import de.biomia.sw.gamestates.InGame;
import de.biomia.sw.ingame.Dead;
import de.biomia.sw.kits.Kit;
import de.biomia.sw.kits.Kits;
import de.biomia.sw.lobby.JoinTeam;
import de.biomia.sw.main.SkyWarsMain;
import de.biomia.sw.messages.ItemNames;
import de.biomia.sw.messages.Messages;
import de.biomia.sw.var.Config;
import de.biomia.sw.var.Scoreboards;
import de.biomia.sw.var.Variables;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class SkyWarsListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        BackToLobby.getLobbyItem(p, 8);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDamageEntitys(false);
        bp.setGetDamage(false);

        if (SkyWarsMain.gameState.equals(GameState.INGAME)) {

            // Hide
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (Biomia.TeamManager().livesPlayer(all)) {
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

            Variables.spectator.add(p);
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!pl.equals(p)) {
                    pl.hidePlayer(p);
                }
            }
            for (Player pl : Variables.spectator) {
                pl.hidePlayer(p);
            }
            Scoreboards.setSpectatorSB(p);
            Scoreboards.spectatorSB.getTeam("spectator").addEntry(p.getName());

            p.teleport(new Location(Bukkit.getWorld(Variables.name), 0, 100, 0));

        } else if (SkyWarsMain.gameState.equals(GameState.LOBBY)) {

            Kits.loadKits(p);
            p.teleport(Variables.warteLobbySpawn);

            if (p.hasPermission("biomia.sw.start")) {

                p.getInventory().setItem(1, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, ItemNames.startItem));

            }

            int lastKit = MySQL.executeQuerygetint(
                    "SELECT * from `LSSkyWarsKit` where biomiaID = " + Biomia.getBiomiaPlayer(p).getBiomiaPlayerID(), "kit", MySQL.Databases.biomia_db);

            if (lastKit != -1) {
                for (int entry : Variables.kits.keySet()) {
                    Kit kit = Variables.kits.get(entry);
                    if (kit.getID() == lastKit)
                        Kits.selectSkyWarsKit(p, kit);
                }
            } else {
                Kits.selectSkyWarsKit(p, Variables.standardKit);
            }

            p.getInventory().setItem(0, Variables.kitItem);
            p.getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, ItemNames.teamWaehlerItem));

            bp.getPlayer().setLevel(Variables.countDown.getCountdown());

            if (bp.isPremium()) {
                Bukkit.broadcastMessage("\u00A76" + p.getName() + Messages.joinedTheGame);
            } else {
                Bukkit.broadcastMessage("\u00A77" + p.getName() + Messages.joinedTheGame);
            }
            if (bp.isPartyLeader()) {
                if (bp.getParty().getAllPlayers().size() > 1) {
                    JoinTeam.partyJoin(bp);
                }
            }
            Scoreboards.setLobbyScoreboard(p);
            Scoreboards.lobbySB.getTeam("noteam").addEntry(p.getName());

        }

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {

            if (SkyWarsMain.gameState.equals(GameState.LOBBY)) {

                String rank = Rank.getRank(e.getPlayer());
                int i = Integer.valueOf(Rank.getRankID(rank));

                if (i < 15) {

                    ArrayList<Player> player = new ArrayList<>(Bukkit.getOnlinePlayers());
                    player.forEach(eachPlayer -> {
                        if (Integer.valueOf(Rank.getRankID(Rank.getRank(eachPlayer))) > i) {

                            e.allow();
                            eachPlayer.sendMessage(Messages.kickedForPremium);
                            eachPlayer.kickPlayer("");
                        }
                    });
                }
            } else if (SkyWarsMain.gameState.equals(GameState.WAITINGFORSTART)
                    || SkyWarsMain.gameState.equals(GameState.INGAME)) {
                e.allow();
            }
        }
    }

    @EventHandler
    public void onHungerSwitch(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (!SkyWarsMain.gameState.equals(GameState.INGAME)) {
                p.setFoodLevel(20);
                e.setCancelled(true);
            } else if (!Variables.livingPlayer.contains(p)) {
                p.setFoodLevel(20);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (!SkyWarsMain.gameState.equals(GameState.INGAME)) {
            e.setCancelled(true);
        } else if (!Variables.livingPlayer.contains(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (!SkyWarsMain.gameState.equals(GameState.INGAME)) {
                e.setCancelled(true);
            } else if (!Variables.livingPlayer.contains(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSwap(PlayerSwapHandItemsEvent e) {
        if (!SkyWarsMain.gameState.equals(GameState.INGAME)) {
            e.setCancelled(true);
        } else if (!Variables.livingPlayer.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        Player killer = p.getKiller();

        if (Variables.livingPlayer.contains(p)) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            BiomiaPlayer bpKiller;
            if (killer != null) {
                bpKiller = Biomia.getBiomiaPlayer(killer);
                Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(bpKiller, bp));
                e.setDeathMessage(Messages.playerKilledByPlayer
                        .replace("%p1", Biomia.TeamManager().getTeam(p).getColorcode() + p.getName())
                        .replace("%p2", Biomia.TeamManager().getTeam(killer).getColorcode() + killer.getName()));
            } else {
                bpKiller = null;
                e.setDeathMessage(Messages.playerDied.replace("%p",
                        Biomia.TeamManager().getTeam(p).getColorcode() + p.getName()));
            }
            Dead.setDead(p, killer);
            Bukkit.getPluginManager().callEvent(new SkyWarsDeathEvent(bp, bpKiller));
        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {

        if (SkyWarsMain.gameState == GameState.INGAME) {
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

            if (projectile.getCustomName().equals(ItemNames.oneHitSnowball)) {
                ((Damageable) event.getHitEntity()).damage(0.5D, pShooter);
                ((Damageable) event.getHitEntity()).setHealth(0);
                ((Damageable) event.getHitEntity()).damage(0.5D, pShooter);
            } else if (projectile.getCustomName().equals(ItemNames.gummipfeil)) {
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

        if (!(SkyWarsMain.gameState == GameState.INGAME)) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();

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

                    if (e.getInventory().getName().equals(Variables.teamJoiner.getName())) {

                        Variables.teamJoiner = JoinTeam.getTeamSwitcher();
                        JoinTeam.join(p, Biomia.TeamManager().DataToTeam(e.getCurrentItem().getData().getData()));
                        p.updateInventory();
                        p.closeInventory();

                    } else if (name.contains(ItemNames.purchaseKitWithoutColors)
                            && kit.getSetupInv(p).equals(e.getInventory())) {
                        p.closeInventory();
                        if (kit.buy(Biomia.getBiomiaPlayer(p))) {
                            Kits.selectSkyWarsKit(p, kit);
                        }
                        p.sendMessage(Messages.youChoseKit.replace("%k", kit.getName()));
                    } else if (name.contains(ItemNames.selectKitWithoutColors)
                            && kit.getSetupInv(p).equals(e.getInventory())) {
                        if (Variables.availableKits.get(p) != null && Variables.availableKits.get(p).contains(kit)) {
                            p.closeInventory();
                            if (!Kits.selectSkyWarsKit(p, kit)) {
                                p.sendMessage(Messages.kitAlreadyChosen);
                            } else {
                                p.sendMessage(Messages.youChoseKit.replace("%k", kit.getName()));
                            }
                        } else {
                            p.closeInventory();
                            p.sendMessage(Messages.kitNotBought);
                        }
                    } else if (name.contains(ItemNames.showKitWithoutColors)
                            && kit.getSetupInv(p).equals(e.getInventory())) {
                        p.openInventory(kit.getDemoInv());
                        p.sendMessage(Messages.nowLookingAtKit.replace("%k", kit.getName()));
                        Bukkit.getPluginManager().callEvent(new KitShowEvent(Biomia.getBiomiaPlayer(p), kit.getID()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        if (e.getItem() != null) {
            if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {

                String displayname = e.getItem().getItemMeta().getDisplayName();
                switch (displayname) {
                    case ItemNames.playerTracker:
                        if (e.getItem().getType() == Material.COMPASS) {

                            for (Entity entity : p.getNearbyEntities(500, 500, 500)) {
                                if (entity instanceof Player) {
                                    Player nearest = (Player) entity;

                                    if (Biomia.TeamManager().livesPlayer(nearest)
                                            && Biomia.TeamManager().getTeam(p) != null
                                            && !Biomia.TeamManager().getTeam(p).playerInThisTeam(nearest)) {
                                        p.setCompassTarget(nearest.getLocation());
                                        p.sendMessage(Messages.compassMessages.replace("%p", nearest.getName())
                                                .replace("%d", (int) p.getLocation().distance(nearest.getLocation()) + ""));
                                        return;
                                    }
                                }
                            }
                        }

                        break;
                    case ItemNames.oneHitSnowball:
                        if (e.getItem().getType() == Material.SNOW_BALL) {
                            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                e.setCancelled(true);
                                Projectile ball = p.launchProjectile(Snowball.class);
                                ball.setCustomName(ItemNames.oneHitSnowball);
                                ball.setShooter(p);
                                p.getInventory().remove(e.getItem());
                            }
                        }
                        break;
                    case ItemNames.gummibogen:
                        if (e.getItem().getType() == Material.BOW) {
                            e.setCancelled(true);
                            Projectile arrow = p.launchProjectile(Arrow.class);
                            arrow.setCustomName(ItemNames.gummipfeil);
                            arrow.setShooter(p);
                            p.getInventory().remove(e.getItem());
                        }
                        break;
                    case ItemNames.kitItemName:
                        p.openInventory(Kits.getKitMenu(p));
                        break;
                    case ItemNames.teamWaehlerItem:
                        p.openInventory(Variables.teamJoiner);
                        break;
                    case ItemNames.startItem:
                        if (Variables.countDown.getCountdown() > 5)
                            Variables.countDown.setCountdown(5);
                        break;
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.CHEST) {
                Chest chest = (Chest) e.getClickedBlock().getState();
                boolean firstOpen = false;
                SkyWarsOpenChestEvent.ChestType chestType = SkyWarsOpenChestEvent.ChestType.NORMAL_Chest;
                if (!Variables.opendChests.contains(chest.getLocation()) && SkyWarsMain.gameState == GameState.INGAME) {
                    if (Variables.normalChestsFill.containsKey(chest)) {
                        chest.getInventory().setContents(Variables.normalChestsFill.get(chest));
                        Variables.opendChests.add(chest.getLocation());
                        firstOpen = true;
                        chestType = SkyWarsOpenChestEvent.ChestType.NORMAL_Chest;
                    } else if (Variables.goodChestsFill.containsKey(chest)) {
                        chest.getInventory().setContents(Variables.goodChestsFill.get(chest));
                        Variables.opendChests.add(chest.getLocation());
                        firstOpen = true;
                        chestType = SkyWarsOpenChestEvent.ChestType.GOOD_Chest;
                    }
                }
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Bukkit.getPluginManager().callEvent(new SkyWarsOpenChestEvent(Biomia.getBiomiaPlayer(p), firstOpen, chestType));
                    e.setCancelled(true);
                    p.openInventory(chest.getInventory());
                }
            }
        }

        if (!(Biomia.getBiomiaPlayer(p).canBuild()) && !Variables.livingPlayer.contains(p)) {
            e.setCancelled(true);
        }
        if (!(Biomia.getBiomiaPlayer(p).canBuild()) && SkyWarsMain.gameState != GameState.INGAME) {
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
                    Config.addNormalChestLocation(loc);
                    p.sendMessage(ItemNames.normaleTruheHinzu);
                } else {
                    p.sendMessage(ItemNames.truheBereitsHinzu);
                }
                // Add Better Chest
            } else if ((e.getAction() == Action.LEFT_CLICK_BLOCK) && (e.getClickedBlock().getType() == Material.CHEST)
                    && (p.isSneaking())) {
                Location loc = e.getClickedBlock().getLocation();
                Chest c = Chests.getChestByLoc(loc);

                e.setCancelled(true);

                if (!Variables.goodChests.contains(c)) {
                    Config.addGoodChestLocation(loc);
                    p.sendMessage(ItemNames.bessereTruheHinzu);
                } else {
                    p.sendMessage(ItemNames.truheBereitsHinzu);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();

        if (SkyWarsMain.gameState.equals(GameState.INGAME)) {
            // Check if Player is instatnce of the act round
            if (Variables.livingPlayer.contains(p)) {
                Variables.livingPlayer.remove(p);

                Bukkit.getPluginManager().callEvent(new SkyWarsLeaveEvent(Biomia.getBiomiaPlayer(p)));

                // Check if only one or less Team(s) left
                ArrayList<Team> livingTeams = new ArrayList<>();
                if (Variables.livingPlayer.size() <= 1) {
                    InGame.end();
                    return;
                }
                for (Player player : Variables.livingPlayer) {
                    Team t = Biomia.TeamManager().getTeam(player);
                    if (!livingTeams.contains(t)) {
                        livingTeams.add(t);
                    }
                }
                if (livingTeams.size() <= 1) {
                    InGame.end();
                }
            }
        } else if (SkyWarsMain.gameState.equals(GameState.LOBBY)) {
            // Remove Player from Team
            if (Biomia.TeamManager().isPlayerInAnyTeam(p)) {
                Scoreboards.lobbySB.getTeam("0" + Biomia.TeamManager().getTeam(p).getTeamname())
                        .removeEntry(p.getName());
                Biomia.TeamManager().getTeam(p).removePlayer(p);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (e.getDamager() instanceof Player) {
                Player killer = (Player) e.getDamager();

                if (!SkyWarsMain.gameState.equals(GameState.INGAME))
                    e.setCancelled(true);

                // Check if the Entity in the same team like the damager
                if (Biomia.TeamManager().isPlayerInAnyTeam(killer) && Biomia.TeamManager().isPlayerInAnyTeam(p)) {
                    if (Biomia.TeamManager().getTeam(killer).playerInThisTeam(p)) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }

                if (p.getHealth() <= e.getFinalDamage()) {

                    // Check if Player is instatnce of the act round
                    if (Variables.livingPlayer.contains(p)) {

                        BiomiaPlayer bpKiller = Biomia.getBiomiaPlayer(killer);
                        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

                        Bukkit.getPluginManager().callEvent(new SkyWarsDeathEvent(bp, bpKiller));
                        Bukkit.getPluginManager().callEvent(new SkyWarsKillEvent(bpKiller, bp));

                        e.setCancelled(true);
                        Dead.setDead(p, killer);
                        Bukkit.broadcastMessage(Messages.killedBy
                                .replace("%t", Biomia.TeamManager().getTeam(p).getColorcode() + p.getName())
                                .replace("%k", Biomia.TeamManager().getTeam(killer).getColorcode() + killer.getName()));

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

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onSignChange(SignChangeEvent e) {

        if (e.getPlayer().hasPermission("biomia.leaderboard") && SkyWarsMain.gameState != GameState.INGAME) {
            if (e.getLine(0).equalsIgnoreCase("leaderboard")) {

                String second = e.getLine(1);
                int i = 0;
                try {
                    second = second.replaceAll(" ", "");
                    i = Integer.valueOf(second);
                } catch (Exception ex) {
                    e.getPlayer().sendMessage(Messages.fillSecondLine);
                }

//                Stats stat = Leaderboard.getStat(i);
//
//                if (stat != null) {
//
//                    double kd = stat.kills / stat.deaths;
//                    kd = ((double) Math.round(kd * 100) / 100);
//
//                    e.setLine(0, Messages.rank.replaceAll("%rank", i + "").replaceAll("%p", stat.name));
//                    e.setLine(1, Messages.wunGames + stat.wins);
//                    e.setLine(2, Messages.kd + kd);
//                    e.setLine(3, Messages.playedGames + stat.played_games);
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
//                    Config.addSignsLocation(e.getBlock().getLocation(), i);
//
//                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInvClick(InventoryClickEvent ie) {

        if (!SkyWarsMain.gameState.equals(GameState.INGAME)) {
            if (ie.getCurrentItem() != null) {
                Player p = (Player) ie.getWhoClicked();
                if (!Variables.livingPlayer.contains(p)) {
                    if (!Biomia.getBiomiaPlayer(p).canBuild()) {
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

        if (SkyWarsMain.gameState != GameState.INGAME) {
            e.setCancelled(true);
        }

        if (e.getRightClicked() instanceof ArmorStand) {

            ItemStack is = p.getInventory().getItemInMainHand();

            if (is != null) {
                if (is.hasItemMeta()) {
                    if (is.getItemMeta().getDisplayName().equals(ItemNames.teamJoinerSetter)) {

                        if (is.getType().equals(Material.WOOL)) {

                            @SuppressWarnings("deprecation")
                            Team team = Biomia.TeamManager().DataToTeam(is.getData().getData());

                            if (team != null) {
                                UUID uuid = e.getRightClicked().getUniqueId();
                                Entity armorstand = e.getRightClicked();
                                Config.addTeamJoiner(uuid, team);
                                armorstand.setCustomName(
                                        team.getColorcode() + Biomia.TeamManager().translate(team.getTeamname()));
                                armorstand.setCustomNameVisible(true);
                                p.sendMessage(Messages.teamJoinerSet.replace("%t", team.getTeamname()));
                            }
                        }
                    }
                }
            }
            for (Team allteams : Biomia.TeamManager().getTeams()) {

                UUID uuid = Variables.joiner.get(allteams);

                if (uuid != null) {
                    if (e.getRightClicked().getUniqueId().equals(uuid)) {
                        JoinTeam.join(p, allteams);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String msg = e.getMessage();
        String format;
        if (p.hasPermission("biomia.coloredchat"))
            msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());

        if (SkyWarsMain.gameState.equals(GameState.INGAME) || SkyWarsMain.gameState.equals(GameState.WAITINGFORSTART)) {

            Team t = Biomia.TeamManager().getTeam(p);

            if (t != null && !Biomia.TeamManager().getTeam(p).isPlayerDead(p)) {
                if (e.getMessage().substring(0, 1).contains("@")) {

                    msg = msg.replace("@all ", "");
                    msg = msg.replace("@all", "");
                    msg = msg.replace("@a ", "");
                    msg = msg.replace("@a", "");
                    msg = msg.replace("@ ", "");
                    msg = msg.replace("@", "");

                    e.setFormat(Messages.chatMessageAll.replaceAll("%p", t.getColorcode() + p.getDisplayName())
                            .replaceAll("%msg", msg));
                } else {
                    e.setCancelled(true);
                    format = Messages.chatMessageTeam.replaceAll("%p", t.getColorcode() + p.getDisplayName())
                            .replaceAll("%msg", msg);
                    for (Player teamPlayer : t.getPlayers()) {
                        teamPlayer.sendMessage(format);
                    }
                }
            } else {

                format = Messages.chatMessageDead.replaceAll("%p", p.getDisplayName()).replaceAll("%msg", msg);
                for (Player spec : Variables.spectator) {
                    spec.sendMessage(format);
                }

            }
        } else if (Biomia.TeamManager().isPlayerInAnyTeam(p)) {
            Team t = Biomia.TeamManager().getTeam(p);
            format = Messages.chatMessageLobby.replaceAll("%p", t.getColorcode() + p.getDisplayName())
                    .replaceAll("%msg", msg);
            e.setFormat(format);
        } else {
            format = Messages.chatMessageLobby.replaceAll("%p", p.getDisplayName()).replaceAll("%msg", msg);
            e.setFormat(format);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if (SkyWarsMain.gameState == GameState.INGAME) {
            if (e.getTo().getBlockY() <= 0) {
                e.getPlayer().setHealth(0);
            }
        } else if (e.getTo().getBlockY() <= 20) {
            e.getPlayer().teleport(Variables.warteLobbySpawn);
        }

        if (SkyWarsMain.gameState.equals(GameState.WAITINGFORSTART)) {
            e.setCancelled(true);
        }
    }
}