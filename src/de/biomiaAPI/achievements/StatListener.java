package de.biomiaAPI.achievements;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;

public class StatListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.BlocksDestroyed, e.getPlayer(), Main.getGroupName());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.BlocksPlaced, e.getPlayer(), Main.getGroupName());
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock().getType() == Material.CHEST)
            Stats.incrementStat(Stats.BiomiaStat.ChestsOpened, e.getPlayer());
    }

    @EventHandler
    public void onDeath(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Stats.incrementStat(Stats.BiomiaStat.HealthLost, p, e.getCause().name());
            if (e.getFinalDamage() >= p.getHealth()) {
                Stats.incrementStat(Stats.BiomiaStat.DeathCause, p, e.getCause().name());
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getFinalDamage() >= p.getHealth()) {
               if(e.getDamager() instanceof  Player){
                   Stats.incrementStat(Stats.BiomiaStat.KilledByPlayer, p, Biomia.getBiomiaPlayer((Player) e.getDamager()).getBiomiaPlayerID() + "");
               }else {

                   Entity ent;

                   if(e.getDamager() instanceof ProjectileSource){
                       ent = (Entity) ((Projectile) e.getDamager()).getShooter();
                   }else
                       ent = e.getDamager();

                   Stats.incrementStat(Stats.BiomiaStat.KilledByMonster, p, ent.getType().name());
               }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Stats.incrementStat(Stats.BiomiaStat.FishCaught, e.getPlayer());
        }
    }

    @EventHandler
    public void onRegain(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
      //  Stats.incrementStatBy(Stats.BiomiaStat.HungerRegenerated, e.getAmount(), e.getEntity());
        }
    }





}
