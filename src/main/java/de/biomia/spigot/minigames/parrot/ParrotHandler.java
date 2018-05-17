package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.events.game.GameDeathEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameStateManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

class ParrotHandler extends GameHandler {

    ParrotHandler(GameMode mode) {
        super(mode);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        super.onJoin(e);
        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME)
            mode.getTeams().stream().map(team -> ((ParrotTeam) team).getShip().getBossBar()).forEach(parrotShip -> parrotShip.addPlayer(e.getPlayer()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getEntity().getWorld())) return;
        Player p = e.getEntity();
        Player killer = p.getKiller();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (!mode.isSpectator(bp)) {
            BiomiaPlayer bpKiller = Biomia.getBiomiaPlayer(killer);
            if (killer != null) {
                Bukkit.getPluginManager().callEvent(new GameKillEvent(bpKiller, bp, false, mode));
                e.setDeathMessage(MinigamesMessages.playerKilledByPlayer.replace("%p1", bp.getTeam().getColorcode() + p.getName()).replace("%p2", bpKiller.getTeam().getColorcode() + killer.getName()));
            } else
                e.setDeathMessage(MinigamesMessages.playerDied.replace("%p", bp.getTeam().getColorcode() + p.getName()));
            Bukkit.getPluginManager().callEvent(new GameDeathEvent(bp, bpKiller, false, mode));
        }
    }

    @EventHandler
    public void onBlockDestroy(BlockExplodeEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getBlock().getWorld())) return;
        handleBlocks(false, (Block[]) e.blockList().toArray());
    }

    @EventHandler
    public void onBlockDestroy(EntityExplodeEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getLocation().getWorld())) return;
        handleBlocks(false, (Block[]) e.blockList().toArray());
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getBlock().getWorld())) return;
        handleBlocks(true, e.getBlock());
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getBlock().getWorld())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        onInteractEntity(e);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        if (e.getRightClicked() instanceof ArmorStand) {
            ((Parrot) mode).getPoints().forEach((ParrotCanonPoint parrotCanonPoint) -> {
                if (e.getRightClicked().equals(parrotCanonPoint.getCanonier())) {
                    //TODO handle armorstands
                }
            });
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        if (e.hasBlock() && (e.getClickedBlock().getType() == Material.STONE_BUTTON || e.getClickedBlock().getType() == Material.WOOD_BUTTON)) {
            ((Parrot) mode).getPoints().forEach(canonPoint -> {
                if (canonPoint.getCanon().getButton().getBlock().equals(e.getClickedBlock())) {
                    canonPoint.getCanon().fire(e.getPlayer());
                }
            });
        }
    }

    @EventHandler
    public void onArrowShoot(ProjectileLaunchEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getEntity().getWorld())) return;
        if (e.getEntity() instanceof Arrow) {
            Player p = (Player) e.getEntity().getShooter();
            ItemStack is = p.getInventory().getItemInMainHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().getDisplayName().equals(ParrotItemNames.explosionBow)) {
                Arrow arrow = (Arrow) e.getEntity();
                arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                arrow.setMetadata("ExplosionArrow", new FixedMetadataValue(Main.getPlugin(), "true"));
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getEntity().getWorld())) return;
        if (e.getEntity() instanceof Arrow) {
            Player p = (Player) e.getEntity().getShooter();
            ItemStack is = p.getInventory().getItemInMainHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().getDisplayName().equals(ParrotItemNames.explosionBow)) {
                Arrow arrow = (Arrow) e.getEntity();
                if (arrow.hasMetadata("ExplosionArrow")) {
                    Block b = e.getHitBlock();
                    if (b != null) {
                        arrow.getLocation().getWorld().createExplosion(arrow.getLocation(), 0); //TODO Power anpassen
                        b.setType(Material.AIR);
                    }
                }
            }
        }
    }

    private void handleBlocks(boolean fromHand, Block... blocks) {
        Arrays.asList(blocks).forEach(block -> {
            ParrotCanonPoint point = ((Parrot) mode).getPoints().stream().filter(parrotCanonPoint -> parrotCanonPoint.getLocation().distance(block.getLocation()) < 1).findFirst().orElse(null);
            if (point != null) {
                if (!fromHand)
                    return;
                point.setDestroyed();
            }
            block.setType(Material.AIR);
            mode.getTeams().stream().map(team -> ((ParrotTeam) team).getShip()).filter(parrotShip -> parrotShip.containsRegionLocation(block.getLocation())).findFirst().ifPresent(ParrotShip::update);
        });
    }
}