package de.biomiaAPI.cosmetics.ParticleItems;

import de.biomiaAPI.cosmetics.Cosmetic;

public class ParticleIniter {

	public static void init() {
		Cosmetic.addParticleListener(86, new Spur());
	}

}
