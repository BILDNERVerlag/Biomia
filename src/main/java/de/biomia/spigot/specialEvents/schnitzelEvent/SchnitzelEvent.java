package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.Main;
import de.biomia.spigot.achievements.Stats;
import de.biomia.spigot.tools.HeadCreator;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SchnitzelEvent extends BiomiaServer {

    private static final String backpackName = "§cBackpack";
    private static ItemStack book;
    private static SchnitzelEvent instance;
    private static final HashMap<Integer, Schnitzel> schnitzelMap = new HashMap<>();
    private static final HashMap<String, SecretBook> secretBookMap = new HashMap<>();
    private static final Location spawn = new Location(Bukkit.getWorld("BiomiaWelt"), 351, 72, 697.5, -35, 0);
    private static final HashMap<BiomiaPlayer, Inventory> inventorys = new HashMap<>();

    public SchnitzelEvent() {
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
        schnitzel.setLocation(new Location(world, 345.5, 80.1, 709.5));
        schnitzel.setDescription("Ein langer Weg,", "Das Ziel scheint unerreichbar", "Doch unten angekommen,", "Ist der Weg dann unvergleichbar");
        schnitzel.spawn();

        schnitzel = new Schnitzel(2);
        schnitzel.setLocation(new Location(world, 271.5, 22.1, 706.5));
        schnitzel.setDescription("Da bist du nun,", "unten.", "Ein langer weg,", "liegt nun vor dir");
        schnitzel.spawn();
    }

    private void initSecretBooks() {

        //TODO add Books

        SecretBook secretBook;
        World world = spawn.getWorld();

        secretBook = new SecretBook("§cBIO§bMIA §7| §cDas geheime Buch", 1);
        secretBook.setDescription("Da wo alles verdampft,", "gewinnt man keinen Kampf");
        secretBook.setLocation(new Location(world, 362.5, 17, 323.5));
        secretBook.spawn();
    }

    private void initSpawner() {

        //TODO add Spawner

        World world = spawn.getWorld();
        new Spawner(EntityType.ZOMBIE, new Location(world, 261.5, 19, 677.5), 4, 3);
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
                item.setType(Material.SKULL_ITEM);
                item.getData().setData((byte) 3);
                HeadCreator.getSkull(item, "d23eaefbd581159384274cdbbd576ced82eb72423f2ea887124f9ed33a6872c");
            }
            inv.setItem(each.getSlot(), item);
        });

        Set<String> foundBooks = getFoundBooks(biomiaPlayer);
        secretBookMap.values().forEach(each -> {
            ItemStack item = each.getItem().clone();
            if (!foundBooks.contains(each.getName())) {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§7???");
                item.setItemMeta(meta);
                item.setType(Material.SKULL_ITEM);
                item.getData().setData((byte) 3);
                HeadCreator.getSkull(item, "5163dafac1d91a8c91db576caac784336791a6e18d8f7f62778fc47bf146b6");
            }
            inv.setItem(each.getSlot(), item);
        });

        biomiaPlayer.getPlayer().openInventory(inv);
    }

    public static Set<String> getFoundSchnitzel(BiomiaPlayer bp) {
        return Stats.getComments(Stats.BiomiaStat.SchnitzelFound, bp.getBiomiaPlayerID()).keySet();
    }

    public static Set<String> getFoundBooks(BiomiaPlayer bp) {
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
            book = ItemCreator.itemCreate(Material.WRITTEN_BOOK, "§bInfo Buch der Schnitzeljäger");
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
