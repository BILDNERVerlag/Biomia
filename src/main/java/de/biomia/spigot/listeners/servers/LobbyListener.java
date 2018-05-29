package de.biomia.spigot.listeners.servers;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import de.biomia.bungee.msg.BungeeMessages;
import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import de.biomia.spigot.general.cosmetics.Cosmetic;
import de.biomia.spigot.general.cosmetics.MysteryChest;
import de.biomia.spigot.listeners.LobbyInventoryManager;
import de.biomia.spigot.server.lobby.Lobby;
import de.biomia.spigot.server.lobby.LobbyScoreboard;
import de.biomia.spigot.tools.PlayerToServerConnector;
import de.biomia.universal.Messages;
import de.biomia.universal.Ranks;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class LobbyListener extends BiomiaListener {

    private static final Location stonebutton = new Location(Bukkit.getWorld("LobbyBiomia"), 465, 97, 359);

    private static final ArrayList<Player> inAir = new ArrayList<>();


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

        for (Player pl : ((Lobby) Biomia.getServerInstance()).getSilentLobby()) {
            p.hidePlayer(Main.getPlugin(), pl);
            pl.hidePlayer(Main.getPlugin(), p);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        e.setDamage(0);
        e.setCancelled(true);
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
        e.setCancelled(true);
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
            p.setFlying(false);
            if (!inAir.contains(p)) {
                p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1, 0);
                Vector jump = p.getLocation().getDirection().multiply(2.4D).setY(1.2);
                p.setVelocity(p.getVelocity().add(jump));
                inAir.add(p);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (inAir.contains(e.getPlayer())) {
            if (e.getPlayer().isOnGround()) {
                e.getPlayer().setAllowFlight(true);
                inAir.remove(e.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract_(PlayerInteractEvent e) {
        Player pl = e.getPlayer().getPlayer();
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            ItemStack itemstack = pl.getInventory().getItemInMainHand();
            if (itemstack.hasItemMeta()) {

                switch (itemstack.getType()) {
                    case COMPASS:
                        if (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(LobbyInventoryManager.getCompass().getItemMeta().getDisplayName()))
                            pl.openInventory(((Lobby) Biomia.getServerInstance()).getNavigator());
                        break;
                    case NETHER_STAR:
                        if (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(LobbyInventoryManager.getServerSwitcher().getItemMeta().getDisplayName()))
                            pl.openInventory(((Lobby) Biomia.getServerInstance()).getLobbySwitcher());
                        break;
                    case FIREBALL:
                        if (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(LobbyInventoryManager.getSilentItemOFF().getItemMeta().getDisplayName())) {
                            pl.getInventory().setItem(6, LobbyInventoryManager.getSilentItemON());
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.hidePlayer(Main.getPlugin(), pl);
                                pl.hidePlayer(Main.getPlugin(), p);
                            }
                            ((Lobby) Biomia.getServerInstance()).getSilentLobby().add(pl);
                        }
                        break;
                    case FIREWORK_CHARGE:
                        if (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(LobbyInventoryManager.getSilentItemON().getItemMeta().getDisplayName())) {
                            pl.getInventory().setItem(6, LobbyInventoryManager.getSilentItemOFF());
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (!((Lobby) Biomia.getServerInstance()).getSilentLobby().contains(p)) {
                                    p.showPlayer(Main.getPlugin(), pl);
                                    pl.showPlayer(Main.getPlugin(), p);
                                }
                            }
                            ((Lobby) Biomia.getServerInstance()).getSilentLobby().remove(pl);
                        }
                        break;
                    case CHEST:
                        if (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(LobbyInventoryManager.getCosmeticItem().getItemMeta().getDisplayName())) {
                            de.biomia.spigot.general.cosmetics.Cosmetic.openMainInventory(Biomia.getBiomiaPlayer(pl));
                        }
                        break;
                    default:
                        return;
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
                            || e.getClickedBlock().getType() == Material.LEVER
                            || e.getClickedBlock().getType() == Material.FENCE_GATE
                            || e.getClickedBlock().getType() == Material.TRAP_DOOR)
                        e.setCancelled(true);
                }
                if (!Biomia.getBiomiaPlayer(e.getPlayer()).canBuild()) e.setCancelled(true);
                if (e.getClickedBlock().getType() == Material.CHEST) { /*only chests in the beach house become mystery chests*/
                    try {
                        if (e.getClickedBlock().getLocation().distance(new Location(Bukkit.getWorld("LobbyBiomia"), 605, 68, 363)) < 10) {
                            e.setCancelled(true);
                            BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
                            int coins = bp.getCoins();
                            if (coins >= 1000) {
                                bp.takeCoins(1000);
                                MysteryChest.open(bp);
                                bp.getPlayer().sendMessage(String.format("%s%sGlückwunsch! %sDu hast ein kosmetisches Item erhalten!", Messages.PREFIX, Messages.COLOR_MAIN, Messages.COLOR_SUB));
                            } else {
                                bp.getPlayer().sendMessage(String.format("%s%sDu hast nicht genug Geld. Dir fehlen noch %s%d%sBC!", Messages.PREFIX, Messages.COLOR_MAIN, Messages.COLOR_SUB, 1000 - coins, Messages.COLOR_MAIN));
                            }
                        }
                    } catch (IllegalArgumentException ignored) {/*Location.distance throws IllegalArgs when the locations are on different worlds*/}
                }
            }
        }
    }

    @EventHandler
    public void onHitProt(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player && e.getEntityType() == EntityType.ARROW) {
            Player p = (Player) e.getEntity().getShooter();
            Location loc = e.getEntity().getLocation();
            e.getEntity().remove();

            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();
            World world = loc.getWorld();

            p.teleport(new Location(world, x, y, z, p.getLocation().getYaw(), p.getLocation().getPitch()));
        }
    }

    private static void sendRegMsg(Player p) {
        if (Biomia.getBiomiaPlayer(p).getRank() == Ranks.UnregSpieler) {
            TextComponent register = new TextComponent();
            register.setText(BungeeMessages.registerAt);
            register.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));
            p.spigot().sendMessage(register);
            p.sendMessage(String.format("%sOder später mit %s/register", Messages.COLOR_MAIN, Messages.COLOR_SUB));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player pl = (Player) e.getWhoClicked();
        if (e.getClick().isLeftClick())
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {

                String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
                if (Cosmetic.getMainInventory().equals(e.getClickedInventory()) && Cosmetic.openGroupInventory(Biomia.getBiomiaPlayer((Player) e.getWhoClicked()), e.getCurrentItem().getItemMeta().getDisplayName())) {
                    e.setCancelled(true);
                } else if (e.getClickedInventory().equals(((Lobby) Biomia.getServerInstance()).getNavigator())) {
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
                        case "§aDuell":
                            pl.teleport(new Location(Bukkit.getWorld("LobbyBiomia"), 465, 70, 243, 93, -10));
                            pl.closeInventory();
                            break;
                    }
                } else if (e.getClickedInventory().getName().equals("§bLobby Switcher"))
                    for (ServerObject so : TimoCloudAPI.getUniversalAPI().getServerGroup(BiomiaServerType.Lobby.name())
                            .getServers())
                        if (itemName.contains(so.getName()))
                            if (!so.getName().equals(TimoCloudAPI.getBukkitAPI().getThisServer().getName()))
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
