package de.biomia.spigot.server.quests.QuestEvents;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GiveItemEvent implements Event {

    private BiomiaPlayer qp;
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
    public void executeEvent(BiomiaPlayer qp) {
        this.qp = qp;
        giveItem();
    }

    private void giveItem() {
        int count = 0;
        for (ItemStack i : qp.getPlayer().getInventory())
            if (i == null) count++;
        if (stack == null)
            if (name != null) stack = ItemCreator.setAmount(ItemCreator.itemCreate(material, name), menge);
            else stack = ItemCreator.setAmount(ItemCreator.itemCreate(material), menge);
        if (count == 0) qp.getPlayer().getWorld().dropItem(qp.getPlayer().getLocation(), stack);
        else qp.getPlayer().getInventory().addItem(stack);
    }

}
