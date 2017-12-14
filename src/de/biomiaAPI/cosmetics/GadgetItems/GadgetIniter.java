package de.biomiaAPI.cosmetics.GadgetItems;

import de.biomiaAPI.cosmetics.Cosmetic;

public class GadgetIniter {

	public static void init() {
		Cosmetic.addGagetListener(0, new PaintballGun());
	}

}
