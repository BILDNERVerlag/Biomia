package de.biomia.versus.vs.game.sw;

import de.biomia.versus.global.Dead;
import de.biomia.versus.sw.messages.ItemNames;
import de.biomia.versus.sw.messages.Messages;
import de.biomia.versus.vs.game.GameHandler;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
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

    SkyWarsHandler(SkyWars skyWars) {
        super(skyWars);
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        if (mode.containsPlayer(Biomia.getBiomiaPlayer(p)))
            if (e.getItem() != null) {
                if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
                    switch (e.getItem().getItemMeta().getDisplayName()) {
                        case ItemNames.playerTracker:
                            if (e.getItem().getType() == Material.COMPASS) {
                                for (Entity entity : p.getNearbyEntities(500, 500, 500)) {
                                    if (entity instanceof Player) {
                                        Player nearest = (Player) entity;
                                        if (!Biomia.TeamManager().livesPlayer(nearest) && Biomia.TeamManager().getTeam(p) != null && !Biomia.TeamManager().getTeam(p).playerInThisTeam(nearest)) {
                                            p.setCompassTarget(nearest.getLocation());
                                            p.sendMessage(Messages.compassMessages.replace("%p", nearest.getName()).replace("%d", (int) p.getLocation().distance(nearest.getLocation()) + ""));
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
                        default:
                            break;
                    }
                }
            }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK)
            if (e.getClickedBlock().getType() == Material.CHEST)
                if (!opendChests.contains(e.getClickedBlock().getLocation())) {
                    Chest c = ((SkyWars) mode).getChests().fillChest(e.getClickedBlock().getLocation());
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
    private void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        p.getInventory().clear();
        Player killer = p.getKiller();
        if (mode.containsPlayer(bp)) {
            Dead.respawn(p);
            String msg;
            if (killer == null)
                msg = Messages.playerDied.replaceAll("%p", p.getName());
            else
                msg = Messages.playerKilledByPlayer.replaceAll("%p1", p.getName()).replaceAll("%p2", killer.getName());
            for (BiomiaPlayer all : mode.getPlayers())
                all.getPlayer().sendMessage(msg);
            mode.getTeam(bp).setDead(bp);
        }
    }
}
