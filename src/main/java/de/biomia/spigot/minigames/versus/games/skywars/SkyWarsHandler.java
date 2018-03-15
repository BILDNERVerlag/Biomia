package de.biomia.spigot.minigames.versus.games.skywars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.messages.SkyWarsMessages;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.general.Dead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

class SkyWarsHandler extends GameHandler {

    private final ArrayList<Location> opendChests = new ArrayList<>();

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

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK)
            if (e.getClickedBlock().getType() == Material.CHEST)
                if (!opendChests.contains(e.getClickedBlock().getLocation())) {
                    Chest c = ((VersusSkyWars) mode).getChests().fillChest(e.getClickedBlock().getLocation());
                    if (c != null) {
                        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            e.setCancelled(true);
                            p.openInventory(c.getInventory());
                        }
                        opendChests.add(e.getClickedBlock().getLocation());
                    }
                }
    }

    @EventHandler
    public void onDeath_(PlayerDeathEvent e) {
        Player p = e.getEntity();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        p.getInventory().clear();
        Player killer = p.getKiller();
        if (mode.getInstance().containsPlayer(bp)) {
            Dead.respawn(p);
            String msg;
            if (killer == null)
                msg = SkyWarsMessages.playerDied.replaceAll("%p", p.getName());
            else
                msg = SkyWarsMessages.playerKilledByPlayer.replaceAll("%p1", p.getName()).replaceAll("%p2", killer.getName());
            for (BiomiaPlayer all : mode.getInstance().getPlayers())
                all.getPlayer().sendMessage(msg);
            bp.getTeam().setDead(bp);
        }
    }
}
