package de.biomia.bw.listeners;

import de.biomia.bw.gamestates.GameState;
import de.biomia.bw.BedWars;
import de.biomia.bw.messages.ItemNames;
import de.biomia.bw.messages.Messages;
import de.biomia.bw.var.*;
import de.biomia.api.Biomia;
import de.biomia.api.Teams.Team;
import de.biomia.api.main.Main;
import de.biomia.api.msg.ActionBar;
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

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                Team t = Variables.getTeamByTeamChests(e.getClickedBlock());
                if (t != null) {
                    Inventory inv = Variables.teamChests.computeIfAbsent(t, t1 -> Bukkit.createInventory(null, 27,
                            "\u00A78Team Kiste: " + t1.getColorcode() + Biomia.TeamManager().translate(t1.getTeamname())));
                    e.setCancelled(true);
                    p.openInventory(inv);
                }
            }
        }
        if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
            String displayname = e.getItem().getItemMeta().getDisplayName();

            if (displayname.equals(ItemNames.teamWaehlerItem))
                p.openInventory(Variables.teamJoiner);
            else if (displayname.equals(ItemNames.startItem)) {
                if (Variables.countDown.getCountdown() > 5)
                    Variables.countDown.setCountdown(5);
            } else if (displayname.equals(ItemNames.bedSetter)) {
                Block blockFoot = p.getLocation().getBlock();
                Block blockHead = p.getTargetBlock((Set<Material>)null, 100);

                if (blockFoot.getType() == Material.BED_BLOCK && blockHead.getType() == Material.BED_BLOCK) {

                    Config.addBedsLocations(blockFoot.getLocation(), blockHead.getLocation(),
                            Biomia.TeamManager().DataToTeam(e.getItem().getData().getData()));
                    Bukkit.broadcastMessage("Bett hinzugef\u00fc\u00A7gt!");

                } else {
                    p.sendMessage(Messages.blocksMustBeBeds);
                }

            } else if (displayname.equals(ItemNames.warper)) {
                if (Teleport.getStartLocation(p) == null) {
                    warpHome(p, e.getItem());
                }
            } else if (displayname.equals(ItemNames.wand)) {
                e.setCancelled(true);
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                    buildProtectionWall(p);
            } else if (displayname.equals(ItemNames.bronzeSetter)) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    addSpawner(e.getClickedBlock().getLocation(), e.getBlockFace(), p, ItemType.BRONZE);
                    p.sendMessage(Messages.bronzeSpawnAdded);
                }
            } else if (displayname.equals(ItemNames.ironSetter)) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    addSpawner(e.getClickedBlock().getLocation(), e.getBlockFace(), p, ItemType.IRON);
                    p.sendMessage(Messages.ironSpawnAdded);
                }
            } else if (displayname.equals(ItemNames.goldSetter)) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    addSpawner(e.getClickedBlock().getLocation(), e.getBlockFace(), p, ItemType.GOLD);
                    p.sendMessage(Messages.goldSpawnAdded);
                }
            } else if (displayname.equals(ItemNames.villagerSpawner)) {
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
                    summonTNTSheep(p, e.getItem());
                    e.setCancelled(true);
                }
            }
        }

        if (!Biomia.getBiomiaPlayer(p).canBuild()) {
            if (!Variables.livingPlayer.contains(p) || BedWars.gameState != GameState.INGAME)
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
                p.sendMessage(Messages.invailedSide);
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
                        p.sendMessage(Messages.invailedSide);
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
                p.sendMessage(Messages.invailedSide);
                return;
        }

        Config.addSpawnerLocations(l, itemType);
    }

    private void warpHome(Player p, ItemStack is) {

        if (Biomia.TeamManager().isPlayerInAnyTeam(p)) {
            Team team = Biomia.TeamManager().getTeam(p);
            Teleport.teleportBackHome(p);

            new BukkitRunnable() {
                final Location loc = Variables.teamSpawns.get(team).clone();
                final Location location = p.getLocation();
                int i = 50;

                @Override
                public void run() {
                    if (Teleport.getStartLocation(p) != null) {
                        if (i == 0) {
                            p.teleport(Variables.teamSpawns.get(team));
                            is.setAmount(is.getAmount() - 1);
                        } else if (i <= 50) {
                            if (i % 10 == 0) {
                                ActionBar.sendActionBarTime("\u00A76" + i / 10 + " Sekunden", p, 0, 10, 0);
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
            p.sendMessage("\u00A7cDu bist in keinem Team!");
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

    private void summonTNTSheep(Player p, ItemStack is) {

        Team t = Biomia.TeamManager().getTeam(p);
        if (t != null) {
            is.setAmount(is.getAmount() - 1);
            Sheep sheep = (Sheep) p.getWorld().spawnEntity(p.getLocation().add(0, 1, 0), EntityType.SHEEP);
            sheep.setColor(DyeColor.valueOf(t.getTeamname()));
            ArrayList<Entity> entities = new ArrayList<>(sheep.getNearbyEntities(300, 100, 300));
            for (Entity entity : entities) {
                if (entities instanceof Player) {
                    Player target = (Player) entity;
                    if (Biomia.TeamManager().getTeam(p) != null && !Biomia.TeamManager().getTeam(p).equals(t)) {
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
