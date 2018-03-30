package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import de.biomia.spigot.achievements.Stats;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.LastPositionListener;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SchnitzelEvent extends BiomiaServer {

    private static final String backpackName = "§cBackpack";
    private static ItemStack book;
    private static SchnitzelEvent instance;
    private static final HashMap<Integer, Schnitzel> schnitzelMap = new HashMap<>();
    private static final HashMap<String, SecretBook> secretBookMap = new HashMap<>();
    private static final Location spawn = new Location(Bukkit.getWorld("BiomiaWelt"), 22, 100, 11);
    private static final HashMap<BiomiaPlayer, Inventory> inventorys = new HashMap<>();

    protected SchnitzelEvent() {
        super(BiomiaServerType.Event_Schnitzeljagd);
        instance = this;
    }

    @Override
    public void start() {
        super.start();
        initSchnitzel();
        initSecretBooks();
        initSpawner();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        Bukkit.getPluginManager().registerEvents(new LastPositionListener(spawn), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new SchnitzelListener(), Main.getPlugin());
    }

    private void initSchnitzel() {

        //TODO add Schnitzel

        Schnitzel schnitzel;
        World world = spawn.getWorld();

        schnitzel = new Schnitzel(1);
        schnitzel.setLocation(new Location(world, 1, 70, 1));
        schnitzel.setDescription("Das ist ein Test", "Noch ein Test");
    }

    private void initSecretBooks() {

        //TODO add Books

        SecretBook secretBook;
        World world = spawn.getWorld();

        secretBook = new SecretBook("TestBuch", 1);
        secretBook.setDescription("123", "TEEST");
        secretBook.setLocation(new Location(world, 1, 1, 1));
    }

    private void initSpawner() {

        //TODO add Spawner

        World world = spawn.getWorld();
        new Spawner(EntityType.ZOMBIE, new Location(world, 50, 80, 50), 3, 3);
    }

    public static SchnitzelEvent getInstance() {
        return instance;
    }

    public static Schnitzel getSchnitzel(String name) {
        name = name.substring(Schnitzel.name.length() - 2);
        int i = Integer.valueOf(name);
        return schnitzelMap.get(i);
    }

    public static SecretBook getSecretBook(String name) {
        return secretBookMap.get(name);
    }

    public static HashMap<Integer, Schnitzel> getSchnitzelMap() {
        return schnitzelMap;
    }

    public static int getSchnitzel() {
        return schnitzelMap.size();
    }

    public static void openBackpack(BiomiaPlayer biomiaPlayer) {
        Inventory inv = inventorys.computeIfAbsent(biomiaPlayer, inventory -> Bukkit.createInventory(null, 27, backpackName));

        Set<String> foundSchnitzel = getFoundSchnitzel(biomiaPlayer);

        schnitzelMap.values().forEach(each -> {
            ItemStack item = each.getItem().clone();
            if (!foundSchnitzel.contains(each.getID() + "")) {
                item.setType(Material.STONE);
            }
            inv.setItem(each.getSlot(), item);
        });

        Set<String> foundBooks = getFoundBooks(biomiaPlayer);
        secretBookMap.values().forEach(each -> {
            ItemStack item = each.getItem().clone();
            if (!foundBooks.contains(each.getName())) {
                item.setType(Material.INK_SACK);
            }
            inv.setItem(each.getSlot(), item);
        });

        biomiaPlayer.getPlayer().openInventory(inv);
    }

    public static Set<String> getFoundSchnitzel(BiomiaPlayer bp) {
        return Stats.getComments(Stats.BiomiaStat.SchnitzelFound, bp.getBiomiaPlayerID()).keySet();
    }

    private static Set<String> getFoundBooks(BiomiaPlayer bp) {
        return Stats.getComments(Stats.BiomiaStat.BooksFound, bp.getBiomiaPlayerID()).keySet();
    }

    public static String getBackpackName() {
        return backpackName;
    }

    public static HashMap<String, SecretBook> getSecretBookMap() {
        return secretBookMap;
    }

    @SuppressWarnings("unchecked cast")
    public static ItemStack getInfoBook() {

        if (book == null) {
            book = ItemCreator.itemCreate(Material.BOOK, "§bInfo Buch der Schnitzeljäger");
            BookMeta bookMeta = (BookMeta) book.getItemMeta();
            List<IChatBaseComponent> pages;

            try {
                pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
                pages.clear();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return null;
            }

            // TODO Infoseite schreiben
            TextComponent verschiedenesUeberschrift = new TextComponent("ÜBERSCHRIFFT");

            TextComponent statsButton = new TextComponent("\n\nINFO TEXT HINZUFÜGEN!");
            verschiedenesUeberschrift.addExtra(statsButton);

            IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(verschiedenesUeberschrift));
            pages.add(page);
            book.setItemMeta(bookMeta);
        }
        return book;
    }
}
