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
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        handleBlocks(false, e.blockList());
    }

    @EventHandler
    public void onBlockDestroy(EntityExplodeEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getLocation().getWorld())) return;

        if (e.getEntity().hasMetadata("FromCannon")) {
            e.setYield(0);
            int damage = e.getEntity().getMetadata("Damage").stream().findFirst().orElse(new FixedMetadataValue(Main.getPlugin(), 2)).asInt();
            e.getLocation().getWorld().createExplosion(e.getLocation(), damage);
            if (e.getEntity().getMetadata("isShotgun").stream().findFirst().orElse(new FixedMetadataValue(Main.getPlugin(), false)).asBoolean()) {
                // TODO launch splitter
            }
        } else {
            handleBlocks(false, e.blockList());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getBlock().getWorld())) return;
        handleBlocks(true, Collections.singletonList(e.getBlock()));
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockIgniteEvent e) {
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
            ((Parrot) mode).getPoints().stream().filter(parrotCannonPoint -> e.getRightClicked().equals(parrotCannonPoint.getCanonier())).forEach(parrotCannonPoint -> {
                e.setCancelled(true);
                parrotCannonPoint.getCanon().getMainInventory().open(Biomia.getBiomiaPlayer(e.getPlayer()));
            });
        }
    }

    @EventHandler
    public void onInteractEntity(EntityDamageEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getEntity().getWorld())) return;
        if (e.getEntity() instanceof ArmorStand) {
            if (((Parrot) mode).getPoints().stream().anyMatch(parrotCannonPoint -> e.getEntity().equals(parrotCannonPoint.getCanonier()))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        if (e.hasBlock() && (e.getClickedBlock().getType() == Material.STONE_BUTTON || e.getClickedBlock().getType() == Material.WOOD_BUTTON)) {
            ((Parrot) mode).getPoints().stream().filter(canonPoint -> canonPoint.getCanon().getButton().getBlock().equals(e.getClickedBlock())).forEach(canonPoint -> canonPoint.getCanon().fire());
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
                arrow.setMetadata("ExplosionArrow", new FixedMetadataValue(Main.getPlugin(), true));
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
                        arrow.getLocation().getWorld().createExplosion(arrow.getLocation(), 0);
                        handleBlocks(false, Collections.singletonList(b));
                        arrow.remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getWhoClicked().getWorld())) return;

        ParrotCanonInventory inventory = ParrotCanonInventory.openInventories.get(Biomia.getBiomiaPlayer((Player) e.getWhoClicked()));

        if (inventory != null) {
            ParrotCanon canon = inventory.getCannon();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getWhoClicked());

            if (inventory instanceof ParrotCanonInventory.CannonDirectionSettingInventory) {

                ParrotCanon.CannonYaw yaw = null;
                ParrotCanon.CannonPitch pitch = null;

                switch (e.getSlot()) {
                    //YAW
                    case 10:
                        yaw = ParrotCanon.CannonYaw.STRONG_LEFT;
                        break;
                    case 11:
                        yaw = ParrotCanon.CannonYaw.LEFT;
                        break;
                    case 12:
                        yaw = ParrotCanon.CannonYaw.STRAIGHT;
                        break;
                    case 13:
                        yaw = ParrotCanon.CannonYaw.RIGHT;
                        break;
                    case 14:
                        yaw = ParrotCanon.CannonYaw.STRONG_RIGHT;
                        break;
                    //PITCH
                    case 7:
                        pitch = ParrotCanon.CannonPitch.LONG;
                        break;
                    case 16:
                        pitch = ParrotCanon.CannonPitch.MIDDLE;
                        break;
                    case 25:
                        pitch = ParrotCanon.CannonPitch.SHORT;
                        break;
                    default:
                        return;
                }

                if (yaw != null) {
                    if (canon.getActualYaw() != yaw && pay(bp, 15))
                        canon.setActualYaw(yaw);
                } else if (canon.getActualPitch() != pitch && pay(bp, 10))
                    canon.setActualPitch(pitch);
                ((ParrotCanonInventory.CannonDirectionSettingInventory) inventory).init();

            } else if (inventory instanceof ParrotCanonInventory.CannonMainInventory) {
                switch (e.getSlot()) {
                    case 2:
                        canon.getSettingInventory().open(bp);
                        break;
                    case 6:
                        canon.getWeaponChangeInventory().open(bp);
                        break;
                    default:
                }
            } else if (inventory instanceof ParrotCanonInventory.CannonSettingInventory) {
                switch (e.getSlot()) {
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        canon.getDirectionSettingInventory().open(bp);
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    default:
                }
            } else {

                ParrotCanon.CannonType type;
                int price;

                switch (e.getSlot()) {
                    case 2:
                        type = ParrotCanon.CannonType.CANON;
                        price = 0;
                        break;
                    case 3:
                        type = ParrotCanon.CannonType.GRANATENWERFER;
                        price = 200;
                        break;
                    case 4:
                        type = ParrotCanon.CannonType.PANZERFAUST;
                        price = 180;
                        break;
                    case 5:
                        type = ParrotCanon.CannonType.HALBAUTOMATIK;
                        price = 150;
                        break;
                    case 6:
                        type = ParrotCanon.CannonType.SCHROTFLINTE;
                        price = 200;
                        break;
                    default:
                        return;
                }

                if (canon.getType() == type || pay(bp, price)) return;

                canon.reset();
                canon.spawn();
            }
            e.setCancelled(true);
        }
    }

    //TODO add messages
    private boolean pay(BiomiaPlayer bp, int gold) {
        if (ItemConditions.hasItemInInventory(bp.getQuestPlayer(), Material.GOLD_INGOT, gold)) {
            bp.sendMessage(String.format("%sUpgrade erhalten", Messages.COLOR_MAIN));
            new TakeItemEvent(Material.GOLD_INGOT, gold).executeEvent(bp);
            return true;
        }
        bp.sendMessage(String.format("%sDu hast nicht genug Gold!", Messages.COLOR_MAIN));
        return false;
    }

    private void handleBlocks(boolean fromHand, List<Block> blocks) {
        AtomicInteger i = new AtomicInteger(0);
        ArrayList<Block> copy = new ArrayList<>(blocks);
        copy.forEach(block -> {
            ParrotCannonPoint point = ((Parrot) mode).getPoints().stream().filter(parrotCannonPoint -> parrotCannonPoint.getLocation().clone().subtract(0, 1, 0).distance(block.getLocation()) <= 1 && block.getType() == Material.ENDER_CHEST).findFirst().orElse(null);
            if (point != null) {
                if (!fromHand) {
                    blocks.remove(block);
                    return;
                }
                point.setDestroyed();
            }
            block.setType(Material.AIR);
            if (i.incrementAndGet() == blocks.size())
                mode.getTeams().stream().map(team -> ((ParrotTeam) team).getShip()).filter(parrotShip -> parrotShip.containsRegionLocation(block.getLocation())).findFirst().ifPresent(ParrotShip::update);
        });
    }
}