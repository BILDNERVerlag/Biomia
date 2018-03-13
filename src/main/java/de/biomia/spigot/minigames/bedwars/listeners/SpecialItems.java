package de.biomia.spigot.minigames.bedwars.listeners;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.messages.manager.ActionBar;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.minigames.bedwars.var.Teleport;
import de.biomia.spigot.minigames.bedwars.var.Variables;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.tools.Particles;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Set;

public class SpecialItems implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                GameTeam t = Variables.getTeamByTeamChests(e.getClickedBlock());
                if (t != null) {
                    Inventory inv = Variables.teamChests.computeIfAbsent(t, t1 -> Bukkit.createInventory(null, 27, "\u00A78Team Kiste: " + t1.getColorcode() + t1.getColor().translate()));
                    e.setCancelled(true);
                    p.openInventory(inv);
                }
            }
        }
        if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
            String displayname = e.getItem().getItemMeta().getDisplayName();

            if (displayname.equals(BedWarsItemNames.teamWaehlerItem))
                p.openInventory(BedWars.getBedWars().getTeamSwitcher());
            else if (displayname.equals(BedWarsItemNames.startItem)) {
                if (BedWars.getBedWars().getStateManager().getLobbyState().getCountDown() > 5)
                    BedWars.getBedWars().getStateManager().getLobbyState().setCountDown(5);
            } else if (displayname.equals(BedWarsItemNames.bedSetter)) {
                Block blockFoot = p.getLocation().getBlock();
                Block blockHead = p.getTargetBlock((Set<Material>) null, 100);

                if (blockFoot.getType() == Material.BED_BLOCK && blockHead.getType() == Material.BED_BLOCK) {
                    new BedWarsConfig().addBedsLocations(blockFoot.getLocation(), blockHead.getLocation(), TeamColor.getColorFromData(e.getItem().getData().getData()));
                    Bukkit.broadcastMessage("Bett hinzugef\u00fcgt!");
                } else {
                    p.sendMessage(BedWarsMessages.blocksMustBeBeds);
                }

            } else if (displayname.equals(BedWarsItemNames.warper)) {
                if (Teleport.getStartLocation(bp) == null) {
                    warpHome(bp, e.getItem());
                }
            } else if (displayname.equals(BedWarsItemNames.wand)) {
                e.setCancelled(true);
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                    buildProtectionWall(p);
            } else if (displayname.equals(BedWarsItemNames.bronzeSetter)) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    addSpawner(e.getClickedBlock().getLocation(), e.getBlockFace(), p, ItemType.BRONZE);
                    p.sendMessage(BedWarsMessages.bronzeSpawnAdded);
                }
            } else if (displayname.equals(BedWarsItemNames.ironSetter)) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    addSpawner(e.getClickedBlock().getLocation(), e.getBlockFace(), p, ItemType.IRON);
                    p.sendMessage(BedWarsMessages.ironSpawnAdded);
                }
            } else if (displayname.equals(BedWarsItemNames.goldSetter)) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    addSpawner(e.getClickedBlock().getLocation(), e.getBlockFace(), p, ItemType.GOLD);
                    p.sendMessage(BedWarsMessages.goldSpawnAdded);
                }
            } else if (displayname.equals(BedWarsItemNames.villagerSpawner)) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    spawnVillager(e.getClickedBlock().getLocation().add(0.5, 1, 0.5));
                    e.setCancelled(true);
                }
            } else if (displayname.equals("\u00A7c30 Sekunden Shop")) {
                e.setCancelled(true);
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    spawn30secShop(e.getClickedBlock().getLocation().add(0.5, 1, 0.5), e.getItem());
                    e.setCancelled(true);
                }
            } else if (displayname.equals("\u00A7cRettungs Plattform")) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    buildRettungsplattform(e.getPlayer().getLocation(), e.getItem());
                    e.setCancelled(true);
                }
            } else if (e.getItem().getType() == Material.MONSTER_EGG) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    summonTNTSheep(bp, e.getItem());
                    e.setCancelled(true);
                }
            }
        }

        if (!bp.canBuild()) {
            if (!BedWars.getBedWars().getInstance().containsPlayer(bp) || BedWars.getBedWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME)
                e.setCancelled(true);
        }
    }

    private void spawnVillager(Location loc) {
        Villager v = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        v.setCustomName("Shop");
        v.setCustomNameVisible(false);
        v.setProfession(Profession.FARMER);
        AttributeInstance attributes = ((CraftLivingEntity) v).getHandle()
                .getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        attributes.setValue(0);
    }

    private void buildProtectionWall(Player p) {

        Location l = p.getLocation();
        BlockFace face = Variables.getDirection(p);
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
                    Variables.destroyableBlocks.add(l.getBlock());
                }
            }
            l = start.getLocation();
        }
    }

    private void addSpawner(Location l, BlockFace blockFace, Player p, ItemType itemType) {

        switch (blockFace) {
        case UP:
            l = l.add(0, 1, 0);
            break;
        case DOWN:
            l = l.add(0, -1, 0);
            break;
        case EAST:
            l = l.add(1, 0, 0);
            break;
        case NORTH:
            l = l.add(0, 0, -1);
            break;
        case SOUTH:
            l = l.add(0, 0, 1);
            break;
        case WEST:
            l = l.add(-1, 0, 0);
            break;
        default:
            p.sendMessage(BedWarsMessages.invailedSide);
            return;
        }

        new BedWarsConfig().addSpawnerLocations(l, itemType);
    }

    private void warpHome(BiomiaPlayer bp, ItemStack is) {

        GameTeam team = BedWars.getBedWars().getTeam(bp);
        if (team != null) {
            Teleport.teleportBackHome(bp);
            new BukkitRunnable() {
                final Location loc = team.getHome().clone();
                final Location location = bp.getPlayer().getLocation();
                int i = 50;

                @Override
                public void run() {
                    if (Teleport.getStartLocation(bp) != null) {
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
            bp.sendMessage("\u00A7cDu bist in keinem Team!");
        }
    }

    private void spawn30secShop(Location l, ItemStack is) {
        ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        as.setCustomNameVisible(true);
        Variables.handlerMap.put(as.getUniqueId(), new ArrayList<>());
        is.setAmount(is.getAmount() - 1);
        new BukkitRunnable() {
            int i = 30;

            @Override
            public void run() {
                if (i == 0) {
                    Variables.handlerMap.get(as.getUniqueId()).forEach(HumanEntity::closeInventory);
                    as.remove();
                    cancel();
                }
                as.setCustomName("\u00A7c" + i + " Sekunden");
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
                    Variables.destroyableBlocks.add(loc.getBlock());
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
        GameTeam t = BedWars.getBedWars().getTeam(bp);
        if (t != null) {
            is.setAmount(is.getAmount() - 1);
            Sheep sheep = (Sheep) p.getWorld().spawnEntity(p.getLocation().add(0, 1, 0), EntityType.SHEEP);
            sheep.setColor(DyeColor.valueOf(t.getTeamname()));
            ArrayList<Entity> entities = new ArrayList<>(sheep.getNearbyEntities(300, 100, 300));
            for (Entity entity : entities) {
                if (entities instanceof Player) {
                    Player target = (Player) entity;
                    GameTeam tempTeam = BedWars.getBedWars().getTeam(Biomia.getBiomiaPlayer(target));
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
