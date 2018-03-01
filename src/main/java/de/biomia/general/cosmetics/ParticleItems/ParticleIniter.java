package de.biomia.general.cosmetics.ParticleItems;

import de.biomia.general.cosmetics.Cosmetic;

public class ParticleIniter {

	public static void init() {
		Cosmetic.addParticleListener(86, new Spur());
	}

}
