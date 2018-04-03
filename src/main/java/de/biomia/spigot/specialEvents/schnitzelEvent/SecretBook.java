package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.achievements.BiomiaStat;
import de.biomia.spigot.server.quests.QuestEvents.Event;
import de.biomia.spigot.tools.ItemCreator;
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

    private ItemStack is;
    private Location loc;
    private final int id;
    private String name;
    private Event event;

    SecretBook(String name, int id) {
        this.id = id;
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
            bp.sendMessage(Messages.PREFIX + "§7Du hast das Buch " + name + " §7gefunden!");
            event.executeEvent(bp);
            if (comments.size() == SchnitzelEvent.getSecretBookMap().size()) {
                Date date = BiomiaStat.SchnitzelFound.getFirstIncrementDate(bp);
                int duration = (int) ((System.currentTimeMillis() - date.getTime()) / 1000);
                SchnitzelEvent.booksHighScore.put(bp.getName(), duration);
                SchnitzelEvent.reloadSBBooks();
                Bukkit.broadcastMessage("§c" + bp.getName() + " §bhat alle §c6 §bGeheimen Bücher in §c" + Time.toText(duration) + " §bgefunden!");

                if (SchnitzelEvent.getFirstName(SchnitzelEvent.booksHighScore).equals(bp.getName())) {
                    Bukkit.broadcastMessage("§c" + bp.getName() + " §bhat den High Score gebrochen!!!");
                }

            }
        }

    }

    public int getSlot() {
        return id + 17;
    }

    public ItemStack getItem() {
        return is;
    }

    public String getName() {
        return name;
    }
}
