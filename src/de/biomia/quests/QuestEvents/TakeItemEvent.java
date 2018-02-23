package de.biomia.quests.QuestEvents;

import de.biomia.quests.general.QuestPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TakeItemEvent implements Event {

    private QuestPlayer qp = null;
    private final Material material;
    private String name = null;
    private final int menge;

    public TakeItemEvent(Material material, String name, int menge) {
        this.material = material;
        this.menge = menge;
        this.name = name;
    }

    public TakeItemEvent(Material material, int menge) {
        this.material = material;
        this.menge = menge;
    }

    @Override
    public void executeEvent(QuestPlayer qp) {
        this.qp = qp;
        takeItem();
    }

    private void takeItem() {

        int i = 0;
        for (ItemStack is : qp.getPlayer().getInventory().getContents()) {
            if (is != null)
                if (is.getType() == material) {
                    if (name != null)
                        if (!is.getItemMeta().getDisplayName().equals(name))
                            continue;
                    if (is.getAmount() >= menge || is.getAmount() >= menge - i) {
                        is.setAmount(is.getAmount() - (menge - i));
                        return;
                    } else {
                        i += is.getAmount();
                        is.setAmount(0);
                    }
                }
        }
    }
}
