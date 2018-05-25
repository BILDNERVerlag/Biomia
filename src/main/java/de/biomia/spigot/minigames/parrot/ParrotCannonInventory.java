package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

class ParrotCannonInventory {

    protected static final HashMap<BiomiaPlayer, ParrotCannonInventory> openInventories = new HashMap<>();

    private final ParrotCannon cannon;
    private final Inventory inventory;

    ParrotCannonInventory(ParrotCannon cannon, int size, String name) {
        inventory = Bukkit.createInventory(null, size, name);
        this.cannon = cannon;
    }

    public void open(BiomiaPlayer bp) {
        openInventories.put(bp, this);
        bp.getPlayer().openInventory(inventory);
    }

    protected ParrotCannon getCannon() {
        return cannon;
    }

    protected void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    static class CannonSettingInventory extends ParrotCannonInventory {

        CannonSettingInventory(ParrotCannon cannon) {
            super(cannon, 9, "Einstellungen");
            setItem(2, ItemCreator.itemCreate(Material.BARRIER, "faster reload"));
            setItem(3, ItemCreator.itemCreate(Material.BARRIER, "more damage"));
            setItem(4, ItemCreator.itemCreate(Material.COMPASS));
            setItem(5, ItemCreator.itemCreate(Material.BARRIER, "more scattering"));
            setItem(6, ItemCreator.itemCreate(Material.BARRIER, "more bullets"));
        }
    }

    static class CannonDirectionSettingInventory extends ParrotCannonInventory {

        boolean first = true;

        CannonDirectionSettingInventory(ParrotCannon cannon) {
            super(cannon, 27, "Ausrichtung ändern");
        }

        @Override
        public void open(BiomiaPlayer bp) {
            if (first) {
                first = false;
                init();
            }
            super.open(bp);
        }

        protected void init() {
            setItem(10, ItemCreator.itemCreate(Material.WOOL, (short) 4));
            setItem(11, ItemCreator.itemCreate(Material.WOOL, (short) 3));
            setItem(12, ItemCreator.itemCreate(Material.WOOL, (short) 2));
            setItem(13, ItemCreator.itemCreate(Material.WOOL, (short) 3));
            setItem(14, ItemCreator.itemCreate(Material.WOOL, (short) 4));

            setItem(7, ItemCreator.itemCreate(Material.WOOL, (short) 4));
            setItem(16, ItemCreator.itemCreate(Material.WOOL, (short) 3));
            setItem(25, ItemCreator.itemCreate(Material.WOOL, (short) 2));

            setItem(getCannon().getActualPitch().getSlot(), ItemCreator.itemCreate(Material.WOOL, (short) 5));
            setItem(getCannon().getActualYaw().getSlot(), ItemCreator.itemCreate(Material.WOOL, (short) 5));
        }
    }

    static class CannonMainInventory extends ParrotCannonInventory {
        CannonMainInventory(ParrotCannon cannon) {
            super(cannon, 9, "Kanonier");
            setItem(2, ItemCreator.itemCreate(Material.REDSTONE));
            setItem(6, ItemCreator.itemCreate(Material.BOW));
        }
    }

    static class CannonWeaponChangeInventory extends ParrotCannonInventory {

        CannonWeaponChangeInventory(ParrotCannon cannon) {
            super(cannon, 9, "Weapon Change");
            setItem(2, ItemCreator.itemCreate(Material.IRON_SWORD, ""));
            setItem(3, ItemCreator.itemCreate(Material.IRON_SWORD));
            setItem(4, ItemCreator.itemCreate(Material.IRON_SWORD));
            setItem(5, ItemCreator.itemCreate(Material.IRON_SWORD));
            setItem(6, ItemCreator.itemCreate(Material.IRON_SWORD));
        }
    }
}
