package de.biomiaAPI.achievements;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.achievements.statEvents.bedwars.*;
import de.biomiaAPI.achievements.statEvents.general.CoinAddEvent;
import de.biomiaAPI.achievements.statEvents.general.CoinTakeEvent;
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
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.projectiles.ProjectileSource;

public class StatListener implements Listener {

    /*
        DONE
        CoinsAccumulated
        BlocksPlaced, BlocksDestroyed,
        ChestsOpened,
        MonstersKilled, PlayersKilled,
        FishCaught,
        Logins,
        HealthLost, HealthRegenerated, HungerLost, HungerRegenerated,
        DeathCause, KilledByMonster, KilledByPlayer,

        MinutesPlayed,
        MysteryChestsOpened,
        MessagesSent,
        ItemsEnchanted, ItemsPickedUp, ItemsDropped, ItemsBroken,
        EXPGained,
        ProjectilesShot,
        FoodEaten, PotionsConsumed,
        KilometresRun,
        SheepsSheared,
        TeleportsMade,
        GadgetsUsed, HeadsUsed, ParticlesUsed, SuitsUsed,
        ReportsMade,
        SW_GamesPlayed, BW_GamesPlayed,
        SW_Deaths, SW_Wins, SW_Kills, SW_Leaves, SW_ChestsOpened, KitsBought, KitsChanged,
        BW_Deaths, BW_Wins, BW_Kills, BW_Leaves, BW_ItemsBought,
        Q_accepted, Q_returned, Q_NPCTalks, Q_CoinsEarned, Q_Kills, Q_Deaths,
        Bau_PlotsClaimed, Bau_PlotsReset,
        FB_CBClaimed, FB_CBUnclaimed, FB_ItemsBought, FB_ItemsSold, FB_WarpsUsed,
     */


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
                if (e.getDamager() instanceof Player) {
                    Stats.incrementStat(Stats.BiomiaStat.KilledByPlayer, p, Biomia.getBiomiaPlayer((Player) e.getDamager()).getBiomiaPlayerID() + "");
                } else {

                    Entity ent;

                    if (e.getDamager() instanceof ProjectileSource) {
                        ent = (Entity) ((Projectile) e.getDamager()).getShooter();
                    } else
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
            Stats.incrementStatBy(Stats.BiomiaStat.HealthRegenerated, (Player) e.getEntity(), (int) e.getAmount());
        }
    }

    @EventHandler
    public void onHungerRegain(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            int change = p.getFoodLevel() - e.getFoodLevel();
            if (change < 0) {
                change *= -1;
                Stats.incrementStatBy(Stats.BiomiaStat.HungerLost, (Player) e.getEntity(), change);
            } else {
                Stats.incrementStatBy(Stats.BiomiaStat.HungerRegenerated, (Player) e.getEntity(), change);
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.Logins, e.getPlayer(), Main.getGroupName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        //minutes played
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
    }

    @EventHandler
    public void onCoinAdd(CoinAddEvent e) {
        Stats.incrementStatBy(Stats.BiomiaStat.CoinsAccumulated, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getAmount());
    }

    @EventHandler
    public void onCoinTake(CoinTakeEvent e) {
        Stats.incrementStatBy(Stats.BiomiaStat.CoinsSpent, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getAmount());
    }

    @EventHandler
    public void onBedWarsDeath(BedWarsDeathEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.BW_Deaths, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKiller().getBiomiaPlayerID() + "");
    }

    @EventHandler
    public void onBedWarsKill(BedWarsKillEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.BW_Deaths, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKilledPlayer().getBiomiaPlayerID() + "");
    }

    @EventHandler
    public void onBedWarsDestroyBedEvent(BedWarsDestroyBedEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.BW_DestroyedBeds, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getTeamcolor());
    }

    @EventHandler
    public void onBedWarsLeaveEvent(BedWarsLeaveEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.BW_Leaves, e.getBiomiaPlayer().getBiomiaPlayerID());
    }

    @EventHandler
    public void onBedWarsUseShopEvent(BedWarsUseShopEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.BW_ShopUsed, e.getBiomiaPlayer().getBiomiaPlayerID(), e.isVillager() + "");
    }

    @EventHandler
    public void onBedWarsBuyShopItemEvent(BedWarsBuyItemEvent e) {
        Stats.incrementStatBy(Stats.BiomiaStat.BW_ItemsBought, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getAmount());
        Stats.incrementStat(Stats.BiomiaStat.BW_ItemsBoughtNames, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getItemName());
    }

    @EventHandler
    public void onBedWarsEnd(BedWarsEndEvent e){

    }
}
