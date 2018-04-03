package de.biomia.spigot.server.quests.QuestEvents;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import org.bukkit.inventory.ItemStack;

public class GiveItemIfNotInInventoryEvent implements Event {

    private BiomiaPlayer qp;

    private final ItemStack stack;

    public GiveItemIfNotInInventoryEvent(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void executeEvent(BiomiaPlayer qp) {
        this.qp = qp;
        giveItem();
    }

    private void giveItem() {
        if (!ItemConditions.hasItemInInventory(qp.getQuestPlayer(), stack.getType(), stack.getAmount(), stack.getItemMeta().getDisplayName())) {
            qp.getPlayer().getInventory().addItem(stack);
        }
    }

}
