package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.events.game.GameDeathEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import de.biomia.spigot.events.game.bedwars.BedWarsUseShopEvent;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.messages.manager.ActionBar;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.WarteLobbyListener;
import de.biomia.spigot.minigames.general.ColorType;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.minigames.general.shop.Shop;
import de.biomia.spigot.minigames.general.shop.ShopGroup;
import de.biomia.spigot.minigames.general.shop.ShopItem;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.Particles;
import de.biomia.universal.Messages;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class BedWarsHandler extends GameHandler {

    private final HashMap<GameTeam, Inventory> teamChests = new HashMap<>();
    private final ArrayList<Block> destroyableBlocks = new ArrayList<>();

    BedWarsHandler(BedWars mode) {
        super(mode);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

        if (WarteLobbyListener.inLobbyOrSpectator(bp) || !bp.canBuild()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BedWarsMessages.cantPlaceBlock);
            return;
        }

        destroyableBlocks.add(e.getBlock());
        if (e.getBlock().getType() == Material.ENDER_CHEST) {
            GameTeam team = bp.getTeam();
            if (team != null) {
                if (!((BedWars) mode).teamChestsLocs.containsKey(team)) {
                    ((BedWars) mode).teamChestsLocs.put(team, new ArrayList<>());
                }
                ((BedWars) mode).teamChestsLocs.get(team).add(e.getBlock());
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (WarteLobbyListener.inLobbyOrSpectator(bp) || !bp.canBuild()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BedWarsMessages.cantDestroyThisBlock);
            return;
        }
        if (destroyableBlocks.contains(e.getBlock())) {
            destroyableBlocks.remove(e.getBlock());
        } else if (e.getBlock().getType() == Material.BED_BLOCK) {
            for (GameTeam gt : mode.getTeams()) {
                if (gt instanceof BedWarsTeam) {
                    BedWarsTeam bt = ((BedWarsTeam) gt);
                    if (bt.getBed().contains(e.getBlock())) {
                        if (bt.equals(bp.getTeam())) {
                            e.getPlayer().sendMessage(MinigamesMessages.destroyOwnBed);
                            e.setCancelled(true);
                            return;
                        }
                        bt.destroyBed();
                        Bukkit.broadcastMessage(String.format(BedWarsMessages.bedWasDestroyed, bt.getColorcode(), Messages.COLOR_AUX, bt.getColorcode(), bt.getTeamname(), Messages.COLOR_AUX));
                        e.setDropItems(false);
                        return;
                    }
                }
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onVillagerDamage(EntityDamageEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getEntity().getWorld())) return;
        if (e.getEntityType() == EntityType.VILLAGER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getEntity().getWorld())) return;
        Player p = e.getEntity();
        Player killer = p.getKiller();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        GameTeam team = bp.getTeam();
        e.getEntity().getInventory().clear();
        if (!mode.isSpectator(bp)) {
            if (!((BedWarsTeam) team).hasBed()) {
                if (killer == null) {
                    Bukkit.getPluginManager().callEvent(new GameDeathEvent(bp, null, true, mode));
                } else {
                    BiomiaPlayer bpKiller = Biomia.getBiomiaPlayer(killer);
                    Bukkit.getPluginManager().callEvent(new GameKillEvent(bpKiller, bp, true, mode));
                    Bukkit.getPluginManager().callEvent(new GameDeathEvent(bp, bpKiller, true, mode));
                }
                e.setDeathMessage(MinigamesMessages.playerDiedFinally.replace("%p", team.getColorcode() + p.getName()));
                team.setDead(bp);
            } else {
                if (killer == null) {
                    Bukkit.getPluginManager().callEvent(new GameDeathEvent(bp, null, true, mode));
                    e.setDeathMessage(MinigamesMessages.playerDied.replace("%p", team.getColorcode() + p.getName()));
                } else {
                    BiomiaPlayer bpKiller = Biomia.getBiomiaPlayer(killer);
                    Bukkit.getPluginManager().callEvent(new GameKillEvent(bpKiller, bp, true, mode));
                    Bukkit.getPluginManager().callEvent(new GameDeathEvent(bp, bpKiller, true, mode));
                    e.setDeathMessage(String.format(MinigamesMessages.playerKilledByPlayer, team.getColorcode() + p.getName(), bpKiller.getTeam().getColorcode() + killer.getName()));
                }
            }
            Dead.respawn(p);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

        Location loc = ((BedWars) mode).starts.get(bp);
        if (loc != null && loc.distance(e.getTo()) > .5)
            ((BedWars) mode).starts.remove(bp);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        for (ArrayList uuid : ((BedWars) mode).handlerMap.values()) {
            uuid.remove(e.getPlayer());
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
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
                } else if (mode.getTeamSwitcher() != null && e.getClickedInventory().getName().equals(mode.getTeamSwitcher().getName())) {
                    mode.getTeamFromData(e.getCurrentItem().getData().getData()).join(bp);
                    e.setCancelled(true);
                    p.closeInventory();
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

                                    if (shopItem.isColorble()) {
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
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof Villager || e.getRightClicked() instanceof ArmorStand)
            if (e.getRightClicked().getCustomName().equals(BedWarsMessages.shopVillagerName)) {
                e.setCancelled(true);
                Bukkit.getPluginManager().callEvent(new BedWarsUseShopEvent(Biomia.getBiomiaPlayer(p), e.getRightClicked() instanceof Villager, mode));
                p.openInventory(Shop.getInventory());
            } else if (e.getRightClicked().getCustomName().contains("Sekunden")) {
                e.setCancelled(true);
                p.openInventory(Shop.getInventory());
                ((BedWars) mode).handlerMap.get(e.getRightClicked().getUniqueId()).add(p);
            }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (!e.isRepair())
            e.getInventory().setResult(ItemCreator.itemCreate(Material.AIR));
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                GameTeam t = ((BedWars) mode).getTeamByTeamChests(e.getClickedBlock());
                if (t != null) {
                    Inventory inv = teamChests.computeIfAbsent(t, t1 -> Bukkit.createInventory(null, 27, "\u00A78Team-Kiste: " + t1.getColorcode() + t1.getColor().translate()));
                    e.setCancelled(true);
                    p.openInventory(inv);
                }
            }
        }
        if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
            String displayname = e.getItem().getItemMeta().getDisplayName();

            switch (displayname) {
                case BedWarsItemNames.warper:
                    if (((BedWars) mode).starts.get(bp) == null) {
                        warpHome(bp, e.getItem());
                    }
                    break;
                case BedWarsItemNames.wand:
                    e.setCancelled(true);
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                        buildProtectionWall(p);
                    break;
                case BedWarsItemNames.villagerSpawner:
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        spawnVillager(e.getClickedBlock().getLocation().add(0.5, 1, 0.5));
                        e.setCancelled(true);
                    }
                    break;
                case BedWarsItemNames.shortShop:
                    e.setCancelled(true);
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        spawn30secShop(e.getClickedBlock().getLocation().add(0.5, 1, 0.5), e.getItem());
                        e.setCancelled(true);
                    }
                    break;
                case BedWarsItemNames.rettungsPlattform:
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        buildRettungsplattform(e.getPlayer().getLocation(), e.getItem());
                        e.setCancelled(true);
                    }
                    break;
            }
            if (e.getItem().getType() == Material.MONSTER_EGG) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    summonTNTSheep(bp, e.getItem());
                    e.setCancelled(true);
                }
            }
        }
        if (!bp.canBuild()) {
            if (!mode.getInstance().containsPlayer(bp) || mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME)
                e.setCancelled(true);
        }
    }

    private void spawnVillager(Location loc) {
        Villager v = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        v.setCustomName(BedWarsMessages.shopVillagerName);
        v.setCustomNameVisible(false);
        v.setProfession(Villager.Profession.FARMER);
        AttributeInstance attributes = ((CraftLivingEntity) v).getHandle()
                .getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        attributes.setValue(0);
    }

    private static BlockFace getDirection(Player p) {
        float yaw = p.getLocation().getYaw();
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            return BlockFace.SOUTH;
        } else if (yaw < 135) {
            return BlockFace.WEST;
        } else if (yaw < 225) {
            return BlockFace.NORTH;
        } else if (yaw < 315) {
            return BlockFace.EAST;
        }
        return BlockFace.NORTH;
    }

    private void buildProtectionWall(Player p) {

        Location l = p.getLocation();
        BlockFace face = getDirection(p);
        ItemStack is = p.getInventory().getItemInMainHand();
        is.setAmount(is.getAmount() - 1);
        switch (face) {
            case EAST:
                l = l.add(1, 0, -2);
                break;
            case NORTH:
                l = l.add(-2, 0, -1);
                break;
            case SOUTH:
                l = l.add(2, 0, 1);
                break;
            case WEST:
                l = l.add(-1, 0, 2);
                break;
            default:
                p.sendMessage(BedWarsMessages.invailedSide);
                return;
        }
        Block start = l.getBlock();

        for (int i = 0; i < 3; i++) {
            l = l.add(0, i, 0);
            for (int j = 0; j < 3; j++) {
                switch (face) {
                    case EAST:
                        l = l.add(0, 0, 1);
                        break;
                    case NORTH:
                        l = l.add(1, 0, 0);
                        break;
                    case SOUTH:
                        l = l.add(-1, 0, 0);
                        break;
                    case WEST:
                        l = l.add(0, 0, -1);
                        break;
                    default:
                        p.sendMessage(BedWarsMessages.invailedSide);
                        return;
                }
                if (l.getBlock().getType() == Material.AIR) {
                    l.getBlock().setType(Material.BRICK);
                    ((BedWarsHandler) mode.getHandler()).destroyableBlocks.add(l.getBlock());
                }
            }
            l = start.getLocation();
        }
    }

    private void warpHome(BiomiaPlayer bp, ItemStack is) {

        GameTeam team = bp.getTeam();
        if (team != null) {
            ((BedWars) mode).starts.put(bp, bp.getPlayer().getLocation());
            new BukkitRunnable() {
                final Location loc = team.getHome().clone();
                final Location location = bp.getPlayer().getLocation();
                int i = 50;

                @Override
                public void run() {
                    if (((BedWars) mode).starts.get(bp) != null) {
                        if (i == 0) {
                            bp.getPlayer().teleport(team.getHome());
                            is.setAmount(is.getAmount() - 1);
                        } else if (i <= 50) {
                            if (i % 10 == 0) {
                                ActionBar.sendActionBarTime("\u00A76" + i / 10 + " Sekunden", bp.getPlayer(), 0, 10, 0);
                                for (Player allPlayer : Bukkit.getOnlinePlayers())
                                    allPlayer.playSound(allPlayer.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                            }
                            if (i % 5 == 0) {
                                for (int degree = 0; degree < 360; degree += 2) {
                                    double radians1 = Math.toRadians(degree);
                                    double x1 = Math.cos(radians1);
                                    double z1 = Math.sin(radians1);
                                    loc.add(x1, 0, z1);
                                    new Particles(EnumParticle.FIREWORKS_SPARK, loc, false, 0, 0, 0, 0, 1).sendAll();
                                    loc.subtract(x1, 0, z1);
                                    double radians = Math.toRadians(degree);
                                    double x = Math.cos(radians);
                                    double z = Math.sin(radians);
                                    location.add(x, 0, z);
                                    new Particles(EnumParticle.FIREWORKS_SPARK, location, false, 0, 0, 0, 0, 1)
                                            .sendAll();
                                    location.subtract(x, 0, z);
                                }
                            }
                            location.add(0, 0.04, 0);
                            loc.add(0, 0.04, 0);
                            i--;
                            return;
                        }
                    }
                    cancel();
                }
            }.runTaskTimer(Main.getPlugin(), 0, 2);

        } else {
            bp.sendMessage(BedWarsMessages.notInATeam);
        }
    }

    private void spawn30secShop(Location l, ItemStack is) {
        ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        as.setCustomNameVisible(true);
        ((BedWars) mode).handlerMap.put(as.getUniqueId(), new ArrayList<>());
        is.setAmount(is.getAmount() - 1);
        new BukkitRunnable() {
            int i = 30;

            @Override
            public void run() {
                if (i == 0) {
                    ((BedWars) mode).handlerMap.get(as.getUniqueId()).forEach(HumanEntity::closeInventory);
                    as.remove();
                    cancel();
                }
                as.setCustomName(String.format(BedWarsMessages.thirtySecondShopName, Messages.COLOR_MAIN, i));
                i--;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    @SuppressWarnings("deprecation")
    private void buildRettungsplattform(Location l, ItemStack is) {

        Location loc = l.clone();
        is.setAmount(is.getAmount() - 1);
        loc.subtract(1, 1, 1);
        for (int x = 0; x < 3; x++) {
            loc.add(1, 0, 0);
            for (int y = 0; y < 3; y++) {
                loc.add(0, 0, 1);
                if (loc.getBlock().getType() == Material.AIR) {
                    loc.getBlock().setType(Material.STAINED_GLASS);
                    loc.getBlock().setData((byte) 4);
                    ((BedWarsHandler) mode.getHandler()).destroyableBlocks.add(loc.getBlock());
                }
            }
            loc.subtract(0, 0, 3);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                l.subtract(1, 1, 1);
                for (int x = 0; x < 3; x++) {
                    l.add(1, 0, 0);
                    for (int y = 0; y < 3; y++) {
                        l.add(0, 0, 1);
                        if (l.getBlock().getType() == Material.STAINED_GLASS) {
                            l.getBlock().setType(Material.AIR);
                        }
                    }
                    l.subtract(0, 0, 3);
                }
            }
        }.runTaskLater(Main.getPlugin(), 20 * 15);
    }

    private void summonTNTSheep(BiomiaPlayer bp, ItemStack is) {

        Player p = bp.getPlayer();
        GameTeam t = bp.getTeam();
        if (t != null) {
            is.setAmount(is.getAmount() - 1);
            Sheep sheep = (Sheep) p.getWorld().spawnEntity(p.getLocation().add(0, 1, 0), EntityType.SHEEP);
            sheep.setColor(DyeColor.valueOf(t.getTeamname()));
            ArrayList<Entity> entities = new ArrayList<>(sheep.getNearbyEntities(300, 100, 300));
            for (Entity entity : entities) {
                if (entities instanceof Player) {
                    Player target = (Player) entity;
                    GameTeam tempTeam = (Biomia.getBiomiaPlayer(target).getTeam());
                    if (tempTeam != null && !tempTeam.equals(t)) {
                        sheep.setTarget(target);
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                if (entity.getLocation().distance(target.getLocation()) <= 4 || i == 15) {
                                    summonTNT(sheep.getLocation().add(0, 1.5, 0), true);
                                    i++;
                                } else {
                                    summonTNT(sheep.getLocation().add(0, 1.5, 0), false);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(Main.getPlugin(), 0, 40);
                        return;
                    }
                }
            }
        }
    }

    private void summonTNT(Location l, boolean remove) {
        new BukkitRunnable() {
            TNTPrimed tnt;
            int i = 0;

            @Override
            public void run() {
                if (i == 1 && remove) {
                    tnt.remove();
                    cancel();
                } else {
                    tnt = (TNTPrimed) l.getWorld().spawnEntity(l, EntityType.PRIMED_TNT);
                    tnt.setFuseTicks(60);
                    i++;
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 40);
    }
}