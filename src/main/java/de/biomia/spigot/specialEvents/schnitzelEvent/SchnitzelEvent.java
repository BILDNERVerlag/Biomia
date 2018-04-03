package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.*;
import de.biomia.spigot.achievements.BiomiaStat;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.LastPositionListener;
import de.biomia.universal.Time;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class SchnitzelEvent extends BiomiaServer {

    private static final String backpackName = "§cBackpack";
    private static ItemStack book;
    private static SchnitzelEvent instance;
    private static final HashMap<Integer, Schnitzel> schnitzelMap = new HashMap<>();
    private static final HashMap<String, SecretBook> secretBookMap = new HashMap<>();
    private static final Location spawn = new Location(Bukkit.getWorld("BiomiaWelt"), 351, 72, 697.5, -35, 0);
    private static final HashMap<BiomiaPlayer, Inventory> inventorys = new HashMap<>();

    public static HashMap<String, Integer> mobsKilled = new HashMap<>();
    public static HashMap<String, Integer> booksHighScore = new HashMap<>();
    public static HashMap<String, Integer> schnitzelHighScore = new HashMap<>();

    private static String placeholder = "§7§m---";

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

        spawn.getWorld().setGameRuleValue("keepInventory", "true");
        spawn.getWorld().setGameRuleValue("mobGriefing", "false");
        spawn.getWorld().setGameRuleValue("randomTickSpeed", "0");
        spawn.getWorld().setDifficulty(Difficulty.NORMAL);


        for (Map.Entry<Integer, Integer> integerIntegerEntry : BiomiaStat.SchnitzelMonsterKilled.getTop(-1, null).entrySet())
            mobsKilled.put(Biomia.getOfflineBiomiaPlayer(integerIntegerEntry.getKey()).getName(), integerIntegerEntry.getValue());

        BiomiaStat.SchnitzelFound.getBiomiaIDSWhereValueIsX(schnitzelMap.size(), null).forEach(each -> {

            OfflineBiomiaPlayer bp = Biomia.getOfflineBiomiaPlayer(each);

            Date first = BiomiaStat.SchnitzelFound.getFirstIncrementDate(bp);
            Date last = BiomiaStat.SchnitzelFound.getLastIncrementDate(bp);

            int duration = ((int) last.getTime() / 1000) - ((int) first.getTime() / 1000);

            schnitzelHighScore.put(bp.getName(), duration);

        });

        BiomiaStat.BooksFound.getBiomiaIDSWhereValueIsX(secretBookMap.size(), null).forEach(each -> {

            OfflineBiomiaPlayer bp = Biomia.getOfflineBiomiaPlayer(each);

            Date first = BiomiaStat.SchnitzelFound.getFirstIncrementDate(bp);
            Date last = BiomiaStat.BooksFound.getLastIncrementDate(bp);

            int duration = ((int) last.getTime() / 1000) - ((int) first.getTime() / 1000);

            booksHighScore.put(bp.getName(), duration);

        });

        initScoreboard();

    }

    @Override
    protected void initCommands() {
        super.initCommands();
        Main.registerCommand(new Checkpoint());
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
                item.setType(Material.STONE);
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
                item.setType(Material.STONE_PLATE);
            }
            inv.setItem(each.getSlot(), item);
        });

        biomiaPlayer.getPlayer().openInventory(inv);
    }

    public static Set<String> getFoundSchnitzel(BiomiaPlayer bp) {
        return BiomiaStat.SchnitzelFound.getComments(bp.getBiomiaPlayerID()).keySet();
    }

    public static Set<String> getFoundBooks(BiomiaPlayer bp) {
        return BiomiaStat.BooksFound.getComments(bp.getBiomiaPlayerID()).keySet();
    }

    public static String getBackpackName() {
        return backpackName;
    }

    public static HashMap<String, SecretBook> getSecretBookMap() {
        return secretBookMap;
    }

    @SuppressWarnings("unchecked")
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

    public static Location getSpawn() {
        return spawn;
    }

    private static Scoreboard sb = Scoreboards.getMainScoreboard();
    private static Team schnitzelHS, bookHS, mobHS, schnitzelHSName, bookHSName, mobHSName;

    private static void initScoreboard() {

        Objective o = sb.registerNewObjective("aaa", "bbb");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName("\u00A7cBIO\u00A7bMIA");
        o.getScore(" ").setScore(13);
        o.getScore("\u00A7cMobs Getötet:").setScore(12);
        o.getScore("\u00A7c\u00A7b").setScore(11);
        o.getScore("\u00A7a").setScore(10);
        o.getScore("\u00A7b").setScore(9);
        o.getScore("\u00A7cSchnitzel:").setScore(8);
        o.getScore("\u00A7f\u00A7b").setScore(7);
        o.getScore("\u00A71").setScore(6);
        o.getScore("\u00A72").setScore(5);
        o.getScore("\u00A7cBücher:").setScore(4);
        o.getScore("\u00A7r\u00A7b").setScore(3);
        o.getScore("\u00A73").setScore(2);
        o.getScore("\u00A7l").setScore(1);

        mobHS = sb.registerNewTeam("mobsHS");
        mobHSName = sb.registerNewTeam("mobsHSName");
        schnitzelHS = sb.registerNewTeam("schnitzelHS");
        schnitzelHSName = sb.registerNewTeam("schnitzelHSName");
        bookHS = sb.registerNewTeam("bookHS");
        bookHSName = sb.registerNewTeam("bookHSName");

        mobHS.setSuffix("   ");
        schnitzelHS.setSuffix("   ");
        bookHS.setSuffix("   ");

        mobHSName.addEntry("\u00A7c\u00A7b");
        mobHSName.setPrefix("§7#§c1 ");
        schnitzelHSName.addEntry("\u00A7f\u00A7b");
        schnitzelHSName.setPrefix("§7#§c1 ");
        bookHSName.addEntry("\u00A7r\u00A7b");
        bookHSName.setPrefix("§7#§c1 ");

        mobHS.addEntry("\u00A7a");
        schnitzelHS.addEntry("\u00A71");
        bookHS.addEntry("\u00A73");

        reloadSBBooks();
        reloadSBSchnitzel();
        new BukkitRunnable() {
            @Override
            public void run() {
                mobsKilled = sortByValue(mobsKilled, true);

                mobHS.setPrefix(mobsKilled.isEmpty() ? "§7§m---" : "§7" + getFirstInt(mobsKilled));
                mobHSName.setSuffix(mobsKilled.isEmpty() ? "§7§m---" : getFirstName(mobsKilled));
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20 * 5);

    }

    public static void reloadSBSchnitzel() {
        schnitzelHighScore = sortByValue(schnitzelHighScore, false);
        String name = getFirstName(schnitzelHighScore);
        String integer = getFirstInt(schnitzelHighScore);

        schnitzelHSName.setSuffix(name == null ? placeholder : name);
        schnitzelHS.setPrefix(integer == null ? placeholder : "§7" + Time.toFromatString("HH:mm:ss", Integer.valueOf(integer)));
    }

    public static void reloadSBBooks() {
        booksHighScore = sortByValue(booksHighScore, false);
        String name = getFirstName(booksHighScore);
        String integer = getFirstInt(booksHighScore);

        bookHSName.setSuffix(name == null ? placeholder : name);
        bookHS.setPrefix(integer == null ? placeholder : "§7" + Time.toFromatString("HH:mm:ss", Integer.valueOf(integer)));
    }

    public static String getFirstName(HashMap<String, Integer> map) {
        return map.keySet().stream().findFirst().orElse(null);
    }

    private static String getFirstInt(HashMap<String, Integer> map) {
        return map.values().stream().findFirst().map(Object::toString).orElse(null);
    }

    private static <K, V extends Comparable<? super V>> HashMap<K, V> sortByValue(Map<K, V> map, boolean invert) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        if (invert)
            list.sort(Comparator.comparing(Map.Entry<K, V>::getValue).reversed());
        else
            list.sort(Comparator.comparing(Map.Entry<K, V>::getValue));

        HashMap<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
