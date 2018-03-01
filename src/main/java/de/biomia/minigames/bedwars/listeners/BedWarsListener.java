package de.biomia.minigames.bedwars.listeners;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.Teams.Team;
import de.biomia.api.achievements.statEvents.bedwars.BedWarsDeathEvent;
import de.biomia.api.achievements.statEvents.bedwars.BedWarsKillEvent;
import de.biomia.api.achievements.statEvents.bedwars.BedWarsLeaveEvent;
import de.biomia.api.achievements.statEvents.bedwars.BedWarsUseShopEvent;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.pex.Rank;
import de.biomia.api.tools.BackToLobby;
import de.biomia.general.configs.BedWarsConfig;
import de.biomia.minigames.GameState;
import de.biomia.minigames.bedwars.gamestates.InGame;
import de.biomia.minigames.bedwars.ingame.Dead;
import de.biomia.minigames.bedwars.lobby.JoinTeam;
import de.biomia.minigames.bedwars.BedWars;
import de.biomia.general.messages.BedWarsItemNames;
import de.biomia.general.messages.BedWarsMessages;
import de.biomia.minigames.bedwars.shop.Shop;
import de.biomia.minigames.bedwars.shop.ShopGroup;
import de.biomia.minigames.bedwars.shop.ShopItem;
import de.biomia.minigames.bedwars.var.*;
import de.biomia.minigames.general.ColorType;
import de.biomia.minigames.general.ItemType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.UUID;

public class BedWarsListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        BackToLobby.getLobbyItem(p, 8);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDamageEntitys(false);
        bp.setGetDamage(false);

        if (BedWars.gameState.equals(GameState.INGAME)) {

            // Hide
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (Biomia.getTeamManager().isPlayerAlive(all)) {
                    all.hidePlayer(p);
                } else {
                    all.showPlayer(all);
                }
            }

            // Disable Damage / Build
            bp.setGetDamage(false);
            bp.setDamageEntitys(false);
            bp.setBuild(false);
            p.setGameMode(GameMode.ADVENTURE);

            // Fly settings
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setFlySpeed(0.5F);

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

        } else if (BedWars.gameState.equals(GameState.LOBBY)) {

            p.teleport(Variables.warteLobbySpawn);

            if (p.hasPermission("biomia.sw.start")) {

                p.getInventory().setItem(0, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, BedWarsItemNames.startItem));

            }

            p.getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, BedWarsItemNames.teamWaehlerItem));

            bp.getPlayer().setLevel(Variables.countDown.getCountdown());

            if (bp.isPremium()) {
                Bukkit.broadcastMessage("\u00A76" + p.getName() + BedWarsMessages.joinedTheGame);
            } else {
                Bukkit.broadcastMessage("\u00A77" + p.getName() + BedWarsMessages.joinedTheGame);
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

            if (BedWars.gameState.equals(GameState.LOBBY)) {

                String rank = Rank.getRank(e.getPlayer());
                int i = Integer.valueOf(Rank.getRankID(rank));

                if (i < 15) {

                    ArrayList<Player> player = new ArrayList<>(Bukkit.getOnlinePlayers());
                    player.forEach(eachPlayer -> {
                        if (Integer.valueOf(Rank.getRankID(Rank.getRank(eachPlayer))) > i) {

                            e.allow();
                            eachPlayer.sendMessage(BedWarsMessages.kickedForPremium);
                            eachPlayer.kickPlayer("");
                        }
                    });
                }
            } else if (BedWars.gameState.equals(GameState.INGAME)) {
                e.allow();
            }
        }
    }

    @EventHandler
    public void onHungerSwitch(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (!BedWars.gameState.equals(GameState.INGAME)) {
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
        if (!BedWars.gameState.equals(GameState.INGAME)) {
            e.setCancelled(true);
        } else if (!Variables.livingPlayer.contains(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (!BedWars.gameState.equals(GameState.INGAME)) {
                e.setCancelled(true);
            } else if (!Variables.livingPlayer.contains(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSwap(PlayerSwapHandItemsEvent e) {
        if (!BedWars.gameState.equals(GameState.INGAME)) {
            e.setCancelled(true);
        } else if (!Variables.livingPlayer.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        Player killer = p.getKiller();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        e.setKeepInventory(true);
        p.getInventory().clear();

        if (!Variables.livingPlayer.contains(p)) {
            e.setDeathMessage(BedWarsMessages.playerDiedFinally.replace("%p",
                    Biomia.getTeamManager().getTeam(p).getColorcode() + p.getName()));
        } else {
            if (killer == null) {
                e.setDeathMessage(BedWarsMessages.playerDied.replace("%p",
                        Biomia.getTeamManager().getTeam(p).getColorcode() + p.getName()));
            } else {
                BiomiaPlayer bpKiller = Biomia.getBiomiaPlayer(killer);
                e.setDeathMessage(BedWarsMessages.playerKilledByPlayer
                        .replace("%p1", Biomia.getTeamManager().getTeam(p).getColorcode() + p.getName())
                        .replace("%p2", Biomia.getTeamManager().getTeam(killer).getColorcode() + killer.getName()));
                Bukkit.getPluginManager().callEvent(new BedWarsKillEvent(bp, bpKiller, false));
            }
        }
        Bukkit.getPluginManager().callEvent(new BedWarsDeathEvent(Biomia.getBiomiaPlayer(p), killer != null ? Biomia.getBiomiaPlayer(killer) : null, true));
        Dead.respawn(p);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {

        Player p = e.getPlayer();

        if (BedWars.gameState == GameState.INGAME) {

            if (Biomia.getTeamManager().isPlayerInAnyTeam(p)) {

                Team t = Biomia.getTeamManager().getTeam(p);

                if (Variables.livingPlayer.contains(p)) {
                    e.setRespawnLocation(Variables.teamSpawns.get(t));
                } else {
                    e.setRespawnLocation(new Location(Bukkit.getWorld(Variables.name), 0, 100, 0));
                }
                return;
            }
        }
        e.setRespawnLocation(Variables.warteLobbySpawn);
    }

    @EventHandler
    public void onProjectileThrow(ProjectileLaunchEvent event) {

        if (!(BedWars.gameState == GameState.INGAME)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        for (ArrayList uuid : Variables.handlerMap.values()) {
            uuid.remove(e.getPlayer());
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();

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
                } else if (e.getClickedInventory().getName().equals(Variables.teamJoiner.getName())) {
                    Variables.teamJoiner = JoinTeam.getTeamSwitcher();
                    JoinTeam.join(p, Biomia.getTeamManager().getTeamFromData(e.getCurrentItem().getData().getData()));
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
                                    Team team = Biomia.getTeamManager().getTeam(p);

                                    ItemStack returnItem = iStack.clone();

                                    if (shopItem.isColorble()) {
                                        if (shopItem.getType() == ColorType.LEATHER) {
                                            LeatherArmorMeta meta = (LeatherArmorMeta) returnItem.getItemMeta();
                                            switch (team.getTeamname()) {
                                                case "BLACK":
                                                    meta.setColor(Color.BLACK);
                                                    break;
                                                case "RED":
                                                    meta.setColor(Color.RED);
                                                    break;
                                                case "BLUE":
                                                    meta.setColor(Color.BLUE);
                                                    break;
                                                case "GOLD":
                                                    meta.setColor(Color.ORANGE);
                                                    break;
                                                case "GREEN":
                                                    meta.setColor(Color.GREEN);
                                                    break;
                                                case "WHITE":
                                                    meta.setColor(Color.WHITE);
                                                    break;
                                                case "PURPLE":
                                                    meta.setColor(Color.PURPLE);
                                                    break;
                                                case "YELLOW":
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
                                                if (name != null) {
                                                    p.sendMessage(BedWarsMessages.notEnoughItemsToPay.replace("%n", name));
                                                }
                                                return;
                                            } else {
                                                return;
                                            }
                                        }

                                    } else if (shopItem.take(p)) {
                                        p.getInventory().addItem(returnItem);
                                    } else {
                                        String name = ItemType.getName(shopItem.getItemType());
                                        if (name != null) {
                                            p.sendMessage(BedWarsMessages.notEnoughItemsToPay.replace("%n", name));
                                        }
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
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();

        if (BedWars.gameState.equals(GameState.INGAME)) {
            // Check if Player is instatnce of the act round
            if (Variables.livingPlayer.contains(p)) {
                Variables.livingPlayer.remove(p);
                e.setQuitMessage(e.getPlayer().getName() + " hat das Spiel verlassen");

                Bukkit.getPluginManager().callEvent(new BedWarsLeaveEvent(Biomia.getBiomiaPlayer(p)));

                // Check if only one or less Team(s) left
                ArrayList<Team> livingTeams = new ArrayList<>();
                if (Variables.livingPlayer.size() <= 1) {
                    InGame.end();
                    return;
                }
                for (Player player : Variables.livingPlayer) {
                    Team t = Biomia.getTeamManager().getTeam(player);
                    if (!livingTeams.contains(t)) {
                        livingTeams.add(t);
                    }
                }
                if (livingTeams.size() <= 1) {
                    InGame.end();
                }

            }
        } else if (BedWars.gameState.equals(GameState.LOBBY)) {
            // Remove Player from Team
            if (Biomia.getTeamManager().isPlayerInAnyTeam(p)) {
                Scoreboards.lobbySB.getTeam("0" + Biomia.getTeamManager().getTeam(p).getTeamname())
                        .removeEntry(p.getName());
                Biomia.getTeamManager().getTeam(p).removePlayer(p);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getDamager() instanceof Player) {
                Player killer = (Player) e.getDamager();
                if (!BedWars.gameState.equals(GameState.INGAME)) {
                    e.setCancelled(true);
                    return;
                }

                // Check if the Entity in the same team like the damager
                if (Biomia.getTeamManager().isPlayerInAnyTeam(killer) && Biomia.getTeamManager().isPlayerInAnyTeam(p)) {
                    if (Biomia.getTeamManager().getTeam(killer).playerInThisTeam(p)) {
                        e.setCancelled(true);
                    } else if (e.getFinalDamage() > p.getHealth()
                            && !Variables.teamsWithBeds.contains(Biomia.getTeamManager().getTeam(p))) {
                        e.setCancelled(true);

                        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
                        BiomiaPlayer bpKiller = Biomia.getBiomiaPlayer(killer);

                        Bukkit.getPluginManager().callEvent(new BedWarsDeathEvent(Biomia.getBiomiaPlayer(p), Biomia.getBiomiaPlayer(killer), true));
                        Bukkit.getPluginManager().callEvent(new BedWarsKillEvent(bp, bpKiller, false));
                        Dead.setDead(p);
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

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onSignChange(SignChangeEvent e) {

        if (e.getPlayer().hasPermission("biomia.leaderboard") && BedWars.gameState != GameState.INGAME) {
            if (e.getLine(0).equalsIgnoreCase("leaderboard")) {

                String second = e.getLine(1);
                //  int i = 0;
                try {
                    second = second.replaceAll(" ", "");
                    //        i = Integer.valueOf(second);
                } catch (Exception ex) {
                    e.getPlayer().sendMessage(BedWarsMessages.fillSecondLine);
                }

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
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInvClick(InventoryClickEvent ie) {

        if (!BedWars.gameState.equals(GameState.INGAME)) {
            if (ie.getCurrentItem() != null) {
                Material t = ie.getCurrentItem().getType();
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
                Variables.handlerMap.get(e.getRightClicked().getUniqueId()).add(p);
            }
        }
    }

    @EventHandler
    public void Interact(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();

        if (e.getRightClicked() instanceof ArmorStand) {

            ItemStack is = p.getInventory().getItemInMainHand();

            if (is != null) {
                if (is.hasItemMeta()) {
                    if (is.getItemMeta().getDisplayName().equals(BedWarsItemNames.teamJoinerSetter)) {
                        if (is.getType().equals(Material.WOOL)) {
                            @SuppressWarnings("deprecation")
                            Team team = Biomia.getTeamManager().getTeamFromData(is.getData().getData());

                            if (team != null) {
                                UUID uuid = e.getRightClicked().getUniqueId();
                                Entity armorstand = e.getRightClicked();
                                BedWarsConfig.addTeamJoiner(uuid, team);
                                armorstand.setCustomName(
                                        team.getColorcode() + Biomia.getTeamManager().translate(team.getTeamname()));
                                armorstand.setCustomNameVisible(true);
                                p.sendMessage(BedWarsMessages.teamJoinerSet.replace("%t", team.getTeamname()));
                            }
                        }
                    }
                }
            }
            for (Team allteams : Biomia.getTeamManager().getTeams()) {

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
    public void craftItem(PrepareItemCraftEvent e) {
        e.getInventory().setResult(ItemCreator.itemCreate(Material.AIR));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String msg = e.getMessage();
        String format;
        if (p.hasPermission("biomia.coloredchat"))
            msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());

        if (BedWars.gameState.equals(GameState.INGAME)) {

            Team t = Biomia.getTeamManager().getTeam(p);

            if (t != null) {
                if (e.getMessage().startsWith("@")) {

                    msg = msg.replaceAll("@all ", "");
                    msg = msg.replaceAll("@all", "");
                    msg = msg.replaceAll("@a ", "");
                    msg = msg.replaceAll("@a", "");
                    msg = msg.replaceAll("@ ", "");
                    msg = msg.replaceAll("@", "");

                    e.setFormat(BedWarsMessages.chatMessageAll.replaceAll("%p", t.getColorcode() + p.getDisplayName())
                            .replaceAll("%msg", msg));
                } else {
                    e.setCancelled(true);
                    format = BedWarsMessages.chatMessageTeam.replaceAll("%p", t.getColorcode() + p.getDisplayName())
                            .replaceAll("%msg", msg);
                    for (Player teamPlayer : t.getPlayers()) {
                        teamPlayer.sendMessage(format);
                    }
                }
            } else {
                format = BedWarsMessages.chatMessageDead.replaceAll("%p", p.getDisplayName()).replaceAll("%msg", msg);
                for (Player spec : Variables.spectator) {
                    spec.sendMessage(format);
                }

            }
        } else if (Biomia.getTeamManager().isPlayerInAnyTeam(p)) {
            Team t = Biomia.getTeamManager().getTeam(p);
            format = BedWarsMessages.chatMessageLobby.replaceAll("%p", t.getColorcode() + p.getDisplayName())
                    .replaceAll("%msg", msg);
            e.setFormat(format);
        } else {
            format = BedWarsMessages.chatMessageLobby.replaceAll("%p", p.getDisplayName()).replaceAll("%msg", msg);
            e.setFormat(format);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if (BedWars.gameState == GameState.INGAME) {
            if (e.getTo().getBlockY() <= 0) {
                e.getPlayer().setHealth(0);
                return;
            }

            Location loc = Teleport.getStartLocation(e.getPlayer());
            if (loc != null && loc.distance(e.getTo()) > .5) {
                Teleport.removeFromStartLocs(e.getPlayer());
            }
        } else if (e.getTo().getBlockY() <= 20) {
            e.getPlayer().teleport(Variables.warteLobbySpawn);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Variables.destroyableBlocks.add(e.getBlock());
        if (e.getBlock().getType() == Material.ENDER_CHEST) {
            Team team = Biomia.getTeamManager().getTeam(e.getPlayer());
            if (team != null) {
                if (!Variables.teamChestsLocs.containsKey(team)) {
                    Variables.teamChestsLocs.put(team, new ArrayList<>());
                }
                Variables.teamChestsLocs.get(team).add(e.getBlock());
            }
        }
    }
}