package de.biomia.spigot.minigames.versus;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.KitPVPMessages;
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
import de.biomia.spigot.tools.BackToLobby;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.WorldCopy;
import de.biomia.universal.Messages;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VSManager implements Listener {

    private final HashMap<BiomiaPlayer, VSSettings> settings = new HashMap<>();
    private final HashMap<GameInstance, VSRequest> requests = new HashMap<>();
    private final ItemStack challengeItem = ItemCreator.itemCreate(Material.DIAMOND_SWORD, "\u00A7cHerausforderer");
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
        ItemStack kitPvPSettingItem = ItemCreator.itemCreate(Material.IRON_SWORD, "\u00A7bKitPvP");

        VSSettingItem map;

        VSGroup bedwars = main.registerDeaktivatableGroup(GameType.BED_WARS_VS, bedwarsSettingItem, "\u00A7cBedWars", 6, 0, true);
        VSGroup bedwarsMaps = bedwars.registerGroup(GameType.BED_WARS_VS, ItemCreator.itemCreate(Material.PAPER, "\u00A7aMaps"), "\u00A7aMaps", 0);
        bedwarsMaps.registerSetting(map = new VSSettingItem(ItemCreator.itemCreate(Material.FLOWER_POT_ITEM), 100, 0, true, bedwarsMaps, "Mayaria"));
        map.setAsMap();

        VSGroup skywars = main.registerDeaktivatableGroup(GameType.SKY_WARS_VS, skywarsSettingItem, "\u00A7aSkyWars", 2, 0, true);
        VSGroup skywarsMaps = skywars.registerGroup(GameType.SKY_WARS_VS, ItemCreator.itemCreate(Material.PAPER, "\u00A7aMaps"), "\u00A7aMaps", 0);
        skywarsMaps.registerSetting(map = new VSSettingItem(ItemCreator.itemCreate(Material.SMOOTH_BRICK, (byte) 2), 100, 0, true, skywarsMaps, "Ruins"));
        skywars.registerGroup(GameType.SKY_WARS_VS, SkyWarsItemNames.kitItem, SkyWarsItemNames.kitItemName, 1);
        map.setAsMap();

        VSGroup kitpvp = main.registerDeaktivatableGroup(GameType.KIT_PVP_VS, kitPvPSettingItem, "\u00A7bKitPvP", 4, 0, true);
        VSGroup kitpvpsMaps = kitpvp.registerGroup(GameType.KIT_PVP_VS, ItemCreator.itemCreate(Material.PAPER, "\u00A7aMaps"), "\u00A7aMaps", 0);
        kitpvpsMaps.registerSetting(map = new VSSettingItem(ItemCreator.itemCreate(Material.FLOWER_POT_ITEM), 100, 0, true, kitpvpsMaps, "Temple"));
        kitpvp.registerGroup(GameType.KIT_PVP_VS, SkyWarsItemNames.kitItem, KitPVPMessages.kitItemName, 1);
        map.setAsMap();
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
        Bukkit.getOnlinePlayers().forEach(all -> p.showPlayer(Main.getPlugin(), all));
    }

    public void setInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setItem(2, settingItem);
        p.getInventory().setItem(6, challengeItem);
        giveBook(p);
        BackToLobby.getLobbyItem(p, 8);
    }

    @SuppressWarnings("unchecked")
    private void giveBook(Player p) {
        ItemStack book = ItemCreator.itemCreate(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) (book.getItemMeta());
        List<IChatBaseComponent> pages;
        // Referenz auf die Liste der Buchseiten holen
        try {
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
            pages.clear();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return;
        }

        // Seiten befuellen
        TextComponent page0 = new TextComponent("Willkommen auf dem Duell-Server der " + Messages.BIOMIA + "-Tec! " +
                "Hier kannst du in den Modi \u00A7cSkyWars\u00A7r, \u00A7cBedWars\u00A7r und \u00A7cKitPVP\u00A7r einzeln gegeneinander antreten.\n\n" +
                "\u00A7c\u00A7nKitPVP:\n" +
                "Zwei Spieler k00e4mpfen mit identischen Kits gegeneinander.");
        IChatBaseComponent pageZero = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(page0));

        TextComponent page1 = new TextComponent("\u00A7c\u00A7nEinstellungen:\n\n");
        page1.addExtra(new TextComponent("In den Einstellungen kannst du dir aus- " +
                "suchen, welche Spielmodus du gerne " +
                "spielen w00fcrdest.\n" +
                "Au00dferdem kannst du dort auch " +
                "die Maps und dein Kit ausw00e4hlen."));
        IChatBaseComponent pageFirst = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(page1));

        TextComponent page2 = new TextComponent("\u00A7c\u00A7nWarteschlange:\n\n");
        page2.addExtra(new TextComponent("Um der Warteschlange " +
                "beizutreten, musst du " +
                "nur den Dorfbewohner" +
                "mit dem Schwert schlagen. " +
                "Sobald jemand anderes bei- " +
                "tritt, der die selben Einstellungen " +
                "hat wie du (also genau die selben " +
                "Spielmodus spielen will), geht das Spiel " +
                "auch schon los!\n\n"));
        IChatBaseComponent pageSecond = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(page2));

        TextComponent page3 = new TextComponent("\u00A7c\u00A7nSchwert:\n\n");
        page3.addExtra(new TextComponent("Mit dem Schwert kannst du andere " +
                "Spieler manuell herausfordern. Wenn \u00A7ldu\u00A7r " +
                "herausgefordert wirst, kannst du durch " +
                "Klick auf die Nachricht annehmen."));
        IChatBaseComponent pageThird = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(page3));

        pages.add(pageZero);
        pages.add(pageFirst);
        pages.add(pageSecond);
        pages.add(pageThird);

        // Buch-Itemstack updaten
        bookMeta.setTitle("\u00A7cHandbuch");
        bookMeta.setAuthor("BIOMIA-Team");
        book.setItemMeta(bookMeta);
        p.getInventory().setItem(0, book);
    }

    private void openMainInventory(BiomiaPlayer bp) {
        main.getInventory(bp).openInventory();
    }

    private void addToQueue(BiomiaPlayer bp) {
        if (WarteschlangenManager.contains(bp)) {
            ActionBar.sendActionBar(String.format("%sDu bist bereits in der Warteschlange.", Messages.COLOR_MAIN), bp.getPlayer());
            return;
        }
        ActionBar.sendActionBar(String.format("%sDu bist der Warteschlange beigetreten!", Messages.COLOR_AUX), bp.getPlayer());
        WarteschlangenManager.add(bp);
    }

    private void removeFromQueue(BiomiaPlayer bp) {
        WarteschlangenManager.remove(bp);
        ActionBar.sendActionBar(String.format("%sDu hast die Warteschlange verlassen!", Messages.COLOR_AUX), bp.getPlayer());
    }

    public VSSettings getSettings(BiomiaPlayer bp) {
        return settings.computeIfAbsent(bp, b -> new VSSettings(bp));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (e.hasItem()) {
            ItemStack is = e.getItem();
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
        if (e.getRightClicked() instanceof Villager)
            e.setCancelled(true);
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
                                p.sendMessage("\u00A7cDu hast keine Herausforderung von \u00A7b" + p1.getName() + "\u00A7c die du ablehnen kannst!");
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
    public static void onInventoryClick(InventoryClickEvent e) {

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