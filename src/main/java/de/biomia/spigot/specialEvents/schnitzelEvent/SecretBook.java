package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.achievements.BiomiaStat;
import de.biomia.spigot.server.quests.QuestEvents.Event;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.RewardItems;
import de.biomia.universal.Messages;
import de.biomia.universal.Time;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SecretBook {

    private final ItemStack is;
    private Location loc;
    private final int id;
    private final String name;
    private Event event;

    private static int bookCount = 1;

    SecretBook(String name) {
        this.id = bookCount++;
        this.name = name;
        SchnitzelEvent.getSecretBookMap().put(name, this);
        is = ItemCreator.itemCreate(Material.BOOK, String.format(name, id));
    }

    public void setDescription(String... lore) {
        ItemMeta meta = is.getItemMeta();

        ArrayList<String> list = new ArrayList<>();
        list.add("");
        list.addAll(Arrays.asList(lore));
        meta.setLore(list);

        is.setItemMeta(meta);
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public void setReward(Event event) {
        this.event = event;
    }

    public void spawn() {

        if (!loc.getChunk().isLoaded())
            loc.getChunk().load();

        Bukkit.getWorld("BiomiaWelt").dropItem(loc, is).setPickupDelay(0);
    }

    public void pickUp(BiomiaPlayer bp) {

        ArrayList<String> comments = SchnitzelEvent.getFoundBooks(bp);

        if (!comments.contains(id + "")) {
            comments.add(id + "");
            BiomiaStat.BooksFound.increment(bp.getBiomiaPlayerID(), 1, id + "");
            bp.sendMessage(Messages.PREFIX + "00A77Du hast das Buch " + name + " 00A77gefunden!");
            switch (id) {
                case 1:
                    ItemStack itemStack = ItemCreator.itemCreate(Material.DIAMOND);
                    itemStack.setAmount(3);
                    RewardItems.addItem(bp, itemStack, BiomiaServerType.Freebuild);
                    bp.sendMessage(Messages.PREFIX + "00A77Du erh00e4ltst 00A7c3 Diamanten! (Freebuildwelt)00A77!");
                    break;
                case 2:
                    itemStack = ItemCreator.itemCreate(Material.GOLD_BLOCK);
                    RewardItems.addItem(bp, itemStack, BiomiaServerType.Freebuild);
                    bp.sendMessage(Messages.PREFIX + "00A77Du erh00e4ltst 00A7ceinen Goldblock! (Freebuildwelt)00A77!");
                    break;
                case 3:
                    itemStack = ItemCreator.itemCreate(Material.LAPIS_BLOCK);
                    RewardItems.addItem(bp, itemStack, BiomiaServerType.Quest);
                    bp.sendMessage(Messages.PREFIX + "00A77Du erh00e4ltst 00A7ceinen Lapisblock! (Freebuildwelt)00A77!");
                    break;
                case 4:
                    bp.addCoins(400, false);
                    break;
                case 5:
                    itemStack = ItemCreator.itemCreate(Material.DIAMOND_SWORD);
                    itemStack.setAmount(1);
                    RewardItems.addItem(bp, itemStack, BiomiaServerType.Quest);
                    bp.sendMessage(Messages.PREFIX + "00A77Du erh00e4ltst 00A7c1 Diamantschwert! (Questwelt)00A77!");
                    break;
                case 6:
                    itemStack = ItemCreator.itemCreate(Material.DIAMOND);
                    itemStack.setAmount(1);
                    RewardItems.addItem(bp, itemStack, BiomiaServerType.Quest);
                    bp.sendMessage(Messages.PREFIX + "00A77Du erh00e4ltst 00A7c1 Totem der Unsterblichkeit! (Questwelt)00A77!");
                    break;
                default:
                    break;
            }
            if (event != null) {
                event.executeEvent(bp);
            }
            if (comments.size() == SchnitzelEvent.getSecretBookMap().size()) {
                Date date = BiomiaStat.SchnitzelFound.getFirstIncrementDate(bp);
                int duration = (int) ((System.currentTimeMillis() - date.getTime()) / 1000);
                SchnitzelEvent.booksHighScore.put(bp.getName(), duration);
                SchnitzelEvent.reloadSBBooks();
                Bukkit.broadcastMessage("00A7c" + bp.getName() + " 00A7bhat alle 6 00A7bGeheimen B00dccher in 00A7c" + Time.toText(duration) + " 00A7bgefunden!");

                if (SchnitzelEvent.getFirstName(SchnitzelEvent.booksHighScore).equals(bp.getName())) {
                    Bukkit.broadcastMessage("00A7c" + bp.getName() + " 00A7bhat den Highscore gebrochen!");
                }

            }
            if (id == 5) {
                bp.getPlayer().teleport(new Location(Bukkit.getWorld("BiomiaWelt"), 424.5, 18, 390.5, 90, 5));
            }
        }

    }

    public int getID() {
        return id;
    }

    public int getSlot() {
        return id + 26;
    }

    public ItemStack getItem() {
        return is;
    }

    public String getName() {
        return name;
    }
}
