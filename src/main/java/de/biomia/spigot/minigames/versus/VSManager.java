package de.biomia.spigot.minigames.versus;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.messages.SkyWarsMessages;
import de.biomia.spigot.messages.manager.ActionBar;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.minigames.general.kits.Kit;
import de.biomia.spigot.minigames.general.kits.KitManager;
import de.biomia.spigot.minigames.versus.settings.VSGroup;
import de.biomia.spigot.minigames.versus.settings.VSRequest;
import de.biomia.spigot.minigames.versus.settings.VSSettingItem;
import de.biomia.spigot.minigames.versus.settings.VSSettings;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.WorldCopy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class VSManager implements Listener {

    private final HashMap<BiomiaPlayer, VSSettings> settings = new HashMap<>();
    private final HashMap<GameInstance, VSRequest> requests = new HashMap<>();
    private final ItemStack toChallangeItem = ItemCreator.itemCreate(Material.DIAMOND_SWORD, "\u00A7cHerausforderer");
    private final ItemStack settingItem = ItemCreator.itemCreate(Material.REDSTONE, "\u00A7cEinstellungen");
    private VSGroup main;

    VSManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
        init();
    }

    private void init() {

        main = new VSGroup(null, "\u00A7cEinstellungen", null, 0, false, null);

        ItemStack bedwarsSettingItem = ItemCreator.itemCreate(Material.BED, "\u00A7cBedWars");
        ItemStack skywarsSettingItem = ItemCreator.itemCreate(Material.GRASS, "\u00A7aSkyWars");
//        ItemStack kitPvPSettingItem = ItemCreator.itemCreate(Material.IRON_SWORD, "\u00A7dKitPvP");

        VSGroup bedwars = main.registerDeaktivatableGroup(GameType.BED_WARS_VS, bedwarsSettingItem, "\u00A7cBedWars", 6, 0, true);
        VSGroup bedwarsMaps = bedwars.registerGroup(GameType.BED_WARS_VS, ItemCreator.itemCreate(Material.PAPER, "\u00A7aMaps"), "\u00A7aMaps", 0);
        bedwarsMaps.registerSetting(new VSSettingItem(ItemCreator.itemCreate(Material.FLOWER_POT_ITEM), 100, 0, true, bedwarsMaps, "Mayaria"));

        VSGroup skywars = main.registerDeaktivatableGroup(GameType.SKY_WARS_VS, skywarsSettingItem, "\u00A7aSkyWars", 2, 0, true);
        VSGroup skywarsMaps = skywars.registerGroup(GameType.SKY_WARS_VS, ItemCreator.itemCreate(Material.PAPER, "\u00A7aMaps"), "\u00A7aMaps", 0);
        skywarsMaps.registerSetting(new VSSettingItem(ItemCreator.itemCreate(Material.SMOOTH_BRICK, (byte) 2), 100, 0, true, skywarsMaps, "Ruins"));
        skywars.registerGroup(GameType.SKY_WARS_VS, SkyWarsItemNames.kitItem, SkyWarsItemNames.kitItemName, 1);

//        VSGroup kitpvp = main.registerDeaktivatableGroup(GameType.KIT_PVP_VS, kitPvPSettingItem, "\u00A7dKitPvP", 4, 0, true);
//        VSGroup kitpvpsMaps = kitpvp.registerGroup(GameType.KIT_PVP_VS, ItemCreator.itemCreate(Material.PAPER, "\u00A7aMaps"), "\u00A7aMaps", 0);
//        kitpvpsMaps.registerSetting(new VSSettingItem(ItemCreator.itemCreate(Material.FLOWER_POT_ITEM), 100, 0, true, kitpvpsMaps, "Map1"));
    }

    public void moveToLobby(Player p, boolean onSpawn) {
        Dead.respawn(p);
        if (!onSpawn)
            p.teleport(de.biomia.spigot.minigames.GameMode.getSpawn(true));
        p.setGameMode(GameMode.SURVIVAL);
        setInventory(p);
        p.setHealth(20);
        p.setFoodLevel(20);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setGetDamage(false);
        bp.setDamageEntitys(false);
        Scoreboards.setTabList(p, true, false);
    }

    public void setInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setItem(2, settingItem);
        p.getInventory().setItem(6, toChallangeItem);
    }

    private void openMainInventory(BiomiaPlayer bp) {
        main.getInventory(bp).openInventory();
    }

    private void addToQueue(BiomiaPlayer bp) {
        WarteschlangenManager.add(bp);
        ActionBar.sendActionBar("\u00A7cDu bist der Warteschlange beigetretten!", bp.getPlayer());
    }

    private void removeFromQueue(BiomiaPlayer bp) {
        WarteschlangenManager.remove(bp);
        ActionBar.sendActionBar("\u00A7cDu hast die Warteschlange verlassen!", bp.getPlayer());
    }

    public VSSettings getSettings(BiomiaPlayer bp) {
        return settings.computeIfAbsent(bp, b -> new VSSettings(bp));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        ItemStack is = e.getItem();

        if (is != null && is.getType() != Material.AIR) {
            if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                switch (is.getItemMeta().getDisplayName()) {
                    case "\u00A7cEinstellungen":
                        openMainInventory(bp);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer hitter = Biomia.getBiomiaPlayer(p);
        ItemStack is = p.getInventory().getItemInMainHand();

        if (is != null && is.getType() != Material.AIR) {
            if (is.hasItemMeta()) {
                if (is.getItemMeta().getDisplayName().equals("\u00A7cHerausforderer")) {
                    if (e.getRightClicked() instanceof Player) {
                        Player p1 = (Player) e.getRightClicked();
                        BiomiaPlayer hittedPlayer = Biomia.getBiomiaPlayer(p1);
                        if (VSRequest.hasRequestSended(hittedPlayer, hitter)) {
                            VSRequest request = VSRequest.getRequest(hittedPlayer);
                            if (request != null)
                                request.decline();
                            else
                                p.sendMessage("\u00A7cDu hast keine Herausforderung von \u00A7d" + p1.getName() + "\u00A7c die du ablehnen kannst!");
                            e.setCancelled(true);
                        }
                    } else if (e.getRightClicked() instanceof Villager) {
                        removeFromQueue(hitter);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            BiomiaPlayer hitter = Biomia.getBiomiaPlayer(p);
            ItemStack is = p.getInventory().getItemInMainHand();

            if (is != null && is.getType() != Material.AIR) {
                if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                    if (is.getItemMeta().getDisplayName().equals("\u00A7cHerausforderer")) {
                        if (e.getEntity() instanceof Player) {
                            Player p1 = (Player) e.getEntity();
                            BiomiaPlayer hittedPlayer = Biomia.getBiomiaPlayer(p1);

                            if (VSRequest.hasRequestSended(hitter, hittedPlayer)) {
                                hitter.getPlayer().sendMessage("\u00A7cDu hast dem Spieler bereits eine Anfrage geschickt!");
                            } else if (VSRequest.hasRequestSended(hittedPlayer, hitter)) {
                                VSRequest request = VSRequest.getRequest(hittedPlayer);
                                if (request != null) {
                                    request.accept();
                                    e.setCancelled(true);
                                }
                            } else {
                                VSRequest request = VSRequest.getRequest(hitter);
                                if (request != null)
                                    request.remove();
                                new VSRequest(hitter, hittedPlayer).sendRequest();
                            }
                        } else if (e.getEntity() instanceof Villager) {
                            addToQueue(hitter);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                    String name = e.getCurrentItem().getItemMeta().getDisplayName();

                    Kit kit = KitManager.getStandardKit();
                    String invName = e.getInventory().getName();
                    for (Kit allKits : KitManager.allKits.values()) {
                        if (invName.equals("Kits")) {
                            if (name.equals(allKits.getDisplayName())) {
                                p.openInventory(allKits.getSetupInv(p));
                                return;
                            }
                        }
                        if (invName.contains(allKits.getName())) {
                            kit = allKits;
                            break;
                        }
                    }
                    switch (name) {
                        case SkyWarsItemNames.purchaseKit:
                            p.closeInventory();
                            KitManager kitManager = KitManager.getManager(bp);
                            if (kitManager.buy(kit)) {
                                kitManager.selectSkyWarsKit(kit);
                                p.sendMessage(SkyWarsMessages.youChoseKit.replace("%k", kit.getName()));
                            }
                            e.setCancelled(true);
                            break;
                        case SkyWarsItemNames.selectKit:
                            final ArrayList<Kit> kits = KitManager.getManager(bp).getAvailableKits();
                            if (kits.contains(kit)) {
                                p.closeInventory();
                                if (!KitManager.getManager(bp).selectSkyWarsKit(kit)) {
                                    p.sendMessage(SkyWarsMessages.kitAlreadyChosen);
                                } else {
                                    p.sendMessage(SkyWarsMessages.youChoseKit.replace("%k", kit.getName()));
                                }
                            } else {
                                p.closeInventory();
                                p.sendMessage(SkyWarsMessages.kitNotBought);
                            }
                            e.setCancelled(true);
                            break;
                        case SkyWarsItemNames.showKit:
                            KitManager.getManager(bp).showInventory(kit);
                            p.sendMessage(SkyWarsMessages.nowLookingAtKit.replace("%k", kit.getName()));
                            e.setCancelled(true);
                            break;
                    }
                }
            }
        }
    }

    public HashMap<GameInstance, VSRequest> getRequests() {
        return requests;
    }

    public World copyWorld(GameType type, int id, String mapName) {

        String name = type.getDisplayName() + "_" + mapName;

        World w = Bukkit.getWorld(name);
        if (w == null) {
            w = new WorldCreator(name).createWorld();
        }
        return WorldCopy.copyWorld(w, name.concat("_" + id));
    }
}