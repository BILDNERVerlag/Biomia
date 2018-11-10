package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

class ParrotCannonInventory {

    static final HashMap<BiomiaPlayer, ParrotCannonInventory> openInventories = new HashMap<>();

    private final ParrotCannon cannon;
    private final Inventory inventory;

    ParrotCannonInventory(ParrotCannon cannon, int size, String name) {
        inventory = Bukkit.createInventory(null, size, name);
        this.cannon = cannon;
    }

    void open(BiomiaPlayer bp) {
        openInventories.put(bp, this);
        bp.getPlayer().openInventory(inventory);
    }

    ParrotCannon getCannon() {
        return cannon;
    }

    void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    static class CannonSettingInventory extends ParrotCannonInventory {

        CannonSettingInventory(ParrotCannon cannon) {
            super(cannon, 9, "Einstellungen");
            setItem(2, ItemCreator.setLore(ItemCreator.itemCreate(Material.FIREBALL, Messages.format("Schneller neuladen")), ParrotCannon.CannonUpgrade.FAST_RELOAD.getLore()));
            setItem(3, ItemCreator.setLore(ItemCreator.itemCreate(Material.GOLD_SWORD, Messages.format("Mehr Schaden")), ParrotCannon.CannonUpgrade.DAMAGE.getLore()));
            setItem(4, ItemCreator.setLore(ItemCreator.itemCreate(Material.COMPASS, Messages.format("Richtung wählen")), ParrotCannon.CannonUpgrade.DIRECTION.getLore()));
            setItem(5, ItemCreator.setLore(ItemCreator.itemCreate(Material.LONG_GRASS, Messages.format("Mehr Streuung")), ParrotCannon.CannonUpgrade.SCATTERING.getLore()));
            setItem(6, ItemCreator.setLore(ItemCreator.itemCreate(Material.STONE_BUTTON, Messages.format("Mehr Schuss")), ParrotCannon.CannonUpgrade.BULLET.getLore()));
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

        void init() {
            setItem(10, ItemCreator.itemCreate(Material.WOOL, "Stark Links", (short) 14));
            setItem(11, ItemCreator.itemCreate(Material.WOOL, "Links", (short) 1));
            setItem(12, ItemCreator.itemCreate(Material.WOOL, "Mittig", (short) 4));
            setItem(13, ItemCreator.itemCreate(Material.WOOL, "Rechts", (short) 1));
            setItem(14, ItemCreator.itemCreate(Material.WOOL, "Stark Rechts", (short) 14));

            setItem(7, ItemCreator.itemCreate(Material.WOOL, "Lang", (short) 14));
            setItem(16, ItemCreator.itemCreate(Material.WOOL, "Mittel", (short) 1));
            setItem(25, ItemCreator.itemCreate(Material.WOOL, "Kurz", (short) 4));

            setItem(getCannon().getActualPitch().getSlot(), ItemCreator.itemCreate(Material.WOOL, getCannon().getActualPitch().getName(), (short) 5));
            setItem(getCannon().getActualYaw().getSlot(), ItemCreator.itemCreate(Material.WOOL, getCannon().getActualYaw().getName(), (short) 5));
        }
    }

    static class CannonMainInventory extends ParrotCannonInventory {
        CannonMainInventory(ParrotCannon cannon) {
            super(cannon, 9, "Kanonier");
            setItem(2, ItemCreator.itemCreate(Material.REDSTONE, "Einstellungen"));
            setItem(6, ItemCreator.itemCreate(Material.BOW, "Kanone ändern"));
        }
    }

    static class CannonWeaponChangeInventory extends ParrotCannonInventory {
        CannonWeaponChangeInventory(ParrotCannon cannon) {
            super(cannon, 9, "Weapon Change");
            setItem(2, ItemCreator.itemCreate(Material.WOOD_SWORD, ParrotCannon.CannonType.KANONE.getName()));
            setItem(3, ItemCreator.itemCreate(Material.STONE_SWORD, ParrotCannon.CannonType.GRANATENWERFER.getName()));
            setItem(4, ItemCreator.itemCreate(Material.GOLD_SWORD, ParrotCannon.CannonType.PANZERFAUST.getName()));
            setItem(5, ItemCreator.itemCreate(Material.IRON_SWORD, ParrotCannon.CannonType.HALBAUTOMATIK.getName()));
            setItem(6, ItemCreator.itemCreate(Material.DIAMOND_SWORD, ParrotCannon.CannonType.SCHROTFLINTE.getName()));
        }
    }
}
