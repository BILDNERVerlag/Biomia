package de.biomia.spigot.general.cosmetics.gadgets;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.general.cosmetics.items.CosmeticGadgetItem;

public interface GadgetListener {

    void execute(BiomiaPlayer bp, CosmeticGadgetItem item);

}
