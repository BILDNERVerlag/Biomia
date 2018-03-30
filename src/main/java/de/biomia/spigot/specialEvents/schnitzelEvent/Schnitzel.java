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

public class Schnitzel {

    public final static String name = "§cSchnitzel §7#§b%s";

    private ItemStack is;
    private Location loc;
    private final int id;

    Schnitzel(int id) {
        this.id = id;
        SchnitzelEvent.getSchnitzelMap().put(id, this);
        is = ItemCreator.itemCreate(Material.PAPER, String.format(name, id));
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

        Set<String> comments = SchnitzelEvent.getFoundSchnitzel(bp);

        if (!comments.contains(id + "")) {
            Stats.incrementStat(Stats.BiomiaStat.SchnitzelFound, bp.getBiomiaPlayerID(), id + "");
            comments.add(id + "");
            bp.sendMessage(Messages.PREFIX + "§cDu hast das Schnitzel §b" + id + " §cgefunden!");
        } else {
            return;
        }

        if (comments.size() == SchnitzelEvent.getSchnitzel()) {
            Date date = Stats.getFirstIncrementDate(Stats.BiomiaStat.SchnitzelFound, bp);
            String text = Time.toText((int) (Calendar.getInstance().getTime().getTime() - date.getTime()) / 1000);
            Bukkit.broadcastMessage("§c" + bp.getName() + " §bhat alle §c" + SchnitzelEvent.getSchnitzel() + " §bSchnitzel in §c" + text + " §bgefunden!");
        }
    }

    public ItemStack getItem() {
        return is;
    }

    public int getID() {
        return id;
    }

    public int getSlot() {
        return id - 1;
    }
}
