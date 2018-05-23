package de.biomia.spigot.minigames.parrot;

import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ParrotCanonInventory extends CraftInventoryCustom {

    private final ParrotCanon canon;

    ParrotCanonInventory(ParrotCanon canon, int size, String name) {
        super(null, size, name);
        this.canon = canon;
    }

    public ParrotCanon getCanon() {
        return canon;
    }

    public static class CannonSettingInventory extends ParrotCanonInventory {

        CannonSettingInventory(ParrotCanon canon) {
            super(canon, 9, "Einstellungen");
            setItem(2, ItemCreator.itemCreate(Material.BARRIER));
            setItem(3, ItemCreator.itemCreate(Material.BARRIER));
            setItem(4, ItemCreator.itemCreate(Material.COMPASS));
            setItem(5, ItemCreator.itemCreate(Material.BARRIER));
            setItem(6, ItemCreator.itemCreate(Material.BARRIER));
        }
    }

    public static class CannonDirectionSettingInventory extends ParrotCanonInventory {

        CannonDirectionSettingInventory(ParrotCanon canon) {
            super(canon, 27, "Richtung ändern");
            setItem(2, ItemCreator.itemCreate(Material.BARRIER));
            setItem(3, ItemCreator.itemCreate(Material.BARRIER));
            setItem(4, ItemCreator.itemCreate(Material.COMPASS));
            setItem(5, ItemCreator.itemCreate(Material.BARRIER));
            setItem(6, ItemCreator.itemCreate(Material.BARRIER));
        }
    }

    public static class CannonMainInventory extends ParrotCanonInventory {

        CannonMainInventory(ParrotCanon canon) {
            super(canon, 9, "Kanonier");
            setItem(2, ItemCreator.itemCreate(Material.IRON_SWORD));
            setItem(6, ItemCreator.itemCreate(Material.IRON_SWORD));
        }
    }

    public static class CannonWeaponChangeInventory extends ParrotCanonInventory {

        CannonWeaponChangeInventory(ParrotCanon canon) {
            super(canon, 9, "Weapon Change");
            setItem(2, ItemCreator.itemCreate(Material.IRON_SWORD));
            setItem(3, ItemCreator.itemCreate(Material.IRON_SWORD));
            setItem(4, ItemCreator.itemCreate(Material.IRON_SWORD));
            setItem(5, ItemCreator.itemCreate(Material.IRON_SWORD));
            setItem(6, ItemCreator.itemCreate(Material.IRON_SWORD));
        }
    }

    public class CannonInventoryListener implements Listener {

        @EventHandler
        public void onClick(InventoryClickEvent e) {

            if (e.getClickedInventory() instanceof ParrotCanonInventory) {

                ParrotCanon canon = ((ParrotCanonInventory) e.getClickedInventory()).getCanon();

                if (e.getClickedInventory() instanceof CannonDirectionSettingInventory) {

                } else if (e.getClickedInventory() instanceof CannonMainInventory) {

                } else {
                    switch (e.getSlot()) {
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                        default:
                    }
                }
            }
        }
    }
}
