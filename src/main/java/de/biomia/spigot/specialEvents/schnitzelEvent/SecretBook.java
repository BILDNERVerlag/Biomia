package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.bungee.events.Time;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.achievements.Stats;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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
        Bukkit.getWorld("BiomiaWelt").dropItem(loc, is).setPickupDelay(0);
    }

    public void pickUp(BiomiaPlayer bp) {
        HashMap<String, Integer> comments = Stats.getComments(Stats.BiomiaStat.BooksFound, bp.getBiomiaPlayerID());

        if (!comments.containsKey(id + "")) {
            Stats.incrementStat(Stats.BiomiaStat.BooksFound, bp.getBiomiaPlayerID(), id + "");
            comments.put(id + "", 1);
            bp.sendMessage(Messages.PREFIX + "§cDu hast das Buch §b" + name + " §cgefunden!");
        } else {
            return;
        }

        if (comments.size() == 6) {
            Date date = Stats.getFirstIncrementDate(Stats.BiomiaStat.SchnitzelFound, bp);
            String text = Time.toText((int) (Calendar.getInstance().getTime().getTime() - date.getTime()) / 1000);

            Bukkit.broadcastMessage("§c" + bp.getName() + " §bhat alle §c" + SchnitzelEvent.getSchnitzel() + " §bSchnitzel in §c" + text + " §bgefunden!");
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
