package de.biomia.achievements;

import de.biomia.Biomia;
import de.biomia.BiomiaPlayer;
import de.biomia.Main;
import de.biomia.events.bedwars.*;
import de.biomia.events.cosmetics.CosmeticUsedEvent;
import de.biomia.events.general.CoinAddEvent;
import de.biomia.events.general.CoinTakeEvent;
import de.biomia.events.skywars.*;
import de.biomia.general.cosmetics.CosmeticItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class StatListener implements Listener {

    private static HashMap<BiomiaPlayer, BukkitRunnable> bukkittasks = new HashMap<>();

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
        MessagesSent,
        TeleportsMade,
        GadgetsUsed, HeadsUsed, ParticlesUsed, SuitsUsed, PetsUsed
        FoodEaten, PotionsConsumed,
        SheepsSheared,
        BW_Deaths, BW_Wins, BW_Kills, BW_Leaves, BW_ItemsBought,

        MinutesPlayed,
        MysteryChestsOpened,
        ItemsEnchanted, ItemsPickedUp, ItemsDropped, ItemsBroken,
        EXPGained,
        ProjectilesShot,
        KilometresRun,
        ReportsMade,
        SW_GamesPlayed, BW_GamesPlayed,
        SW_Deaths, SW_Wins, SW_Kills, SW_Leaves, SW_ChestsOpened, KitsBought, KitsChanged,
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
        if (e.hasBlock() && e.getClickedBlock().getType() == Material.CHEST)
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
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.POTION) {
            Stats.incrementStat(Stats.BiomiaStat.PotionsConsumed, e.getPlayer());
        } else {
            Stats.incrementStat(Stats.BiomiaStat.FoodEaten, e.getPlayer());
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.Logins, e.getPlayer(), Main.getGroupName());
    }

//    @EventHandler
//    public void onQuit(PlayerQuitEvent e) {
//        //track minutes played
//    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.MessagesSent, e.getPlayer(), e.getMessage());
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
        if (!e.isFinalDeath())
            Stats.incrementStat(Stats.BiomiaStat.BW_Deaths, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKiller() != null ? e.getKiller().getBiomiaPlayerID() + "" : "NO_KILLER");
        else
            Stats.incrementStat(Stats.BiomiaStat.BW_FinalDeaths, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKiller() != null ? e.getKiller().getBiomiaPlayerID() + "" : "NO_KILLER");
    }

    @EventHandler
    public void onBedWarsKill(BedWarsKillEvent e) {
        if (!e.isFinalKill())
            Stats.incrementStat(Stats.BiomiaStat.BW_FinalKills, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKilledPlayer().getBiomiaPlayerID() + "");
        else
            Stats.incrementStat(Stats.BiomiaStat.BW_Kills, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKilledPlayer().getBiomiaPlayerID() + "");

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
    public void onBedWarsEnd(BedWarsEndEvent e) {
        e.getBiomiaPlayerIDWinner().forEach(each -> Stats.incrementStat(Stats.BiomiaStat.BW_Wins, each.getBiomiaPlayerID(), e.getTeamcolor()));
    }

    @EventHandler
    public void onBedWarsStart(BedWarsStartEvent e) {
        e.getPlayers().keySet().forEach(each -> {
            String teamName = e.getPlayers().get(each);
            Stats.incrementStat(Stats.BiomiaStat.BW_GamesPlayed, each.getBiomiaPlayerID(), teamName);
        });
    }

    @EventHandler
    public void onSkyWarsDeath(SkyWarsDeathEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.SW_Deaths, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKiller() != null ? e.getKiller().getBiomiaPlayerID() + "" : "NO_KILLER");
    }

    @EventHandler
    public void onSkyWarsKill(SkyWarsKillEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.SW_Kills, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKilledPlayer().getBiomiaPlayerID() + "");

    }

    @EventHandler
    public void onSkyWarsOpenChestEvent(SkyWarsOpenChestEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.SW_ChestsOpened, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getChestType().name());
    }

    @EventHandler
    public void onSkyWarsLeaveEvent(SkyWarsLeaveEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.SW_Leaves, e.getBiomiaPlayer().getBiomiaPlayerID());
    }

    @EventHandler
    public void onSkyWarsEnd(SkyWarsEndEvent e) {
        e.getBiomiaPlayerWinner().forEach(each -> Stats.incrementStat(Stats.BiomiaStat.SW_Wins, each.getBiomiaPlayerID(), e.getTeamcolor()));
    }

    @EventHandler
    public void onSkyWarsStart(SkyWarsStartEvent e) {
        e.getPlayers().keySet().forEach(each -> {
            int kitID = e.getPlayers().get(each);
            Stats.incrementStat(Stats.BiomiaStat.SW_GamesPlayed, each.getBiomiaPlayerID(), kitID + "");
        });
    }

    @EventHandler
    public void onKitChangeEvent(KitChangeEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.KitsChanged, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKitID() + "");

    }

    @EventHandler
    public void onKitBuyEvent(KitBuyEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.KitsBought, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKitID() + "");
    }

    @EventHandler
    public void onKitShowEvent(KitShowEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.KitsShown, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKitID() + "");
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) {
            // In this case "Player" is actually a Citizens NPC.
            // We don't want CitizenNPCs in our database.
            return;
        }
        Stats.incrementStat(Stats.BiomiaStat.TeleportsMade, e.getPlayer(), Main.getGroupName());
    }

    @EventHandler
    public void onCosmeticUse(CosmeticUsedEvent e) {
        Stats.BiomiaStat stat = null;
        CosmeticItem eventItem = e.getItem();
        switch (eventItem.getGroup()) {
        case HEADS:
            stat = Stats.BiomiaStat.HeadsUsed;
            break;
        case SUITS:
            stat = Stats.BiomiaStat.SuitsUsed;
            break;
        case GADGETS:
            stat = Stats.BiomiaStat.GadgetsUsed;
            break;
        case PETS:
            stat = Stats.BiomiaStat.PetsUsed;
            break;
        case PARTICLES:
            stat = Stats.BiomiaStat.ParticlesUsed;
            break;
        }
        Stats.incrementStat(stat, e.getBiomiaPlayer().getBiomiaPlayerID(), eventItem.getName());
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent e) {
        if (e.getEntity().getType() == EntityType.SHEEP) {
            Stats.incrementStat(Stats.BiomiaStat.SheepsSheared, e.getPlayer());
        }
    }

}
