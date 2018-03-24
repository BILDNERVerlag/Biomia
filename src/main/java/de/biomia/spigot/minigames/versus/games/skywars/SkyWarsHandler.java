package de.biomia.spigot.minigames.versus.games.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.game.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.messages.SkyWarsMessages;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.minigames.general.chests.Chests;
import de.biomia.spigot.minigames.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

class SkyWarsHandler extends GameHandler {

    SkyWarsHandler(VersusSkyWars versusSkyWars) {
        super(versusSkyWars);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (mode.getInstance().containsPlayer(Biomia.getBiomiaPlayer(p)))
            if (e.getItem() != null) {
                if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
                    switch (e.getItem().getItemMeta().getDisplayName()) {
                        case SkyWarsItemNames.playerTracker:
                            if (e.getItem().getType() == Material.COMPASS) {
                                for (Entity entity : p.getNearbyEntities(500, 500, 500)) {
                                    if (entity instanceof Player) {
                                        Player nearest = (Player) entity;
                                        BiomiaPlayer nearestbp = Biomia.getBiomiaPlayer(nearest);
                                        GameTeam nearestPlayerTeam = nearestbp.getTeam();
                                        if (nearestPlayerTeam != null && nearestPlayerTeam.lives(nearestbp) && bp.getTeam() != null && !bp.getTeam().containsPlayer(nearestbp)) {
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
                        default:
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
                    Bukkit.getPluginManager().callEvent(new SkyWarsOpenChestEvent(bp, firstOpen, chestType, mode));
                    e.setCancelled(true);
                    p.openInventory(chest.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        p.getInventory().clear();
        Player killer = p.getKiller();
        if (mode.getInstance().containsPlayer(bp)) {
            Dead.respawn(p);
            String msg;
            if (killer == null)
                msg = MinigamesMessages.playerDied.replaceAll("%p", p.getName());
            else
                msg = MinigamesMessages.playerKilledByPlayer.replaceAll("%p1", p.getName()).replaceAll("%p2", killer.getName());
            for (BiomiaPlayer all : mode.getInstance().getPlayers())
                all.getPlayer().sendMessage(msg);
            bp.getTeam().setDead(bp);
        }
    }
}
