package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.achievements.BiomiaStat;
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

public class Schnitzel {

    public final static String name = "00A7cSchnitzel 00A77#00A7b%s";

    private ItemStack is;
    private Location loc;
    private final int id;

    private static int schnitzelCount = 1;

    Schnitzel() {
        this.id = schnitzelCount++;
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
        if (!loc.getChunk().isLoaded())
            loc.getChunk().load();

        Bukkit.getWorld("BiomiaWelt").dropItemNaturally(loc, is).setPickupDelay(0);
    }

    public void pickUp(BiomiaPlayer bp) {

        ArrayList<String> comments = SchnitzelEvent.getFoundSchnitzel(bp);

        if (!comments.contains(id + "")) {
            comments.add(id + "");
            BiomiaStat.SchnitzelFound.increment(bp.getBiomiaPlayerID(), 1, id + "");
            bp.sendMessage(Messages.PREFIX + "00A7cDu hast " + getItem().getItemMeta().getDisplayName() + " 00A7cgefunden!");
            bp.addCoins(50, false);
            if (comments.size() == SchnitzelEvent.getSchnitzel()) {
                Date date = BiomiaStat.SchnitzelFound.getFirstIncrementDate(bp);
                int duration = (int) ((System.currentTimeMillis() - date.getTime()) / 1000);
                SchnitzelEvent.schnitzelHighScore.put(bp.getName(), duration);
                SchnitzelEvent.reloadSBSchnitzel();
                Bukkit.broadcastMessage("00A7c" + bp.getName() + " 00A7bhat alle 00A7c" + SchnitzelEvent.getSchnitzel() + " 00A7bSchnitzel in 00A7c" + Time.toText(duration) + " 00A7bgefunden!");

                if (SchnitzelEvent.getFirstName(SchnitzelEvent.schnitzelHighScore).equals(bp.getName())) {
                    Bukkit.broadcastMessage("00A7c" + bp.getName() + " 00A7bhat den Highscore gebrochen!");
                }
            }
            if (id == 17) {
                bp.getPlayer().teleport(new Location(Bukkit.getWorld("BiomiaWelt"), 370.5, 23, 439.5, -90, 0));
            }
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

/*
    Schnitzel Locations:


 */