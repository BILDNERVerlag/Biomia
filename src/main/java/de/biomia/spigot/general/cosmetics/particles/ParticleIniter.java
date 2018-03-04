package de.biomia.spigot.general.cosmetics.particles;

import de.biomia.spigot.general.cosmetics.Cosmetic;

public class ParticleIniter {

    public static void init() {
        Cosmetic.addParticleListener(86, new Spur());
    }

}
