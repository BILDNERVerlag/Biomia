package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.events.bedwars.BedWarsDeathEvent;
import de.biomia.spigot.events.bedwars.BedWarsKillEvent;
import de.biomia.spigot.events.bedwars.BedWarsLeaveEvent;
import de.biomia.spigot.events.bedwars.BedWarsUseShopEvent;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.general.ColorType;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.minigames.general.Scoreboards;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.minigames.general.shop.Shop;
import de.biomia.spigot.minigames.general.shop.ShopGroup;
import de.biomia.spigot.minigames.general.shop.ShopItem;
import de.biomia.spigot.tools.BackToLobby;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.RankManager;
import de.biomia.universal.UniversalBiomia;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;

public class BedWarsListener extends GameHandler {

    public final ArrayList<Block> destroyableBlocks = new ArrayList<>();

    BedWarsListener(GameMode mode) {
        super(mode);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        BackToLobby.getLobbyItem(p, 8);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDamageEntitys(false);
        bp.setGetDamage(false);

        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            // Hide
            for (Player all : Bukkit.getOnlinePlayers()) {

                GameTeam team = bp.getTeam();

                if (team != null && team.lives(bp)) {
                    all.hidePlayer(p);
                } else {
                    all.showPlayer(all);
                }
            }

            // Disable Damage / Build
            bp.setGetDamage(false);
            bp.setDamageEntitys(false);
            bp.setBuild(false);
            p.setGameMode(org.bukkit.GameMode.ADVENTURE);

            // Fly settings
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setFlySpeed(0.5F);

            Scoreboards.setSpectatorSB(p);
            Scoreboards.spectatorSB.getTeam("spectator").addEntry(p.getName());

            p.teleport(new Location(Bukkit.getWorld(MinigamesConfig.getMapName()), 0, 100, 0));

        } else if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {

            p.teleport(GameMode.getSpawn());

            if (p.hasPermission("biomia.sw.start")) {
                p.getInventory().setItem(0, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, BedWarsItemNames.startItem));
            }

            p.getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, BedWarsItemNames.teamWaehlerItem));

            bp.getPlayer().setLevel(mode.getStateManager().getLobbyState().getCountDown());


            Bukkit.broadcastMessage(bp.isPremium() ? "\u00A76" : "\u00A77" + p.getName() + MinigamesMessages.joinedTheGame);

            mode.partyJoin(bp);
            Scoreboards.setLobbyScoreboard(p);
            Scoreboards.lobbySB.getTeam("xnoteam").addEntry(p.getName());

        }

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {
            if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {
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
            } else
                e.allow();
        }
    }

    @EventHandler
    public void onHungerSwitch(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
                p.setFoodLevel(30);
                e.setCancelled(true);
            } else if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                p.setFoodLevel(30);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        } else if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
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
        GameTeam team = bp.getTeam();

        e.setKeepInventory(true);
        p.getInventory().clear();

        if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
            e.setDeathMessage(MinigamesMessages.playerDiedFinally.replace("%p",
                    team.getColorcode() + p.getName()));
            team.setDead(bp);
        } else {
            if (killer == null) {
                e.setDeathMessage(MinigamesMessages.playerDied.replace("%p", team.getColorcode() + p.getName()));
            } else {
                BiomiaPlayer bpKiller = Biomia.getBiomiaPlayer(killer);
                e.setDeathMessage(String.format(MinigamesMessages.playerKilledByPlayer, team.getColorcode() + p.getName(), bpKiller.getTeam().getColorcode() + killer.getName()));
                Bukkit.getPluginManager().callEvent(new BedWarsKillEvent(bp, bpKiller, false));
            }
        }
        Bukkit.getPluginManager().callEvent(new BedWarsDeathEvent(Biomia.getBiomiaPlayer(p), killer != null ? Biomia.getBiomiaPlayer(killer) : null, true));
        Dead.respawn(p);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {

            GameTeam t = Biomia.getBiomiaPlayer(p).getTeam();
            if (t != null) {
                if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                    e.setRespawnLocation(t.getHome());
                } else
                    e.setRespawnLocation(new Location(Bukkit.getWorld(MinigamesConfig.getMapName()), 0, 100, 0));
                return;
            }
        }
        e.setRespawnLocation(GameMode.getSpawn());
    }

    @EventHandler
    public void onProjectileThrow(ProjectileLaunchEvent event) {

        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            event.setCancelled(true);
        }
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
                } else if (e.getClickedInventory().getName().equals(mode.getTeamSwitcher().getName())) {
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
                                            if (shopItem.take(p)) {
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

                                    } else if (shopItem.take(p)) {
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
    public void onDisconnect(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            // Check if Player is instatnce of the act round
            if (mode.getInstance().containsPlayer(bp)) {
                e.setQuitMessage(e.getPlayer().getName() + " hat das Spiel verlassen");
                Bukkit.getPluginManager().callEvent(new BedWarsLeaveEvent(bp));
                if (mode.canStop()) {
                    mode.stop();
                }
            }
        } else if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {
            super.onDisconnect(e);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getDamager() instanceof Player) {
                Player killer = (Player) e.getDamager();
                if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
                    e.setCancelled(true);
                    return;
                }

                BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
                BiomiaPlayer bpKiller = Biomia.getBiomiaPlayer(killer);

                GameTeam team = bp.getTeam();
                GameTeam teamkiller = bpKiller.getTeam();

                // Check if the Entity in the same team like the damager
                if (team != null && teamkiller != null) {
                    if (team.equals(teamkiller)) {
                        e.setCancelled(true);
                    } else if (e.getFinalDamage() > p.getHealth() && !((BedWarsTeam) Biomia.getBiomiaPlayer(killer).getTeam()).hasBed()) {
                        e.setCancelled(true);
                        Bukkit.getPluginManager().callEvent(new BedWarsDeathEvent(bp, bpKiller, true));
                        Bukkit.getPluginManager().callEvent(new BedWarsKillEvent(bp, bpKiller, false));
                        team.setDead(bp);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        } else if (e.getEntity() instanceof Villager) {
            if (e.getEntity().getCustomName().equals("Shop")) {
                e.setCancelled(true);
            }
        }
    }

//    @SuppressWarnings("deprecation")
//    @EventHandler
//    public void onSignChange(SignChangeEvent e) {
//        if (e.getPlayer().hasPermission("biomia.leaderboard") && BedWars.gameState != GameState.INGAME) {
//            if (e.getLine(0).equalsIgnoreCase("leaderboard")) {
//                String second = e.getLine(1);
//                  int i = 0;
//                try {
//                    second = second.replaceAll(" ", "");
//                    //        i = Integer.valueOf(second);
//                } catch (Exception ex) {
//                    e.getPlayer().sendMessage(BedWarsMessages.fillSecondLine);
//                    return;
//                }
//
//TODO: renew
//
//				Stats stat = Leaderboard.getStat(i);
//
//				if (stat != null) {
//
//					double kd = stat.kills / stat.deaths;
//
//					NumberFormat n = NumberFormat.getInstance();
//					n.setMaximumFractionDigits(2);
//
//					e.setLine(0, BedWarsMessages.rank.replaceAll("%rank", i + "").replaceAll("%p", e.getPlayer().getName()));
//					e.setLine(1, BedWarsMessages.wunGames + stat.wins);
//					e.setLine(2, BedWarsMessages.kd + n.format(kd));
//					e.setLine(3, BedWarsMessages.playedGames + stat.played_games);
//
//					org.bukkit.material.Sign signData = (org.bukkit.material.Sign) e.getBlock().getState().getData();
//
//					Block b = e.getBlock().getLocation().add(0, 1, 0).getBlock();
//					b.setTypeIdAndData(Material.SKULL.getId(), Leaderboard.getFacingDirectionByte(signData.getFacing()),
//							true);
//
//					Skull s = (Skull) b.getState();
//					s.setSkullType(SkullType.PLAYER);
//					s.setOwner(stat.name);
//					s.update();
//				} else {
//					org.bukkit.material.Sign signData = (org.bukkit.material.Sign) e.getBlock().getState().getData();
//
//					Block b = e.getBlock().getLocation().add(0, 1, 0).getBlock();
//					b.setTypeIdAndData(Material.SKULL.getId(), Leaderboard.getFacingDirectionByte(signData.getFacing()),
//							true);
//
//					Skull s = (Skull) b.getState();
//					s.setSkullType(SkullType.PLAYER);
//					s.update();
//				}
//				BedWarsVersusConfig.addSignsLocation(e.getBlock().getLocation(), i);
//            }
//        }
//    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInvClick(InventoryClickEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getWhoClicked());
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            if (e.getCurrentItem() != null) {
                if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                    if (!bp.canBuild()) {
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler
    public void Interact(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();

        if (e.getRightClicked() instanceof Villager || e.getRightClicked() instanceof ArmorStand) {
            if (e.getRightClicked().getCustomName().equals("Shop")) {
                e.setCancelled(true);
                Bukkit.getPluginManager().callEvent(new BedWarsUseShopEvent(Biomia.getBiomiaPlayer(p), e.getRightClicked() instanceof Villager));
                p.openInventory(Shop.getInventory());
            } else if (e.getRightClicked().getCustomName().contains("Sekunden")) {
                e.setCancelled(true);
                p.openInventory(Shop.getInventory());
                ((BedWars) mode).handlerMap.get(e.getRightClicked().getUniqueId()).add(p);
            }
        }
    }

    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e) {
        e.getInventory().setResult(ItemCreator.itemCreate(Material.AIR));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        destroyableBlocks.add(e.getBlock());
        if (e.getBlock().getType() == Material.ENDER_CHEST) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
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
    public void onBlockBreak(BlockBreakEvent e) {
        if (destroyableBlocks.contains(e.getBlock())) {
            destroyableBlocks.remove(e.getBlock());
        } else {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BedWarsMessages.cantDestroyThisBlock);
        }
    }
}