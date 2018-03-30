package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.achievements.Stats;
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
import java.util.Set;

public class SecretBook {

    public ItemStack is;
    public Location loc;
    public final int id;
    public String name;

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

    public void spawn() {

        if (!loc.getChunk().isLoaded())
            loc.getChunk().load();

        Bukkit.getWorld("BiomiaWelt").dropItem(loc, is).setPickupDelay(0);
    }

    public void pickUp(BiomiaPlayer bp) {

        Set<String> comments = SchnitzelEvent.getFoundBooks(bp);

        if (!comments.contains(name)) {
            Stats.incrementStat(Stats.BiomiaStat.BooksFound, bp.getBiomiaPlayerID(), name);
            bp.sendMessage(Messages.PREFIX + "§7Du hast das Buch " + name + " §7gefunden!");
        } else {
            return;
        }

        if (comments.size() == 5) {
            Date date = Stats.getFirstIncrementDate(Stats.BiomiaStat.SchnitzelFound, bp);
            String text = Time.toText((int) ((System.currentTimeMillis() - date.getTime()) / 1000));
            Bukkit.broadcastMessage("§c" + bp.getName() + " §bhat alle §c6 §bGeheimen Bücher in §c" + text + " §bgefunden!");
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
