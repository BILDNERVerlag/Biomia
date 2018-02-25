package de.biomia.api.cosmetics.ParticleItems;

import de.biomia.api.cosmetics.Cosmetic;

public class ParticleIniter {

	public static void init() {
		Cosmetic.addParticleListener(86, new Spur());
	}

}
