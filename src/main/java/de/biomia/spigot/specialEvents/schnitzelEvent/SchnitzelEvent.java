package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.*;
import de.biomia.spigot.achievements.BiomiaStat;
import de.biomia.spigot.messages.manager.Scoreboards;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.LastPositionListener;
import de.biomia.spigot.tools.Teleporter;
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
    public static HashMap<String, MonsterPunkte> mobsKilled = new HashMap<>();
    public static HashMap<String, Integer> booksHighScore = new HashMap<>();
    public static HashMap<String, Integer> schnitzelHighScore = new HashMap<>();

    private static HashMap<String, ArrayList<String>> foundBooks = new HashMap<>();
    private static HashMap<String, ArrayList<String>> foundSchnitzel = new HashMap<>();

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
        new Teleporter(new Location(Bukkit.getWorld("BiomiaWelt"), 121, 63, 722), new Location(Bukkit.getWorld("BiomiaWelt"), 125, 68, 719),
                new Location(Bukkit.getWorld("BiomiaWelt"), 272.5, 69, 654, -120, 0));

        spawn.getWorld().setGameRuleValue("keepInventory", "true");
        spawn.getWorld().setGameRuleValue("mobGriefing", "false");
        spawn.getWorld().setGameRuleValue("randomTickSpeed", "0");
        spawn.getWorld().setDifficulty(Difficulty.NORMAL);


        for (Map.Entry<Integer, Integer> entry : BiomiaStat.SchnitzelMonsterKilled.getTop(-1, null).entrySet()) {
            OfflineBiomiaPlayer bp = Biomia.getOfflineBiomiaPlayer(entry.getKey());
            int i = BiomiaStat.SchnitzelDiedByMonster.get(bp.getBiomiaPlayerID(), null);
            mobsKilled.put(bp.getName(), new MonsterPunkte(bp, entry.getValue() - i));
        }


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
        Schnitzel schnitzel;
        World world = spawn.getWorld();

        //dschungeltempel
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 109.5, 85.2, 675));
        schnitzel.setDescription("Zwischen Bäumen und Büschen", "Und uralten Fischen", "Hiervon gibts noch mehr", "Doch manche findet man schwer!");
        schnitzel.spawn();

        //dschungeltempel unten
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 127.5, 72.2, 675.5));
        schnitzel.setDescription("Tief in dem Tempel", "Findet man Krempel", "Hinter Mauern und Ecken", "Kannst du es aufdecken?");
        schnitzel.spawn();

        //im haus
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 372.5, 73.2, 734.5));
        schnitzel.setDescription("Da bist du nun,", "Im Haus.", "Nun, die Suche...", "Ist noch nicht aus.");
        schnitzel.spawn();

        //unten, mineneingang
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 271.5, 22.2, 706.5));
        schnitzel.setDescription("Ein langer Weg,", "Das Ziel scheint unerreichbar", "Unten angekommen,", "Ist der Weg dann unvergleichbar");
        schnitzel.spawn();

        //geheimer schatz
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 236.5, 1.7, 623.5));
        schnitzel.setDescription("Versteckt am Boden der Mine", "Gibt es einen Platz.", "Wer suchet, der findet", "Den geheimen Schatz.");
        schnitzel.spawn();

        //sackgasse links anfang
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 107.5, 22.2, 576.5));
        schnitzel.setDescription("Von drei Gängen", "Nimm den zur Linken", "Dort findest du ein Schnitzel,", "Eins von den Besten");
        schnitzel.spawn();

        //geradeaus, hinter steinen
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 260.9, 19.2, 582.6));
        schnitzel.setDescription("Von Steinen fast zerdrückt,", "komm dorthin zurück.");
        schnitzel.spawn();

        //geradeaus, rechter gang
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 310.5, 18.2, 537.5));
        schnitzel.setDescription("Kann man in Schatten", "wie Wasser tauchen", "Werden unzählige", "Zombies auftauchen.");
        schnitzel.spawn();

        //rechts neben brücke
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 271.5, 16.7, 435.5));
        schnitzel.setDescription("Über die Lücke", "Führt eine Brücke", "Such gleich nebenan.", "Wer kann, der kann!");
        schnitzel.spawn();

        //links von brücke in eckigem raum
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 212, 21.2, 451.5));
        schnitzel.setDescription("Auf der Brück'", "Folg nicht den Schienen", "Viel interessanter: Die Minen!");
        schnitzel.spawn();

        //am ende der minecart bahn
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 361.0, 20.2, 426.6));
        schnitzel.setDescription("Am Ende der Bahn", "wirst du trotzdem belohnt", "Wirst wohl so schnell", "nicht mehr entthront.");
        schnitzel.spawn();

        //labyrintheingang: 370.5 23 439.5 -90 0
        //labyrinth
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 437.5, 23.2, 498.5));
        schnitzel.setDescription("Ein Ort, an dem mehr", "Gänge als Türen sind.", "Wer in Kisten sieht,", "findet das Labyrinth.");
        schnitzel.spawn();

        //bei den totenköpfen im eck
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 403.5, 15.2, 346.5));
        schnitzel.setDescription("Jeder Junge", "und jedes Mädel", "findet das Schnitzel", "hinter dem Schädel!");
        schnitzel.spawn();

        //bei den pilzen #1
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 251.5, 27.2, 197.5));
        schnitzel.setDescription("Ein kleiner Schritt für", "einen Menschen, aber", "ein riesiger für einen..", "Pilz..?");
        schnitzel.spawn();

        //bei den pilzen #2
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 282.5, 31.2, 292.5));
        schnitzel.setDescription("Ein kleiner Schritt für", "einen Pilz, aber", "ein riesiger für einen..", "Menschen..?");
        schnitzel.spawn();

        //rundgang ende
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 366.5, 21.2, 674.5));
        schnitzel.setDescription("Einmal den Rundgang", "überstanden!", "Besser als die", "ander'n Probanden!");
        schnitzel.spawn();

        //rundgang (unter wasser) ende
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 373.5, 21.2, 538.5));
        schnitzel.setDescription("Unter der Oberfläche sitzt", "(und lächelt verschmitzt)", "ein Schnitzel.");
        schnitzel.spawn();

        //mittig durch
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 247.5, 20.2, 375.5));
        schnitzel.setDescription("Lauf mittig hindurch", "oder zumindest versuch's,", "Lauf schnell und gerade", "zu dem, was du suchst.");
        schnitzel.spawn();

        //bei den kisten
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 141.5, 19.2, 291.5));
        schnitzel.setDescription("Such bei den Kisten", "Such unter den Kisten", "Such über den Kisten", "Such neben den Kisten");
        schnitzel.spawn();

        //loge
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 103.5, 27.7, 257.5));
        schnitzel.setDescription("Such am Ort mit der", "großartigen Aussicht", "Oder auch nicht.");
        schnitzel.spawn();

        //lavasee
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 92.5, 13.2, 188.5));
        schnitzel.setDescription("In den abgelegensten Ecken", "Hinter Lava und Steinen", "Kann man sich verstecken!");
        schnitzel.spawn();

        //hinterstes eck
        schnitzel = new Schnitzel();
        schnitzel.setLocation(new Location(world, 313, 8.2, 168));
        schnitzel.setDescription("Tief, tief unten,", "lauf so weit wie du kannst", "schnell wird gefunden", "was finden du kannst.");
        schnitzel.spawn();
    }

    private void initSecretBooks() {

        SecretBook secretBook;
        World world = spawn.getWorld();

        //tempel
        secretBook = new SecretBook("§cBIO§bMIA §7| §cWeltenlabor#2");
        secretBook.setDescription("Es liegt tief im Tempel", "unter dem Urwald,", "du hast es schon bald.");
        secretBook.setLocation(new Location(world, 122.5, 60.2, 671.5));
        secretBook.spawn();

        //lavasee
        secretBook = new SecretBook("§cBIO§bMIA §7| §cDas geheime Buch");
        secretBook.setDescription("Balancier über die Magma", "(das hier ist kein Manga!)");
        secretBook.setLocation(new Location(world, 361.5, 16.2, 322.5));
        secretBook.spawn();

        //über der brücke
        secretBook = new SecretBook("§cBIO§bMIA §7| §cDas Obsidian-Gefängnis");
        secretBook.setDescription("Manchmal führt ein Umweg", "zum Ziel!", "Besonders auf Brücken!");
        secretBook.setLocation(new Location(world, 210, 25.2, 403));
        secretBook.spawn();

        //jnr im see
        secretBook = new SecretBook("§cBIO§bMIA §7| §cDas Labyrinth des Todes");
        secretBook.setDescription("Inmitten des dunklen Sees", "Pass auf, dass sicher du stehst!");
        secretBook.setLocation(new Location(world, 218.5, 25.2, 260.5));
        secretBook.spawn();

        //totenkopf-lava-ding
        secretBook = new SecretBook("§cBIO§bMIA §7| §cWeltenlabor#1");
        secretBook.setDescription("Zwischen den Knochentoren", "Ist leicht alles verloren.");
        secretBook.setLocation(new Location(world, 520, 17.2, 390.5));
        secretBook.spawn();

        //unter brücke
        secretBook = new SecretBook("§cBIO§bMIA §7| §cDas Handbuch");
        secretBook.setDescription("Unter der zerbrochenen Brücke", "Auf dass es dir glücke!");
        secretBook.setLocation(new Location(world, 264, 5.2, 414));
        secretBook.spawn();
    }

    private void initSpawner() {

        //TODO pixelbiester rang
        //TODO teleporter bei tempel
        //TODO teleporter bei labyrinth

        World world = spawn.getWorld();
        new Spawner(EntityType.ZOMBIE, new Location(world, 261.5, 19, 677.5), 4, 3);
        new Spawner(EntityType.ZOMBIE, new Location(world, 348.5, 20.5, 687.5), 4, 3);
        new Spawner(EntityType.SKELETON, new Location(world, 197.5, 18.5, 627.5), 4, 2);
        new Spawner(EntityType.ZOMBIE, new Location(world, 262.2, 19, 595.5), 4, 3);
        new Spawner(EntityType.ZOMBIE, new Location(world, 262.7, 20.8, 460), 4, 3);
        new Spawner(EntityType.ZOMBIE, new Location(world, 156, 9, 265.5), 4, 3);
        new Spawner(EntityType.SKELETON, new Location(world, 228, 12, 353.5), 4, 2);
        new Spawner(EntityType.ZOMBIE, new Location(world, 265, 17, 393), 4, 3);
        new Spawner(EntityType.ZOMBIE, new Location(world, 241.5, 9, 278.5), 4, 3);
        new Spawner(EntityType.CREEPER, new Location(world, 364.5, 8, 387.5), 3, 2);
        new Spawner(EntityType.ZOMBIE, new Location(world, 222, 11.5, 182.5), 4, 3);
        new Spawner(EntityType.BLAZE, new Location(world, 154.5, 22.5, 582.5), 6, 1);
        new Spawner(EntityType.CAVE_SPIDER, new Location(world, 311, 9, 167.5), 4, 1);
        new Spawner(EntityType.SKELETON, new Location(world, 311, 9, 167.5), 4, 2);
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
        Inventory inv = inventorys.computeIfAbsent(biomiaPlayer, inventory -> Bukkit.createInventory(null, 45, backpackName));

        ArrayList<String> foundSchnitzel = getFoundSchnitzel(biomiaPlayer);

        schnitzelMap.values().forEach(each -> {
            ItemStack item = each.getItem().clone();
            if (!foundSchnitzel.contains(each.getID() + "")) {
                item.setType(Material.STONE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§7???");
                item.setItemMeta(meta);
            }
            inv.setItem(each.getSlot(), item);
        });

        ArrayList<String> foundBooks = getFoundBooks(biomiaPlayer);
        secretBookMap.values().forEach(each -> {
            ItemStack item = each.getItem().clone();
            if (!foundBooks.contains(each.getID() + "")) {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§7???");
                item.setItemMeta(meta);
                item.setType(Material.STONE_PLATE);
            }
            inv.setItem(each.getSlot(), item);
        });

        biomiaPlayer.getPlayer().openInventory(inv);
    }

    public static ArrayList<String> getFoundSchnitzel(BiomiaPlayer bp) {
        return foundSchnitzel.computeIfAbsent(bp.getName(), list -> new ArrayList<>(BiomiaStat.SchnitzelFound.getComments(bp.getBiomiaPlayerID()).keySet()));
    }

    public static ArrayList<String> getFoundBooks(BiomiaPlayer bp) {
        return foundBooks.computeIfAbsent(bp.getName(), list -> new ArrayList<>(BiomiaStat.BooksFound.getComments(bp.getBiomiaPlayerID()).keySet()));
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
            book = ItemCreator.itemCreate(Material.WRITTEN_BOOK, "§bHandbuch der Schnitzeljäger");
            BookMeta bookMeta = (BookMeta) book.getItemMeta();
            List<IChatBaseComponent> pages;
            try {
                pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
                pages.clear();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return null;
            }
            TextComponent textComp1 = new TextComponent(
                    "Checkpoints:\n" +
                            "Alle 2 Minuten wird\n" +
                            "automatisch ein\n" +
                            "Checkpoint (Spawnpunkt)\n" +
                            "auf deine aktuelle\n" +
                            "Position gesetzt.\n" +
                            "/checkpoint für TP zum\n" +
                            "letzten Checkpoint\n" +
                            "/checkpoint set Setze\n" +
                            "den Checkpoint manuell");
            //TextComponent textComp2 = new TextComponent("\n\nINFO TEXT HINZUFÜGEN!");
            //textComp1.addExtra(textComp2);
            IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(textComp1));
            pages.add(page);
            TextComponent textComp2 = new TextComponent(
                    "Preise:\n" +
                            "Die Hauptpreise gehen je an die 3 besten Spieler in jeder Kategorie:\n" +
                            "-Meiste Monstermarken\n" +
                            "-Am schnellsten alle Schnitzel gefunden\n" +
                            "-Am schnellsten alle Bücher gefunden");
            IChatBaseComponent page2 = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(textComp2));
            pages.add(page2);
            //TODO: Preise hinzufügen
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

        mobHS.setPrefix("§r §r §r §r");
        schnitzelHS.setPrefix("§r §r §r §r");
        bookHS.setPrefix("§r §r §r §r");

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
                mobsKilled = sortMonsterPoints(mobsKilled);

                mobHS.setPrefix(mobsKilled.isEmpty() ? "§7§m---" : "§7" + mobsKilled.values().stream().findFirst().map(MonsterPunkte::getPoints).orElse(null));
                mobHSName.setSuffix(mobsKilled.isEmpty() ? "§7§m---" : mobsKilled.values().stream().findFirst().map(MonsterPunkte::getName).orElse(null));
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

    private static HashMap<String, MonsterPunkte> sortMonsterPoints(Map<String, MonsterPunkte> map) {
        List<Map.Entry<String, MonsterPunkte>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> o2.getValue().getPoints() - o1.getValue().getPoints());
        HashMap<String, MonsterPunkte> result = new LinkedHashMap<>();
        for (Map.Entry<String, MonsterPunkte> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}