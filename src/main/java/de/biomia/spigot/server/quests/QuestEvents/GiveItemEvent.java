package de.biomia.spigot.server.quests.QuestEvents;

import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GiveItemEvent implements Event {

    private QuestPlayer qp;
    private Material material;
    private String name;
    private int menge;

    private ItemStack stack;

    public GiveItemEvent(Material material, String name, int menge) {
        this.material = material;
        this.menge = menge;
        this.name = name;
    }

    public GiveItemEvent(Material material, int menge) {
        this.material = material;
        this.menge = menge;
    }

    public GiveItemEvent(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void executeEvent(QuestPlayer qp) {
        this.qp = qp;
        giveItem();
    }

    private void giveItem() {
        if (stack == null) {
            if (name != null) {
                qp.getPlayer().getInventory()
                        .addItem(ItemCreator.setAmount(ItemCreator.itemCreate(material, name), menge));
            } else {
                qp.getPlayer().getInventory().addItem(ItemCreator.setAmount(ItemCreator.itemCreate(material), menge));
            }
        } else {
            qp.getPlayer().getInventory().addItem(stack);
        }
    }

}
