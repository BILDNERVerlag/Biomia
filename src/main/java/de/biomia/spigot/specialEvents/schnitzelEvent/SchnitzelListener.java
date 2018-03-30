package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.listeners.servers.BiomiaListener;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

public class SchnitzelListener extends BiomiaListener {

    private static final ItemStack shovel = ItemCreator.itemCreate(Material.IRON_SPADE, "ßb‹berlebens Schaufel");
    private static final ItemStack helmet = ItemCreator.itemCreate(Material.CHAINMAIL_HELMET, "ßbArbeits Hose");
    private static final ItemStack chestplate = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE, "ßcMonster Schutzjacke");
    private static final ItemStack leggings = ItemCreator.itemCreate(Material.LEATHER_HELMET, "ßbMinen Helm");
    private static final ItemStack boots = ItemCreator.itemCreate(Material.GOLD_BOOTS, "ßcBergmanns Schuhe");

    private static final ItemStack backpack = ItemCreator.itemCreate(Material.CHEST, "ßcRucksack");
    private static final ItemStack bread = ItemCreator.itemCreate(Material.BREAD, "ßcBauern Brot");

    static {

        ItemMeta meta = shovel.getItemMeta();

        meta.setUnbreakable(true);
        shovel.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        shovel.setItemMeta(meta);

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
            e.getItem().setPickupDelay(0);
            ItemStack is = e.getItem().getItemStack();
            if (is.getType() == Material.PAPER) {
                if (is.getItemMeta().getDisplayName().contains("Schnitzel")) {
                    SchnitzelEvent.getSchnitzel(is.getItemMeta().getDisplayName()).pickUp(bp);
                    e.setCancelled(true);
                }
            } else if (is.getType() == Material.BOOK) {
                SecretBook book = SchnitzelEvent.getSecretBook(is.getItemMeta().getDisplayName());
                if (book != null)
                    book.pickUp(bp);
            }
        }
    }

    @EventHandler
    public void onJoin_(PlayerJoinEvent e) {
        e.getPlayer().getInventory().clear();

        e.getPlayer().getInventory().setItem(8, backpack);
        e.getPlayer().getInventory().setItem(7, SchnitzelEvent.getInfoBook());
        e.getPlayer().getInventory().setItem(1, bread);

        e.getPlayer().getInventory().setItem(0, shovel);
        e.getPlayer().getInventory().setHelmet(helmet);
        e.getPlayer().getInventory().setChestplate(chestplate);
        e.getPlayer().getInventory().setBoots(boots);
        e.getPlayer().getInventory().setLeggings(leggings);
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
            if (e.getFoodLevel() < 20 && !ItemConditions.hasItemInInventory(Biomia.getQuestPlayer(p), Material.BREAD, 1)) {
                p.getInventory().addItem(bread);
            }
        }
    }

    @EventHandler
    public void onInteract_(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            if (e.getItem().isSimilar(backpack))
                SchnitzelEvent.openBackpack(Biomia.getBiomiaPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onClick_(InventoryClickEvent e) {
        if (e.getClickedInventory().getName().equals(SchnitzelEvent.getBackpackName()))
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
    public void onMove(PlayerMoveEvent e) {

        int multiplicator = 1;
        int nowInSeconds = (int) (System.currentTimeMillis() / 1000);
        Random random = new Random();

        for (Spawner spawner : Spawner.getMonsterSpawner()) {

            Location loc = spawner.getLocation();

            if (nowInSeconds - spawner.getLastSpawnedTime() > random.nextInt(20) + 30) {
                int distance = spawner.getDistance();
                if (e.getTo().distance(loc) <= distance) {
                    for (Player allPlayer : Bukkit.getOnlinePlayers()) {

                        if (e.getPlayer().equals(allPlayer))
                            continue;

                        if (allPlayer.getLocation().distance(loc) <= distance) {
                            multiplicator += 1;
                        }
                    }

                    ArrayList<Location> possibleSpawnLocations = spawner.getPossibleSpawnLocations();

                    int spawned = 0;
                    final int targetSpawns = spawner.getMonsterPerPlayer() * multiplicator;

                    for (Location possibleLocs : possibleSpawnLocations) {
                        if (targetSpawns >= spawned)
                            return;
                        possibleLocs.getWorld().spawnEntity(possibleLocs, spawner.getType());
                        spawned++;
                    }

                    int loops = 0;
                    a:
                    while (targetSpawns > spawned) {
                        loops++;
                        if (loops > targetSpawns * 4) {
                            Bukkit.getLogger().log(Level.SEVERE, "Not enough places to Spawn found! Stopped spawning to not overload the CPU!");
                            return;
                        }
                        int x = random.nextInt(7) - 3;
                        int z = random.nextInt(7) - 3;

                        Location location = loc.clone();
                        int temp = 0;
                        while (location.clone().add(0, 1, 0).getBlock().getType() != Material.AIR) {
                            location = location.add(x, 1, z);
                            temp++;
                            if (temp > 4) {
                                continue a;
                            }
                        }
                        if (!possibleSpawnLocations.contains(location))
                            possibleSpawnLocations.add(location);

                        location.getWorld().spawnEntity(location, spawner.getType());
                        spawned++;
                    }

                    spawner.setLastSpawnedTime();
                }
            }

        }
    }
}