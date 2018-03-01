package de.biomia.general.cosmetics.GadgetItems;

import de.biomia.Main;
import de.biomia.general.cosmetics.Cosmetic;
import org.bukkit.Bukkit;

public class GadgetIniter {

	public static void init() {
		Cosmetic.addGagetListener(61, new PaintballGun());
		Cosmetic.addGagetListener(63, new Booster());
		Cosmetic.addGagetListener(64, new Bouncer());
		Cosmetic.addGagetListener(65, new CrazyCat());
		Cosmetic.addGagetListener(67, new FastDayCircle());
		Cosmetic.addGagetListener(68, new Firework());
		Cosmetic.addGagetListener(69, new Freezer());
		Cosmetic.addGagetListener(70, new Invisible());
		Cosmetic.addGagetListener(71, new Levitator());
		Cosmetic.addGagetListener(72, new Suizid());
		Switcher s = new Switcher();
        Bukkit.getPluginManager().registerEvents(s, Main.getPlugin());
		Cosmetic.addGagetListener(73, s);
		Cosmetic.addGagetListener(74, new TNTCanon());
		Cosmetic.addGagetListener(75, new Witch());
	}

}
