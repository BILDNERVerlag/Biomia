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
            BiomiaStat.BlocksDestroyed.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, Biomia.getServerInstance().getServerType().name());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!e.isCancelled())
            BiomiaStat.BlocksPlaced.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, Biomia.getServerInstance().getServerType().name());
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        if (e.hasBlock() && e.getClickedBlock().getType() == Material.CHEST)
            BiomiaStat.ChestsOpened.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, null);
    }

    @EventHandler
    public void onDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && !e.getEntity().hasMetadata("NPC")) {
            Player p = (Player) e.getEntity();
            BiomiaStat.HealthLost.increment(Biomia.getBiomiaPlayer(p).getBiomiaPlayerID(), 1, e.getCause().name());
            if (e.getFinalDamage() >= p.getHealth()) {
                BiomiaStat.DeathCause.increment(Biomia.getBiomiaPlayer(p).getBiomiaPlayerID(), 1, e.getCause().name());
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getFinalDamage() >= p.getHealth()) {
                if (e.getDamager() instanceof Player) {

                    BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
                    BiomiaPlayer damagerBP = Biomia.getBiomiaPlayer((Player) e.getDamager());

                    BiomiaStat.KilledByPlayer.increment(bp.getBiomiaPlayerID(), 1, damagerBP.getBiomiaPlayerID() + "");
                    BiomiaStat.PlayersKilled.increment(damagerBP.getBiomiaPlayerID(), 1, bp.getBiomiaPlayerID() + "");
                } else {
                    BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

                    Entity ent;
                    if (e.getDamager() instanceof Projectile) {
                        ent = (Entity) ((Projectile) e.getDamager()).getShooter();
                    } else
                        ent = e.getDamager();
                    BiomiaStat.KilledByMonster.increment(bp.getBiomiaPlayerID(), 1, ent.getType().name());
                }
            }
        } else {
            if (e.getEntity() instanceof Monster && e.getDamager() instanceof Player) {
                Monster monster = (Monster) e.getEntity();
                Player p = (Player) e.getDamager();
                if (e.getFinalDamage() >= monster.getHealth()) {
                    BiomiaStat.MonstersKilled.increment(Biomia.getBiomiaPlayer(p).getBiomiaPlayerID(), 1, monster.getType().toString());
                }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            BiomiaStat.FishCaught.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, null);
        }
    }

    @EventHandler
    public void onRegain(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getAmount() > 0)
                BiomiaStat.HealthRegenerated.increment(Biomia.getBiomiaPlayer((Player) e.getEntity()).getBiomiaPlayerID(), (int) e.getAmount(), null);
        }
    }

    @EventHandler
    public void onHungerRegain(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            int change = p.getFoodLevel() - e.getFoodLevel();
            if (change > 0) {
                change *= -1;
                BiomiaStat.HungerLost.increment(Biomia.getBiomiaPlayer((Player) e.getEntity()).getBiomiaPlayerID(), change, null);
            } else {
                BiomiaStat.HungerRegenerated.increment(Biomia.getBiomiaPlayer((Player) e.getEntity()).getBiomiaPlayerID(), change, null);
            }
        }
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.POTION) {
            BiomiaStat.PotionsConsumed.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, null);
        } else {
            BiomiaStat.FoodEaten.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, null);
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) {
            // In this case "Player" is actually a Citizens NPC.
            // We don't want CitizenNPCs in our database.
            return;
        }

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        BiomiaStat.Logins.increment(bp.getBiomiaPlayerID(), 1, Biomia.getServerInstance().getServerType().name());

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
            BiomiaStat.MinutesPlayed.increment(bp.getBiomiaPlayerID(), minutes, null);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        BiomiaStat.MessagesSent.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, e.getMessage());
    }

    @EventHandler
    public void onCoinAdd(CoinAddEvent e) {
        BiomiaStat.CoinsAccumulated.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), e.getAmount(), null);
    }

    @EventHandler
    public void onCoinTake(CoinTakeEvent e) {
        BiomiaStat.CoinsSpent.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), e.getAmount(), null);
    }

    @EventHandler
    public void onBedWarsDeath(GameDeathEvent e) {

        final String nokiller = "NO_KILLER";

        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            if (!e.isFinalDeath())
                BiomiaStat.BW_Deaths.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.getKiller() != null ? e.getKiller().getBiomiaPlayerID() + "" : nokiller);
            else
                BiomiaStat.BW_FinalDeaths.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.getKiller() != null ? e.getKiller().getBiomiaPlayerID() + "" : nokiller);
        } else if (e.getMode().getInstance().getType() == GameType.SKY_WARS) {
            BiomiaStat.SW_Deaths.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.getKiller() != null ? e.getKiller().getBiomiaPlayerID() + "" : nokiller);
        }
    }

    @EventHandler
    public void onBedWarsKill(GameKillEvent e) {
        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            if (!e.isFinalKill()) {
                BiomiaStat.BW_FinalKills.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.getKilledPlayer().getBiomiaPlayerID() + "");
            } else {
                BiomiaStat.BW_Kills.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.getKilledPlayer().getBiomiaPlayerID() + "");
            }
        } else if (e.getMode().getInstance().getType() == GameType.SKY_WARS) {
            BiomiaStat.SW_Kills.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.getKilledPlayer().getBiomiaPlayerID() + "");
        }
    }

    @EventHandler
    public void onBedWarsDestroyBedEvent(BedWarsDestroyBedEvent e) {
        BiomiaStat.BW_DestroyedBeds.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.getTeamColor().name());
    }

    @EventHandler
    public void onBedWarsLeaveEvent(GameLeaveEvent e) {
        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            BiomiaStat.BW_Leaves.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, null);
        } else if (e.getMode().getInstance().getType() == GameType.SKY_WARS) {
            BiomiaStat.SW_Leaves.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, null);
        }
    }

    @EventHandler
    public void onBedWarsUseShopEvent(BedWarsUseShopEvent e) {
        BiomiaStat.BW_ShopUsed.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.isVillager() + "");
    }

    @EventHandler
    public void onBedWarsBuyShopItemEvent(BedWarsBuyItemEvent e) {
        BiomiaStat.BW_ItemsBought.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), e.getAmount(), e.getItem().getName());
    }

    @EventHandler
    public void onBedWarsEnd(GameEndEvent e) {

        BiomiaStat stat;
        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            stat = BiomiaStat.BW_Wins;
        } else {
            stat = BiomiaStat.SW_Wins;
        }

        e.getWinner().forEach(each -> stat.increment(each.getBiomiaPlayerID(), 1, e.getWinnerTeam().name()));
    }

    @EventHandler
    public void onBedWarsStart(GameStartEvent e) {
        if (e.getMode().getInstance().getType() == GameType.BED_WARS) {
            e.getMode().getInstance().getPlayers().forEach(each -> BiomiaStat.BW_GamesPlayed.increment(each.getBiomiaPlayerID(), 1, each.getTeam().getColor().name()));
        } else
            e.getMode().getInstance().getPlayers().forEach(each -> BiomiaStat.SW_GamesPlayed.increment(each.getBiomiaPlayerID(), 1, KitManager.getManager(each).getSelectedKit().getID() + ""));
    }

    @EventHandler
    public void onSkyWarsOpenChestEvent(SkyWarsOpenChestEvent e) {
        if (e.isFirstOpen())
            BiomiaStat.SW_ChestsOpened.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.getChestType().name());
    }

    @EventHandler
    public void onKitChangeEvent(KitEvent e) {

        BiomiaStat stat = null;

        switch (e.getType()) {
            case BUY:
                stat = BiomiaStat.KitsBought;
                break;
            case SELECT:
                stat = BiomiaStat.KitsChanged;
                break;
            case SHOW:
                stat = BiomiaStat.KitsShown;
                break;
        }

        stat.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, e.getKit().getID() + "");

    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) {
            // In this case "Player" is actually a Citizens NPC.
            // We don't want CitizenNPCs in our database.
            return;
        }
        BiomiaStat.TeleportsMade.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, Biomia.getServerInstance().getServerType().name());
    }

    @EventHandler
    public void onCosmeticUse(CosmeticUsedEvent e) {
        BiomiaStat stat = null;
        CosmeticItem eventItem = e.getItem();
        switch (eventItem.getGroup()) {
            case HEADS:
                stat = BiomiaStat.HeadsUsed;
                break;
            case SUITS:
                stat = BiomiaStat.SuitsUsed;
                break;
            case GADGETS:
                stat = BiomiaStat.GadgetsUsed;
                break;
            case PETS:
                stat = BiomiaStat.PetsUsed;
                break;
            case PARTICLES:
                stat = BiomiaStat.ParticlesUsed;
                break;
        }
        stat.increment(e.getOfflineBiomiaPlayer().getBiomiaPlayerID(), 1, eventItem.getName());
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent e) {
        if (e.getEntity().getType() == EntityType.SHEEP) {
            BiomiaStat.SheepsSheared.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, null);
        }
    }

    @EventHandler
    public void onEXPGain(PlayerExpChangeEvent e) {
        BiomiaStat.EXPGained.increment(Biomia.getBiomiaPlayer(e.getPlayer()).getBiomiaPlayerID(), 1, null);
    }

}
