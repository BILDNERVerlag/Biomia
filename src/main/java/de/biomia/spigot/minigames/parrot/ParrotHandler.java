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
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.minigames.general.shop.Price;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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
                e.setDeathMessage(Messages.format(MinigamesMessages.playerKilledByPlayer, bp.getTeam().getColorcode() + p.getName(), bpKiller.getTeam().getColorcode() + killer.getName()));
            } else
                e.setDeathMessage(Messages.format(MinigamesMessages.playerDied, bp.getTeam().getColorcode() + p.getName()));
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
            double damage = e.getEntity().getMetadata("Damage").stream().findFirst().orElse(new FixedMetadataValue(Main.getPlugin(), 2)).asDouble();
            e.getLocation().getWorld().createExplosion(e.getLocation(), (float) damage);
            if (e.getEntity().getMetadata("isShotgun").stream().findFirst().orElse(new FixedMetadataValue(Main.getPlugin(), false)).asBoolean()) {
                // TODO launch splitter
            }
        } else {
            handleBlocks(false, e.blockList());
        }
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getBlock().getWorld())) return;
        if (handleBlocks(true, Collections.singletonList(e.getBlock()))) {
            new GiveItemEvent(new Price(ItemType.GOLD, 100).getPriceItem()).executeEvent(Biomia.getBiomiaPlayer(e.getPlayer()));
        }
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
            ((Parrot) mode).getPoints().stream().filter(parrotCannonPoint -> e.getRightClicked().equals(parrotCannonPoint.getGunner())).forEach(parrotCannonPoint -> {
                e.setCancelled(true);
                parrotCannonPoint.getcannon().getMainInventory().open(Biomia.getBiomiaPlayer(e.getPlayer()));
            });
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getEntity().getWorld())) return;
        if (e.getEntity() instanceof ArmorStand) {
            if (((Parrot) mode).getPoints().stream().anyMatch(parrotCannonPoint -> e.getEntity().equals(parrotCannonPoint.getGunner()))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        if (e.hasBlock() && (e.getClickedBlock().getType() == Material.STONE_BUTTON || e.getClickedBlock().getType() == Material.WOOD_BUTTON)) {
            ((Parrot) mode).getPoints().stream().filter(cannonPoint -> cannonPoint.getcannon().getButton().getBlock().equals(e.getClickedBlock())).forEach(cannonPoint -> cannonPoint.getcannon().fire());
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
                        handleBlocks(false, Collections.singletonList(b));
                        arrow.getLocation().getWorld().createExplosion(arrow.getLocation(), 0);
                        b.setType(Material.AIR);
                        arrow.remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getWhoClicked().getWorld())) return;

        if (e.getClickedInventory() instanceof PlayerInventory) return;

        ParrotCannonInventory inventory = ParrotCannonInventory.openInventories.get(Biomia.getBiomiaPlayer((Player) e.getWhoClicked()));

        if (inventory != null) {
            ParrotCannon cannon = inventory.getCannon();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getWhoClicked());

            if (inventory instanceof ParrotCannonInventory.CannonDirectionSettingInventory) {

                ParrotCannon.CannonYaw yaw = null;
                ParrotCannon.CannonPitch pitch = null;

                switch (e.getSlot()) {
                    //YAW
                    case 10:
                        yaw = ParrotCannon.CannonYaw.STRONG_LEFT;
                        break;
                    case 11:
                        yaw = ParrotCannon.CannonYaw.LEFT;
                        break;
                    case 12:
                        yaw = ParrotCannon.CannonYaw.STRAIGHT;
                        break;
                    case 13:
                        yaw = ParrotCannon.CannonYaw.RIGHT;
                        break;
                    case 14:
                        yaw = ParrotCannon.CannonYaw.STRONG_RIGHT;
                        break;
                    //PITCH
                    case 7:
                        pitch = ParrotCannon.CannonPitch.LONG;
                        break;
                    case 16:
                        pitch = ParrotCannon.CannonPitch.MIDDLE;
                        break;
                    case 25:
                        pitch = ParrotCannon.CannonPitch.SHORT;
                        break;
                    default:
                        return;
                }

                if (yaw != null) {
                    if (cannon.getActualYaw() != yaw && pay(bp, 15))
                        cannon.setActualYaw(yaw);
                } else if (cannon.getActualPitch() != pitch && pay(bp, 10))
                    cannon.setActualPitch(pitch);
                ((ParrotCannonInventory.CannonDirectionSettingInventory) inventory).init();

            } else if (inventory instanceof ParrotCannonInventory.CannonMainInventory) {
                switch (e.getSlot()) {
                    case 2:
                        cannon.getSettingInventory().open(bp);
                        break;
                    case 6:
                        cannon.getWeaponChangeInventory().open(bp);
                        break;
                    default:
                }
            } else if (inventory instanceof ParrotCannonInventory.CannonSettingInventory) {
                ParrotCannon.CannonUpgrade upgrade;
                int price = 10; //TODO
                switch (e.getSlot()) {
                    case 2:
                        upgrade = ParrotCannon.CannonUpgrade.FAST_RELOAD;
                        break;
                    case 3:
                        upgrade = ParrotCannon.CannonUpgrade.DAMAGE;
                        break;
                    case 4:
                        cannon.getDirectionSettingInventory().open(bp);
                        return;
                    case 5:
                        upgrade = ParrotCannon.CannonUpgrade.SCATTERING;
                        break;
                    case 6:
                        upgrade = ParrotCannon.CannonUpgrade.BULLET;
                        break;
                    default:
                        return;
                }
                if (canPay(bp, price) && cannon.upgrade(upgrade))
                    pay(bp, price);
            } else {

                ParrotCannon.CannonType type;
                int price;

                switch (e.getSlot()) {
                    case 2:
                        type = ParrotCannon.CannonType.KANONE;
                        price = 0;
                        break;
                    case 3:
                        type = ParrotCannon.CannonType.GRANATENWERFER;
                        price = 200;
                        break;
                    case 4:
                        type = ParrotCannon.CannonType.PANZERFAUST;
                        price = 180;
                        break;
                    case 5:
                        type = ParrotCannon.CannonType.HALBAUTOMATIK;
                        price = 150;
                        break;
                    case 6:
                        type = ParrotCannon.CannonType.SCHROTFLINTE;
                        price = 200;
                        break;
                    default:
                        return;
                }

                if (cannon.getType() == type) {
                    bp.sendMessage(String.format("%sDu hast diese Kanone bereits!", Messages.COLOR_MAIN));
                    e.setCancelled(true);
                    return;
                } else if (pay(bp, price)) {
                    cannon.reset();
                    cannon.spawn();
                }
            }
            e.setCancelled(true);
        }
    }

    private boolean pay(BiomiaPlayer bp, int gold) {
        if (!canPay(bp, gold)) return false;
        bp.sendMessage(String.format("%sUpgrade erhalten", Messages.COLOR_MAIN));
        new TakeItemEvent(Material.GOLD_INGOT, gold).executeEvent(bp);
        return true;
    }

    private boolean canPay(BiomiaPlayer bp, int gold) {
        boolean b = ItemConditions.hasItemInInventory(bp.getQuestPlayer(), Material.GOLD_INGOT, gold);
        if (!b) bp.sendMessage(String.format("%sDu hast nicht genug Gold!", Messages.COLOR_MAIN));
        return b;
    }

    private boolean handleBlocks(boolean fromHand, List<Block> blocks) {
        AtomicInteger i = new AtomicInteger(0);
        AtomicBoolean b = new AtomicBoolean(false);
        ArrayList<Block> copy = new ArrayList<>(blocks);
        copy.forEach(block -> {
            ParrotCannonPoint point = ((Parrot) mode).getPoints().stream().filter(parrotCannonPoint -> parrotCannonPoint.getLocation().clone().subtract(0, 1, 0).distance(block.getLocation()) <= 1 && block.getType() == Material.ENDER_CHEST).findFirst().orElse(null);
            if (point != null) {
                if (fromHand) {
                    point.setDestroyed();
                    b.set(true);
                    return;
                }
                blocks.remove(block);
            }
            if (i.incrementAndGet() == blocks.size())
                mode.getTeams().stream().map(team -> ((ParrotTeam) team).getShip()).filter(parrotShip -> parrotShip.containsRegionLocation(block.getLocation())).findFirst().ifPresent(ParrotShip::update);
        });
        return b.get();
    }
}