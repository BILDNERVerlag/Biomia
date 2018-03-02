package de.biomia.general.cosmetics.gadgets;

import de.biomia.BiomiaPlayer;
import de.biomia.general.cosmetics.items.CosmeticGadgetItem;

public interface GadgetListener {

    void execute(BiomiaPlayer bp, CosmeticGadgetItem item);

}
