package de.biomia.api.cosmetics;

import de.biomia.api.BiomiaPlayer;

public interface GadgetListener {

	void execute(BiomiaPlayer bp, CosmeticGadgetItem item);
	
}
