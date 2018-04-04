package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.achievements.BiomiaStat;
import de.biomia.spigot.listeners.servers.BiomiaListener;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

public class SchnitzelListener extends BiomiaListener {

    private static final ItemStack shovel = ItemCreator.itemCreate(Material.IRON_SPADE, "§bÜberlebens-Schaufel");
    private static final ItemStack helmet = ItemCreator.itemCreate(Material.CHAINMAIL_HELMET, "§bMinenhelm");
    private static final ItemStack chestplate = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE, "§cMonsterschutzjacke");
    private static final ItemStack leggings = ItemCreator.itemCreate(Material.LEATHER_LEGGINGS, "§bArbeitshose");
    private static final ItemStack boots = ItemCreator.itemCreate(Material.GOLD_BOOTS, "§cBergmannsschuhe");

    private static final ItemStack backpack = ItemCreator.itemCreate(Material.CHEST, "§cRucksack");
    private static final ItemStack bread = ItemCreator.itemCreate(Material.BREAD, "§cBauern Brot");

    static {


        ItemMeta meta = shovel.getItemMeta();
        meta.setUnbreakable(true);
        shovel.setItemMeta(meta);
        shovel.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);

        meta = chestplate.getItemMeta();
        meta.setUnbreakable(true);
        chestplate.setItemMeta(meta);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        meta = leggings.getItemMeta();
        meta.setUnbreakable(true);
        leggings.setItemMeta(meta);
        leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        meta = helmet.getItemMeta();
        meta.setUnbreakable(true);
        helmet.setItemMeta(meta);
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        meta = boots.getItemMeta();
        meta.setUnbreakable(true);
        boots.setItemMeta(meta);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);

    }


    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getEntity());

            if (!bp.canBuild())
                e.setCancelled(true);

            ItemStack is = e.getItem().getItemStack();
            if (is.getType() == Material.PAPER) {
                if (is.getItemMeta().getDisplayName().contains("Schnitzel")) {
                    e.setCancelled(true);
                    SchnitzelEvent.getSchnitzel(is.getItemMeta().getDisplayName()).pickUp(bp);
                }
            } else if (is.getType() == Material.BOOK) {
                SecretBook book = SchnitzelEvent.getSecretBook(is.getItemMeta().getDisplayName());
                if (book != null) {
                    e.setCancelled(true);
                    book.pickUp(bp);
                }
            }
        }
    }

    @EventHandler
    public void onJoin_(PlayerJoinEvent e) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

        Checkpoint.startSave(bp);

        e.getPlayer().getInventory().clear();

        e.getPlayer().getInventory().setItem(8, backpack);
        e.getPlayer().getInventory().setItem(7, SchnitzelEvent.getInfoBook());

        e.getPlayer().getInventory().setItem(0, shovel);
        e.getPlayer().getInventory().setHelmet(helmet);
        e.getPlayer().getInventory().setChestplate(chestplate);
        e.getPlayer().getInventory().setBoots(boots);
        e.getPlayer().getInventory().setLeggings(leggings);

        MonsterPunkte mp = SchnitzelEvent.mobsKilled.computeIfAbsent(bp.getName(), mob -> new MonsterPunkte(bp, BiomiaStat.SchnitzelMonsterKilled.get(bp.getBiomiaPlayerID(), null)));
        e.getPlayer().setLevel(mp.getPoints());
        Scoreboards.setTabList(e.getPlayer(), true, false);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!Biomia.getBiomiaPlayer(e.getPlayer()).canBuild())
            e.setCancelled(true);
    }

    @EventHandler
    public void onDamagePlayer(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player && e.getDamager() instanceof Player) {
            if (!Biomia.getBiomiaPlayer(((Player) entity)).canBuild())
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onGetHunger(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            int fehlend = (int) (Math.ceil(((double) (20 - e.getFoodLevel()) / 4)) + 1);
            while (!ItemConditions.hasItemInInventory(Biomia.getQuestPlayer(p), Material.BREAD, fehlend))
                p.getInventory().addItem(bread);
        }
    }

    @EventHandler
    public void onInteract_(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType() == Material.DISPENSER)
            e.setCancelled(true);
        else if (e.getItem() != null) {
            if (e.getItem().isSimilar(backpack))
                SchnitzelEvent.openBackpack(Biomia.getBiomiaPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onClick_(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getName().equals(SchnitzelEvent.getBackpackName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        ItemStack is = e.getEntity().getItemStack();

        if (is.getType() == Material.PAPER) {
            if (is.getItemMeta().getDisplayName().contains("Schnitzel")) {
                e.setCancelled(true);
            }
        } else if (is.getType() == Material.BOOK) {
            SecretBook book = SchnitzelEvent.getSecretBook(is.getItemMeta().getDisplayName());
            if (book != null)
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Location loc = Checkpoint.getLastSavedLocation(Biomia.getBiomiaPlayer(e.getPlayer()));
        if (loc == null) {
            e.getPlayer().sendMessage("§cRespawn am Spawn, da du nur alle §b30 §cSekunden zu deinem Checkpoint zurückkehren kannst§7!");
            loc = SchnitzelEvent.getSpawn();
        }
        e.setRespawnLocation(loc);
    }

    @EventHandler
    public void mobDeathEvent(EntityDeathEvent e) {
        e.setDroppedExp(0);
        Player p = e.getEntity().getKiller();
        if (p != null) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
            BiomiaStat.SchnitzelMonsterKilled.increment(bp.getBiomiaPlayerID(), 1, null);
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            SchnitzelEvent.mobsKilled.get(Biomia.getBiomiaPlayer(p).getName()).addPoint(e.getEntityType());
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {

        if (e.getEntityType() == EntityType.PLAYER) {
            Player p = (Player) e.getEntity();
            if (e.getFinalDamage() >= p.getHealth()) {
                SchnitzelEvent.mobsKilled.get(Biomia.getBiomiaPlayer(p).getName()).removePoints();
            }
        }
    }

    @EventHandler
    public void onItemDrop(ItemSpawnEvent e) {
        ItemStack is = e.getEntity().getItemStack();
        if (is.getType() != Material.PAPER && is.getType() != Material.BOOK) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplosion(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() == EntityType.DROPPED_ITEM) e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        int multiplicator = 0;
        int nowInSeconds = (int) (System.currentTimeMillis() / 1000);
        Random random = new Random();

        if ((e.getTo().getX() > 425 || e.getTo().getX() < 40) &&
                e.getTo().getY() > 60 &&
                (e.getTo().getZ() > 805 || e.getTo().getZ() < 590)) e.getPlayer().teleport(SchnitzelEvent.getSpawn());

        for (Spawner spawner : Spawner.getMonsterSpawner()) {

            Location loc = spawner.getLocation();

            int distance = spawner.getDistance();
            if (e.getFrom().distance(loc) <= distance) {

                if (nowInSeconds - spawner.getLastSpawnedTime() > 30) {

                    for (Player allPlayer : Bukkit.getOnlinePlayers()) {
                        if (allPlayer.getLocation().distance(loc) <= distance + 2) {
                            multiplicator += 1;
                        }
                    }

                    ArrayList<Location> possibleSpawnLocations = spawner.getPossibleSpawnLocations();

                    int spawned = 0;
                    final int targetSpawns = spawner.getMonsterPerPlayer() * multiplicator;

                    for (Location possibleLocs : possibleSpawnLocations) {

                        possibleLocs.getWorld().spawnEntity(possibleLocs, spawner.getType());
                        spawned++;
                        if (targetSpawns == spawned)
                            break;
                    }

                    int loops = 0;
                    a:
                    while (targetSpawns > spawned) {
                        loops++;
                        if (loops > targetSpawns * 4) {
                            Bukkit.getLogger().log(Level.SEVERE, "Not enough places to Spawn found! Stopped spawning to not overload the CPU!");
                            return;
                        }
                        int temp = 0;
                        Location location;
                        do {
                            location = loc.clone().add(random.nextDouble() * 7 - 3, 0, random.nextDouble() * 7 - 3);
                            temp++;
                            if (temp > 4)
                                continue a;
                        } while (location.clone().add(0, 1, 0).getBlock().getType() != Material.AIR);
                        if (!possibleSpawnLocations.contains(location))
                            possibleSpawnLocations.add(location);

                        location.getWorld().spawnEntity(location, spawner.getType());
                        spawned++;
                    }
                    spawner.setLastSpawnedTime(nowInSeconds);
                }

            }

        }
    }
}