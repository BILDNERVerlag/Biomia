package de.biomia.minigames.versus.vs.main;

import de.biomia.minigames.versus.sw.kits.Kit;
import de.biomia.minigames.versus.sw.kits.KitManager;
import de.biomia.general.messages.SkyWarsItemNames;
import de.biomia.general.messages.SkyWarsMessages;
import de.biomia.minigames.versus.sw.var.Variables;
import de.biomia.minigames.versus.vs.settings.VSGroup;
import de.biomia.minigames.versus.vs.settings.VSRequest;
import de.biomia.minigames.versus.vs.settings.VSSettingItem;
import de.biomia.minigames.versus.vs.settings.VSSettings;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.Main;
import de.biomia.api.messages.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private final HashMap<VSMode, HashMap<Integer, String>> mapNames = new HashMap<>();
    private final Location home = new Location(Bukkit.getWorld("Spawn"), 0.5, 75, -0.5, 40, 0);
    private final ItemStack toChallangeItem = ItemCreator.itemCreate(Material.DIAMOND_SWORD, "\u00A7cHerausforderer");
    private final ItemStack settingItem = ItemCreator.itemCreate(Material.REDSTONE, "\u00A7cEinstellungen");
    private VSGroup main;

    public VSManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
        init();
    }

    private void init() {

        HashMap<Integer, String> swMaps = new HashMap<>();
        HashMap<Integer, String> bwMaps = new HashMap<>();
        HashMap<Integer, String> vsMaps = new HashMap<>();

        bwMaps.put(100, "Village");
        swMaps.put(100, "Village");
        vsMaps.put(100, "Village");

        mapNames.put(VSMode.BedWars, bwMaps);
        mapNames.put(VSMode.SkyWars, swMaps);
        mapNames.put(VSMode.KitPVP, vsMaps);

        main = new VSGroup(VSMode.Main, "\u00A7cEinstellungen", null, 0, false, null);

        ItemStack bedwarsSettingItem = ItemCreator.itemCreate(Material.BED, "\u00A7cBedWars");
        ItemStack skywarsSettingItem = ItemCreator.itemCreate(Material.GRASS, "\u00A7aSkyWars");
        ItemStack kitPvPSettingItem = ItemCreator.itemCreate(Material.IRON_SWORD, "\u00A7dKitPvP");

        VSGroup bedwars = main.registerNewGroup(VSMode.BedWars, bedwarsSettingItem, "\u00A7cBedWars", 6, 0, true);
        VSGroup bedwarsMaps = bedwars.registerNewGroup(VSMode.BedWars, ItemCreator.itemCreate(Material.PAPER, "\u00A7aMaps"), "\u00A7aMaps", 0);
        bedwarsMaps.registerSetting(new VSSettingItem(ItemCreator.itemCreate(Material.FLOWER_POT_ITEM), 100, 0, true, bedwarsMaps));

        VSGroup skywars = main.registerNewGroup(VSMode.SkyWars, skywarsSettingItem, "\u00A7aSkyWars", 4, 0, true);
        VSGroup skywarsMaps = skywars.registerNewGroup(VSMode.SkyWars, ItemCreator.itemCreate(Material.PAPER, "\u00A7aMaps"), "\u00A7aMaps", 0);
        skywarsMaps.registerSetting(new VSSettingItem(ItemCreator.itemCreate(Material.FLOWER_POT_ITEM), 100, 0, true, skywarsMaps));
        skywars.registerNewGroup(VSMode.SkyWars, Variables.kitItem, SkyWarsItemNames.kitItemName, 1);

        VSGroup kitpvp = main.registerNewGroup(VSMode.KitPVP, kitPvPSettingItem, "\u00A7dKitPvP", 2, 0, true);
        VSGroup kitpvpsMaps = kitpvp.registerNewGroup(VSMode.KitPVP, ItemCreator.itemCreate(Material.PAPER, "\u00A7aMaps"), "\u00A7aMaps", 0);
        kitpvpsMaps.registerSetting(new VSSettingItem(ItemCreator.itemCreate(Material.FLOWER_POT_ITEM), 100, 0, true, kitpvpsMaps));
    }

    public void moveToLobby(Player p) {
        p.teleport(home);
        p.setGameMode(GameMode.SURVIVAL);
        setInventory(p);
        p.setHealth(20);
        p.setFoodLevel(20);
        //TODO scoreboard
    }

    public String getMapName(VSMode mode, int mapID) {
        return mapNames.get(mode).get(mapID);
    }

    public void setInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setItem(3, settingItem);
        p.getInventory().setItem(5, toChallangeItem);
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
                if (is.hasItemMeta()) {
                    if (is.getItemMeta().getDisplayName().equals("\u00A7cHerausforderer")) {
                        if (e.getEntity() instanceof Player) {
                            Player p1 = (Player) e.getEntity();
                            BiomiaPlayer hittedPlayer = Biomia.getBiomiaPlayer(p1);

                            if (VSRequest.hasRequestSended(hitter, hittedPlayer)) {
                                hitter.getPlayer().sendMessage("\u00A7cDu hast dem Spieler bereits eine Anfrage geschickt!");
                            } else if (VSRequest.hasRequestSended(hittedPlayer, hitter)) {
                                VSRequest request = VSRequest.getRequest(hittedPlayer);
                                assert request != null;
                                request.accept();
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

                    Kit kit = KitManager.standardKit;
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
                            break;
                    case SkyWarsItemNames.showKit:
                            KitManager.getManager(bp).showInventory(kit);
                        p.sendMessage(SkyWarsMessages.nowLookingAtKit.replace("%k", kit.getName()));
                            break;
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

    public Location getHome() {
        return home;
    }

    public enum VSMode {
        Main, BedWars, SkyWars, KitPVP
    }
}