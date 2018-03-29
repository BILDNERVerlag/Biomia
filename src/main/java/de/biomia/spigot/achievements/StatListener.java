package de.biomia.spigot.achievements;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.events.coins.CoinAddEvent;
import de.biomia.spigot.events.coins.CoinTakeEvent;
import de.biomia.spigot.events.cosmetics.CosmeticUsedEvent;
import de.biomia.spigot.events.game.*;
import de.biomia.spigot.events.game.bedwars.BedWarsBuyItemEvent;
import de.biomia.spigot.events.game.bedwars.BedWarsDestroyBedEvent;
import de.biomia.spigot.events.game.bedwars.BedWarsUseShopEvent;
import de.biomia.spigot.events.game.skywars.KitEvent;
import de.biomia.spigot.events.game.skywars.SkyWarsOpenChestEvent;
import de.biomia.spigot.general.cosmetics.items.CosmeticItem;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.general.kits.KitManager;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class StatListener implements Listener {

    private static final HashMap<BiomiaPlayer, BukkitRunnable> onlineTime = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.isCancelled())
            Stats.incrementStat(Stats.BiomiaStat.BlocksDestroyed, e.getPlayer(), Main.getGroupName());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!e.isCancelled())
            Stats.incrementStat(Stats.BiomiaStat.BlocksPlaced, e.getPlayer(), Main.getGroupName());
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        if (e.hasBlock() && e.getClickedBlock().getType() == Material.CHEST)
            Stats.incrementStat(Stats.BiomiaStat.ChestsOpened, e.getPlayer());
    }

    @EventHandler
    public void onDeath(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player && !e.getEntity().hasMetadata("NPC")) {
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
                    Stats.incrementStat(Stats.BiomiaStat.PlayersKilled, (Player) e.getDamager(), Biomia.getBiomiaPlayer(p).getBiomiaPlayerID() + "");
                } else {
                    Entity ent;
                    if (e.getDamager() instanceof Projectile) {
                        ent = (Entity) ((Projectile) e.getDamager()).getShooter();
                    } else
                        ent = e.getDamager();
                    Stats.incrementStat(Stats.BiomiaStat.KilledByMonster, p, ent.getType().name());
                }
            }
        } else {
            if (e.getEntity() instanceof Monster && e.getDamager() instanceof Player) {
                Monster monster = (Monster) e.getEntity();
                Player p = (Player) e.getDamager();
                if (e.getFinalDamage() >= monster.getHealth()) {
                    Stats.incrementStat(Stats.BiomiaStat.MonstersKilled, p, monster.getType().toString());
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
            if (e.getAmount() > 0)
                Stats.incrementStatBy(Stats.BiomiaStat.HealthRegenerated, (Player) e.getEntity(), (int) e.getAmount());
        }
    }

    @EventHandler
    public void onHungerRegain(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            int change = p.getFoodLevel() - e.getFoodLevel();
            if (change > 0) {
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
        if (e.getPlayer().hasMetadata("NPC")) {
            // In this case "Player" is actually a Citizens NPC.
            // We don't want CitizenNPCs in our database.
            return;
        }

        Stats.incrementStat(Stats.BiomiaStat.Logins, e.getPlayer(), Main.getGroupName());

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

        try {
            onlineTime.put(bp, new BukkitRunnable() {
                @Override
                public void run() {
                    bp.incrementOnlineMinutes();
                }
            }).runTaskTimer(Main.getPlugin(), 20 * 60, 20 * 60);
        } catch (NullPointerException ignored) {/*do nothing*/}
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        try {
            onlineTime.get(bp).cancel();
        } catch (IllegalStateException ignored) {
        } finally {
            onlineTime.remove(bp);
        }
        int minutes = bp.getActualOnlineMinutes();
        if (minutes > 0)
            Stats.incrementStatBy(Stats.BiomiaStat.MinutesPlayed, bp.getBiomiaPlayerID(), minutes);
    }

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
    public void onBedWarsDeath(GameDeathEvent e) {

        final String nokiller = "NO_KILLER";

        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            if (!e.isFinalDeath())
                Stats.incrementStat(Stats.BiomiaStat.BW_Deaths, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKiller() != null ? e.getKiller().getBiomiaPlayerID() + "" : nokiller);
            else
                Stats.incrementStat(Stats.BiomiaStat.BW_FinalDeaths, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKiller() != null ? e.getKiller().getBiomiaPlayerID() + "" : nokiller);
        } else {
            Stats.incrementStat(Stats.BiomiaStat.SW_Deaths, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKiller() != null ? e.getKiller().getBiomiaPlayerID() + "" : nokiller);
        }
    }

    @EventHandler
    public void onBedWarsKill(GameKillEvent e) {
        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            if (!e.isFinalKill()) {
                Stats.incrementStat(Stats.BiomiaStat.BW_FinalKills, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKilledPlayer().getBiomiaPlayerID() + "");
            } else {
                Stats.incrementStat(Stats.BiomiaStat.BW_Kills, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKilledPlayer().getBiomiaPlayerID() + "");
            }
        } else {
            Stats.incrementStat(Stats.BiomiaStat.SW_Kills, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKilledPlayer().getBiomiaPlayerID() + "");
        }

    }

    @EventHandler
    public void onBedWarsDestroyBedEvent(BedWarsDestroyBedEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.BW_DestroyedBeds, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getTeamColor().name());
    }

    @EventHandler
    public void onBedWarsLeaveEvent(GameLeaveEvent e) {
        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            Stats.incrementStat(Stats.BiomiaStat.BW_Leaves, e.getBiomiaPlayer().getBiomiaPlayerID());
        } else {
            Stats.incrementStat(Stats.BiomiaStat.SW_Leaves, e.getBiomiaPlayer().getBiomiaPlayerID());
        }
    }

    @EventHandler
    public void onBedWarsUseShopEvent(BedWarsUseShopEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.BW_ShopUsed, e.getBiomiaPlayer().getBiomiaPlayerID(), e.isVillager() + "");
    }

    @EventHandler
    public void onBedWarsBuyShopItemEvent(BedWarsBuyItemEvent e) {
        Stats.incrementStatBy(Stats.BiomiaStat.BW_ItemsBought, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getAmount());
        Stats.incrementStat(Stats.BiomiaStat.BW_ItemsBoughtNames, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getItem().getName());
    }

    @EventHandler
    public void onBedWarsEnd(GameEndEvent e) {

        Stats.BiomiaStat stat;
        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            stat = Stats.BiomiaStat.BW_Wins;
        } else {
            stat = Stats.BiomiaStat.SW_Wins;
        }

        e.getWinner().forEach(each -> Stats.incrementStat(stat, each.getBiomiaPlayerID(), e.getWinnerTeam().name()));
    }

    @EventHandler
    public void onBedWarsStart(GameStartEvent e) {
        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            e.getMode().getInstance().getPlayers().forEach(each -> Stats.incrementStat(Stats.BiomiaStat.BW_GamesPlayed, each.getBiomiaPlayerID(), each.getTeam().getColor().name()));
        } else
            e.getMode().getInstance().getPlayers().forEach(each -> Stats.incrementStat(Stats.BiomiaStat.SW_GamesPlayed, each.getBiomiaPlayerID(), KitManager.getManager(each).getSelectedKit().getID() + ""));
    }

    @EventHandler
    public void onSkyWarsOpenChestEvent(SkyWarsOpenChestEvent e) {
        if (e.isFirstOpen())
            Stats.incrementStat(Stats.BiomiaStat.SW_ChestsOpened, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getChestType().name());
    }

    @EventHandler
    public void onKitChangeEvent(KitEvent e) {

        switch (e.getType()) {
            case BUY:
                Stats.incrementStat(Stats.BiomiaStat.KitsBought, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKit().getID() + "");
            case SELECT:
                Stats.incrementStat(Stats.BiomiaStat.KitsChanged, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKit().getID() + "");
            case SHOW:
                Stats.incrementStat(Stats.BiomiaStat.KitsShown, e.getBiomiaPlayer().getBiomiaPlayerID(), e.getKit().getID() + "");

        }

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

    @EventHandler
    public void onEXPGain(PlayerExpChangeEvent e) {
        Stats.incrementStat(Stats.BiomiaStat.EXPGained, e.getPlayer());
    }

}
