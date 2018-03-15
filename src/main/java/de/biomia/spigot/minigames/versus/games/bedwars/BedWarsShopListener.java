package de.biomia.spigot.minigames.versus.games.bedwars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.general.shop.Shop;
import de.biomia.spigot.minigames.general.shop.ShopGroup;
import de.biomia.spigot.minigames.general.shop.ShopItem;
import de.biomia.spigot.minigames.general.ColorType;
import de.biomia.spigot.minigames.general.shop.ItemType;
import de.biomia.spigot.tools.ItemCreator;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class BedWarsShopListener implements Listener {

    private final VersusBedWars versusBedWars;

    public BedWarsShopListener(VersusBedWars versusBedWars) {
        this.versusBedWars = versusBedWars;
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (versusBedWars.getInstance().containsPlayer(bp))
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
                            if (iStack.hasItemMeta() && iStack.getItemMeta().hasDisplayName() && iStack.getItemMeta().getDisplayName().equals(BedWarsItemNames.back)) {
                                p.openInventory(Shop.getInventory());
                                return;
                            } else {
                                ShopItem shopItem = group.getShopItem(iStack);

                                ItemStack returnItem = iStack.clone();

                                if (shopItem.isColorble()) {
                                    VersusBedWarsTeam team = (VersusBedWarsTeam) bp.getTeam();
                                    if (shopItem.getType() == ColorType.LEATHER) {
                                        LeatherArmorMeta meta = (LeatherArmorMeta) returnItem.getItemMeta();
                                        switch (team.getColor()) {
                                        case RED:
                                            meta.setColor(Color.RED);
                                            break;
                                        case BLUE:
                                            meta.setColor(Color.BLUE);
                                            break;
                                        default:
                                            break;
                                        }
                                        returnItem.setItemMeta(meta);
                                    } else {
                                        returnItem.setDurability(team.getColordata());
                                    }
                                }
                                if (e.getClick().isShiftClick()) {
                                    int i = iStack.getMaxStackSize() / iStack.getAmount();
                                    boolean first = true;
                                    for (int j = 0; j < i; j++) {
                                        if (shopItem.take(p)) {
                                            p.getInventory().addItem(returnItem);
                                            first = false;
                                        } else if (first) {
                                            String name = ItemType.getName(shopItem.getItemType());
                                            p.sendMessage(BedWarsMessages.notEnoughItemsToPay.replace("%n", name));
                                            return;
                                        } else {
                                            return;
                                        }
                                    }
                                } else if (shopItem.take(p)) {
                                    p.getInventory().addItem(returnItem);
                                } else {
                                    String name = ItemType.getName(shopItem.getItemType());
                                    p.sendMessage(BedWarsMessages.notEnoughItemsToPay.replace("%n", name));
                                    return;

                                }
                            }
                        }
                    }
                }
            }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Villager) {
            if (e.getEntity().getCustomName().equals("Shop")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void Interact(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof Villager && e.getRightClicked().getCustomName().equals("Shop")) {
            e.setCancelled(true);
            p.openInventory(Shop.getInventory());
        }
    }

    @EventHandler
    public void craftItem(PrepareItemCraftEvent e) {
        if (!e.isRepair())
            e.getInventory().setResult(ItemCreator.itemCreate(Material.AIR));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
            if (e.getItem().getItemMeta().getDisplayName().equals(BedWarsItemNames.villagerSpawner)) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    spawnVillager(e.getClickedBlock().getLocation().add(0.5, 1, 0.5));
                    e.setCancelled(true);
                }
            }
        }
    }

    private void spawnVillager(Location loc) {
        Villager v = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        v.setCustomName("Shop");
        v.setCustomNameVisible(false);
        v.setProfession(Villager.Profession.FARMER);
        AttributeInstance attributes = ((CraftLivingEntity) v).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        attributes.setValue(0);
    }
}