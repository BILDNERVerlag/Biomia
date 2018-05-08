package de.biomia.spigot.general.cosmetics;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.achievements.BiomiaStat;
import de.biomia.spigot.general.cosmetics.items.CosmeticGadgetItem;
import de.biomia.spigot.general.cosmetics.items.CosmeticItem;
import de.biomia.spigot.general.cosmetics.items.CosmeticItem.Commonness;
import de.biomia.spigot.general.cosmetics.items.CosmeticParticleItem;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class MysteryChest {

    private static Commonness determineCommonness() {
        // Prozentchancen:
        // VERY_COMMON: 60
        // COMMON: 30
        // RARE: 9
        // VERY_RARE: 1

        int random = new Random().nextInt(100) + 1;
        Commonness c;

        if (random > 40)
            c = Commonness.VERY_COMMON;
        else if (random > 10)
            c = Commonness.COMMON;
        else if (random > 2)
            c = Commonness.RARE;
        else
            c = Commonness.VERY_RARE;

        return c;
    }

    public static void open(BiomiaPlayer bp) {

        Commonness c;
        ArrayList<CosmeticItem> items;

        do {
            c = determineCommonness();
            items = Cosmetic.getItemsOfCommonness(c);
        } while (items.size() == 0);

        int i = new Random().nextInt(items.size());
        CosmeticItem item = items.get(i);

        Inventory inv = Bukkit.createInventory(null, 27, "\u00A74Mysteriöse Box \u00A78- \u00A74Dein Gewinn:");
        ItemStack itemStack = item.getItem();

        int menge = -1;
        if (item instanceof CosmeticGadgetItem)
            switch (new Random().nextInt(6)) {
                case 0:
                case 1:
                    menge = 10;
                    break;
                case 2:
                    menge = 15;
                    break;
                case 3:
                    menge = 20;
                    break;
                case 4:
                    menge = 25;
                    break;
                case 5:
                    menge = 30;
                    break;
            }
        else if (item instanceof CosmeticParticleItem)
            switch (new Random().nextInt(6)) {
                case 0:
                    menge = 60;
                    break;
                case 1:
                    menge = 100;
                    break;
                case 2:
                    menge = 150;
                    break;
                case 3:
                    menge = 180;
                    break;
                case 4:
                    menge = 200;
                    break;
                case 5:
                    menge = 240;
                    break;
            }
        Cosmetic.setLimit(bp, item.getID(), menge);
        if (menge != -1)
            itemStack.setAmount(menge);

        ItemStack is = ItemCreator.itemCreate(Material.BLACK_GLAZED_TERRACOTTA);
        int filler = 0;
        while (filler < 27) {
            inv.setItem(filler, is);
            filler++;
        }

        bp.getPlayer().openInventory(inv);
        BiomiaStat.MysteryChestsOpened.increment(bp.getBiomiaPlayerID(), 1, null);

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {

                switch (counter) {
                    case 13:
                        inv.setItem(counter, itemStack);
                        break;
                    case 27:
                        cancel();
                        return;
                    default:
                        inv.setItem(counter, null);
                        break;
                }
                counter++;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 2);
    }
}
