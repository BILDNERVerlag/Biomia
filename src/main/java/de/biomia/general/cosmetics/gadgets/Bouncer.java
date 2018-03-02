package de.biomia.general.cosmetics.gadgets;

import de.biomia.BiomiaPlayer;
import de.biomia.general.cosmetics.items.CosmeticGadgetItem;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class Bouncer implements GadgetListener {

    @Override
    public void execute(BiomiaPlayer bp, CosmeticGadgetItem item) {
        Player p = bp.getPlayer();
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 30, 30, false, false));
        item.removeOne(bp, true);
    }
}
