package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.events.game.GameDeathEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import de.biomia.spigot.events.game.bedwars.BedWarsUseShopEvent;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.messages.ParrotItemNames;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.minigames.general.ColorType;
import de.biomia.spigot.minigames.general.shop.*;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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
    public void onPlayerKill(GameKillEvent e) {
        Location loc = e.getOfflineBiomiaPlayer().getBiomiaPlayer().getPlayer().getLocation();
        if (e.isFinalKill())
            loc.getWorld().dropItem(loc, new ItemStack(Material.GOLD_INGOT, 50)).setPickupDelay(0);
        else
            loc.getWorld().dropItem(loc, new ItemStack(Material.GOLD_INGOT, 10)).setPickupDelay(0);
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
    public void onInventoryClick(InventoryClickEvent e) {
        super.onInventoryClick(e);
        if (!mode.getInstance().getWorld().equals(e.getWhoClicked().getWorld())) return;
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                ItemStack iStack = e.getCurrentItem();
                if (e.getInventory().getName().equals(BedWarsMessages.shopInventory)) {
                    for (ShopGroup group : Shop.getGroups()) {
                        if (iStack.equals(group.getIcon())) {
                            e.setCancelled(true);
                            p.openInventory(group.getInventory());
                            return;
                        }
                    }
                } else {
                    for (ShopGroup group : Shop.getGroups()) {
                        if (e.getClickedInventory().getName().equals(group.getFullName())) {
                            e.setCancelled(true);
                            if (iStack.hasItemMeta() && iStack.getItemMeta().hasDisplayName()
                                    && iStack.getItemMeta().getDisplayName().equals(BedWarsItemNames.back)) {
                                p.openInventory(Shop.getInventory());
                                return;
                            } else {
                                ShopItem shopItem = group.getShopItem(iStack);
                                if (shopItem != null) {
                                    GameTeam team = bp.getTeam();

                                    ItemStack returnItem = iStack.clone();

                                    if (shopItem.isColorable()) {
                                        if (shopItem.getType() == ColorType.LEATHER) {
                                            LeatherArmorMeta meta = (LeatherArmorMeta) returnItem.getItemMeta();
                                            switch (team.getColor()) {
                                                case BLACK:
                                                    meta.setColor(Color.BLACK);
                                                    break;
                                                case RED:
                                                    meta.setColor(Color.RED);
                                                    break;
                                                case BLUE:
                                                    meta.setColor(Color.BLUE);
                                                    break;
                                                case ORANGE:
                                                    meta.setColor(Color.ORANGE);
                                                    break;
                                                case GREEN:
                                                    meta.setColor(Color.GREEN);
                                                    break;
                                                case WHITE:
                                                    meta.setColor(Color.WHITE);
                                                    break;
                                                case PURPLE:
                                                    meta.setColor(Color.PURPLE);
                                                    break;
                                                case YELLOW:
                                                    meta.setColor(Color.YELLOW);
                                                    break;
                                                default:
                                                    break;
                                            }
                                            returnItem.setItemMeta(meta);
                                        } else {
                                            returnItem.setDurability(team.getColordata());
                                        }
                                    }
                                    if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {

                                        int i = iStack.getMaxStackSize() / iStack.getAmount();
                                        boolean first = true;

                                        for (int j = 0; j < i; j++) {
                                            if (shopItem.take(bp)) {
                                                p.getInventory().addItem(returnItem);
                                                first = false;
                                            } else if (first) {
                                                String name = ItemType.getName(shopItem.getItemType());
                                                p.sendMessage(BedWarsMessages.notEnoughItemsToPay.replace("$n", name));
                                                return;
                                            } else {
                                                return;
                                            }
                                        }

                                    } else if (shopItem.take(bp)) {
                                        p.getInventory().addItem(returnItem);
                                    } else {
                                        String name = ItemType.getName(shopItem.getItemType());
                                        p.sendMessage(BedWarsMessages.notEnoughItemsToPay.replace("$n", name));
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryInteractEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getWhoClicked().getWorld())) return;
        if (e.getInventory().getName().equals(BedWarsMessages.shopInventory))
            e.setCancelled(true);
        else for (ShopGroup group : Shop.getGroups()) {
            if (e.getInventory().getName().equals(group.getFullName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        super.onInteract(e);
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        Player p = e.getPlayer();
        boolean isArmorStand = false;
        if (e.getRightClicked() instanceof Villager || (isArmorStand = e.getRightClicked() instanceof ArmorStand))
            if (e.getRightClicked().getCustomName().contains(BedWarsMessages.shopVillagerName)) {
                e.setCancelled(true);
                Bukkit.getPluginManager().callEvent(new BedWarsUseShopEvent(Biomia.getBiomiaPlayer(p), e.getRightClicked() instanceof Villager, mode));
                p.openInventory(Shop.getInventory());
                if (isArmorStand)
                    ((BedWars) mode).handlerMap.get(e.getRightClicked().getUniqueId()).add(p);
            }
    }

    @EventHandler
    public void onBlockDestroy(EntityExplodeEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getLocation().getWorld())) return;

        if (e.getEntity().hasMetadata("FromCannon")) {
            e.setYield(0);
            double damage = e.getEntity().getMetadata("Damage").stream().findFirst().orElse(new FixedMetadataValue(Main.getPlugin(), 2)).asDouble();
            e.getLocation().getWorld().createExplosion(e.getLocation(), (float) damage);
            //if (e.getEntity().getMetadata("isShotgun").stream().findFirst().orElse(new FixedMetadataValue(Main.getPlugin(), false)).asBoolean()) {
            // TODO: split bullets on impact for shotgun
            //}
        } else {
            handleBlocks(null, e.blockList());
        }
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getBlock().getWorld())) return;
        if (handleBlocks(Biomia.getBiomiaPlayer(e.getPlayer()), Collections.singletonList(e.getBlock()))) {
            new GiveItemEvent(new Price(ItemType.GOLD, 100).getPriceItem()).executeEvent(Biomia.getBiomiaPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onBlockDestroy(BlockExplodeEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getBlock().getWorld())) return;
        handleBlocks(null, e.blockList());
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
                parrotCannonPoint.getCannon().getMainInventory().open(Biomia.getBiomiaPlayer(e.getPlayer()));
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
            ((Parrot) mode).getPoints().stream()
                    .filter(cannonPoint -> cannonPoint.getLocation().distance(e.getClickedBlock().getLocation()) < 0.1)
                    .forEach(cannonPoint -> cannonPoint.getCannon().fire());
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
                        handleBlocks(null, Collections.singletonList(b));
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
                    if (cannon.getActualYaw() != yaw)
                        cannon.setActualYaw(yaw);
                } else if (cannon.getActualPitch() != pitch)
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
                if (canPay(bp, upgrade.getPrice()) && cannon.upgrade(upgrade))
                    pay(bp, upgrade.getPrice());
            } else {

                ParrotCannon.CannonType type;
                int price;

                switch (e.getSlot()) {
                    case 2:
                        type = ParrotCannon.CannonType.SIX_POUNDER;
                        price = 0;
                        break;
                    case 3:
                        type = ParrotCannon.CannonType.MOERSER;
                        price = 200;
                        break;
                    case 4:
                        type = ParrotCannon.CannonType.TWELVE_POUNDER;
                        price = 180;
                        break;
                    case 5:
                        type = ParrotCannon.CannonType.DRILLING;
                        price = 150;
                        break;
                    case 6:
                        type = ParrotCannon.CannonType.BOMBARDE;
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
                    cannon.setType(type);
                    cannon.reset();
                    cannon.spawn();
                }
            }
            e.setCancelled(true);
        }
    }

    private boolean pay(BiomiaPlayer bp, int gold) {
        if (!canPay(bp, gold)) return false;
        bp.sendMessage(Messages.format("Upgrade erhalten"));
        new TakeItemEvent(Material.GOLD_INGOT, gold).executeEvent(bp);
        return true;
    }

    private boolean canPay(BiomiaPlayer bp, int gold) {
        boolean b = ItemConditions.hasItemInInventory(bp.getQuestPlayer(), Material.GOLD_INGOT, gold);
        if (!b) bp.sendMessage(Messages.format("Du hast nicht genug Gold!"));
        return b;
    }

    private boolean handleBlocks(BiomiaPlayer player, List<Block> blocks) {
        AtomicInteger i = new AtomicInteger(0);
        AtomicBoolean b = new AtomicBoolean(false);
        ArrayList<Block> copy = new ArrayList<>(blocks);

        copy.forEach(block -> {

            if (block.getType() == Material.STONE_BUTTON || block.getType() == Material.WOOD_BUTTON) {
                blocks.remove(block);
                return;
            }

            ParrotCannonPoint point = ((Parrot) mode).getPoints().stream().filter(parrotCannonPoint -> parrotCannonPoint.getButtonLocation().getBlock().equals(block)).findFirst().orElse(null);
            if (point != null) {
                if (player != null && point.getTeam().getColor() != player.getTeam().getColor()) {
                    point.getButtonLocation().getWorld().createExplosion( point.getButtonLocation(), 7);
                    player.getTeam().getPlayers().forEach(biomiaPlayer -> biomiaPlayer.getPlayer().getWorld().dropItem(biomiaPlayer.getPlayer().getLocation(), new ItemStack(Material.GOLD_INGOT, 4)).setPickupDelay(0));
                    player.getPlayer().getWorld().dropItem(player.getPlayer().getLocation(), new ItemStack(Material.GOLD_INGOT, 10)).setPickupDelay(0);
                    point.setDestroyed();
                    b.set(true);
                } else blocks.remove(block);
            }
            if (i.incrementAndGet() == blocks.size())
                mode.getTeams().stream().map(team -> ((ParrotTeam) team).getShip()).filter(parrotShip -> parrotShip.containsRegionLocation(block.getLocation())).findFirst().ifPresent(ParrotShip::update);
        });
        return b.get();
    }
}