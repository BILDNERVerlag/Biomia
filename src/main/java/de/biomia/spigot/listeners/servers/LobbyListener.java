package de.biomia.spigot.listeners.servers;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.general.cosmetics.MysteryChest;
import de.biomia.spigot.listeners.LobbyInventoryManager;
import de.biomia.universal.Messages;
import de.biomia.spigot.server.lobby.Lobby;
import de.biomia.spigot.server.lobby.LobbyScoreboard;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.PlayerToServerConnector;
import de.biomia.spigot.tools.RankManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class LobbyListener extends BiomiaListener {

    private final Location stonebutton = new Location(Bukkit.getWorld("LobbyBiomia"), 465, 97, 359);

    @EventHandler
    public void onDrop(PlayerDropItemEvent di) {
        if (di.getItemDrop().getItemStack().getItemMeta().getDisplayName() != null) {
            if (!Biomia.getBiomiaPlayer(di.getPlayer()).canBuild()) {
                di.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityInteract(PlayerInteractAtEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onHungerSwitch(FoodLevelChangeEvent fe) {
        if (fe.getEntity() instanceof Player) {
            Player pl = (Player) fe.getEntity();
            pl.setFoodLevel(200);
            fe.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent pr) {
        Player pl = pr.getPlayer();

        LobbyInventoryManager.setInventory(pl);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.getPlayer().getInventory().clear();
    }

    @EventHandler
    public void onJoin_(PlayerJoinEvent pj) {
        Player p = pj.getPlayer();
        p.setAllowFlight(true);
        sendRegMsg(p);
        LobbyInventoryManager.setInventory(p);
        LobbyScoreboard.sendScoreboard(p);

        for (Player pl : ((Lobby) Biomia.getSeverInstance()).getSilentLobby()) {
            p.hidePlayer(pl);
            pl.hidePlayer(p);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setDamage(0);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onArmorStandDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ArmorStand || e.getEntity() instanceof ItemFrame) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void disableItemFrameRotate(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ItemFrame) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void stopCreepers(EntityExplodeEvent e) {
        e.blockList().clear();
    }

    @EventHandler
    public void stopOtherExplosions(BlockExplodeEvent e) {
        e.blockList().clear();
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e) {

        Player p = e.getPlayer();
        if (p.getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
            if (!((Lobby) Biomia.getSeverInstance()).getInAir().contains(p)) {
                p.setFlying(false);
                p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1, 0);
                Vector jump = p.getLocation().getDirection().multiply(2.6D).setY(1.2);
                p.setVelocity(p.getVelocity().add(jump));
                p.setAllowFlight(false);
                ((Lobby) Biomia.getSeverInstance()).getInAir().add(p);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (((Lobby) Biomia.getSeverInstance()).getInAir().contains(e.getPlayer())) {
            if (e.getPlayer().isOnGround()) {
                e.getPlayer().setAllowFlight(true);
                ((Lobby) Biomia.getSeverInstance()).getInAir().remove(e.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract_(PlayerInteractEvent e) {
        Player pl = e.getPlayer().getPlayer();
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            ItemStack itemstack = pl.getInventory().getItemInMainHand();
            if (itemstack.hasItemMeta()) {
                if ((itemstack.getType().equals(Material.COMPASS)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7cNavigator"))) {
                    pl.openInventory(((Lobby) Biomia.getSeverInstance()).getNavigator());
                } else if ((itemstack.getType().equals(Material.NETHER_STAR)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7dLobby Switcher"))) {
                    pl.openInventory(((Lobby) Biomia.getSeverInstance()).getLobbySwitcher());
                } else if ((itemstack.getType().equals(Material.FIREBALL)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7cSilent Lobby:\u00A78 Off"))) {
                    pl.getInventory().setItem(6,
                            ItemCreator.itemCreate(Material.FIREWORK_CHARGE, "\u00A7aSilent Lobby:\u00A78 On"));

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.hidePlayer(pl);
                        pl.hidePlayer(p);
                    }
                    ((Lobby) Biomia.getSeverInstance()).getSilentLobby().add(pl);

                } else if ((itemstack.getType().equals(Material.FIREWORK_CHARGE)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7aSilent Lobby:\u00A78 On"))) {
                    pl.getInventory().setItem(6, ItemCreator.itemCreate(Material.FIREBALL, "\u00A7cSilent Lobby:\u00A78 Off"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!((Lobby) Biomia.getSeverInstance()).getSilentLobby().contains(p)) {
                            p.showPlayer(pl);
                            pl.showPlayer(p);
                        }
                    }
                    ((Lobby) Biomia.getSeverInstance()).getSilentLobby().remove(pl);
                } else if ((itemstack.getType().equals(Material.CHEST)
                        && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("\u00A7eCosmetics"))) {
                    de.biomia.spigot.general.cosmetics.Cosmetic.openMainInventory(Biomia.getBiomiaPlayer(pl));
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType() == Material.STONE_BUTTON
                        && e.getClickedBlock().getLocation().distance(stonebutton) <= 2) {
                    return;
                }
                if (e.getItem() != null && e.getItem().getType() == Material.BOW) {
                    if (!(e.getClickedBlock().getType() == Material.STONE_BUTTON)
                            && !(e.getClickedBlock().getLocation().distance(stonebutton) <= 2))
                        return;
                    if (e.getClickedBlock().getType() == Material.DAYLIGHT_DETECTOR
                            || e.getClickedBlock().getType() == Material.DAYLIGHT_DETECTOR_INVERTED
                            || e.getClickedBlock().getType() == Material.WOOD_BUTTON
                            || e.getClickedBlock().getType() == Material.LEVER || e.getClickedBlock().getType() == Material.FENCE_GATE)
                        e.setCancelled(true);
                }
                if (!Biomia.getBiomiaPlayer(e.getPlayer()).canBuild()) {
                    e.setCancelled(true);
                }

                if (e.getClickedBlock().getType() == Material.CHEST) { /*only chests in the beach house become mystery chests*/
                    try {
                        if (e.getClickedBlock().getLocation().distance(new Location(Bukkit.getWorld("LobbyBiomia"), 605, 68, 363)) < 10) {

                            e.setCancelled(true);

                            BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

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
                    } catch (IllegalArgumentException ignored) {/*Location.distance throws IllegalArgs when the locations are on different worlds*/}
                }
            }
        }
    }

    private static void sendRegMsg(Player p) {
        if (RankManager.getRank(p).equals("UnregSpieler")) {
            TextComponent register = new TextComponent();
            p.sendMessage(ChatColor.DARK_PURPLE + "Du bist noch nicht registriert!");
            register.setText(ChatColor.BLUE + "Registriere dich jetzt auf: " + ChatColor.GRAY + "www."
                    + ChatColor.DARK_PURPLE + "Bio" + ChatColor.DARK_GREEN + "mia"
                    + ChatColor.GRAY + ".de");
            register.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));
            p.spigot().sendMessage(register);
            p.sendMessage(ChatColor.GRAY + "Oder sp\u00fcter mit " + ChatColor.GOLD + "/register");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent ie) {
        Player pl = (Player) ie.getWhoClicked();
        if (ie.getClick().isLeftClick())
            if (ie.getCurrentItem() != null && ie.getCurrentItem().hasItemMeta()) {
                String itemName = ie.getCurrentItem().getItemMeta().getDisplayName();
                if (ie.getClickedInventory().equals(((Lobby) Biomia.getSeverInstance()).getNavigator())) {
                    switch (itemName) {
                        case "§6Bau Welt":
                            pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 551.5, 80, 285.5, -90, 0));
                            pl.closeInventory();
                            break;
                        case "§eDemo Welt":
                            pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 512, 80, 354, -50, 8));
                            pl.closeInventory();
                            break;
                        case "§cSpawn":
                            pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 534.5, 67, 193.5));
                            pl.closeInventory();
                            break;
                        case "§5Quests":
                            pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 473.5, 123, 359.5, -90, 0));
                            pl.closeInventory();
                            break;
                        case "§bSkyWars":
                            pl.teleport(new Location(Bukkit.getWorld("SkywarsSignlobby"), 370.5, 82, 264.5, 70, 0));
                            pl.closeInventory();
                            break;
                        case "§4BedWars":
                            pl.teleport(new Location(Bukkit.getWorld("BedwarsSignlobby"), 370.5, 82, 264.5, 70, 0));
                            pl.closeInventory();
                            break;
                        case "§5Mysteriöse Box":
                            pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 605.5, 68, 358, 0, 0));
                            pl.closeInventory();
                            break;
                        case "§6Freebuild Welt":
                            pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 560, 96, 290, 80, 0));
                            pl.closeInventory();
                            break;
                    }
                } else if (ie.getClickedInventory().getName().equals("§dLobby Switcher"))
                    for (ServerObject so : TimoCloudAPI.getUniversalInstance().getServerGroup("Lobby")
                            .getServers())
                        if (itemName.contains(so.getName()))
                            if (!so.getName().equals(TimoCloudAPI.getBukkitInstance().getThisServer().getName()))
                                PlayerToServerConnector.connect(pl, so.getName());
                            else
                                pl.sendMessage("§cDu bist schon auf dieser Lobby!");
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
            }
        }
    }
}
