package de.biomia.general.cosmetics.items;

import de.biomia.BiomiaPlayer;
import de.biomia.general.cosmetics.Cosmetic;
import de.biomia.general.cosmetics.Cosmetic.Group;
import de.biomia.general.cosmetics.particles.ParticleListener;
import de.biomia.server.quests.QuestEvents.TakeItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

@SuppressWarnings("StatementWithEmptyBody")
public class CosmeticParticleItem extends CosmeticItem {

    private static final HashMap<BiomiaPlayer, BukkitTask> actives = new HashMap<>();
    private final ParticleListener particleListener = Cosmetic.getParticleListener(getID());

    @Override
    public void use(BiomiaPlayer bp) {
        super.use(bp);
        bp.getPlayer().sendMessage(getName() + " \u00A78wurde \u00A7aAktiviert\u00A78!");
        if (CosmeticParticleItem.actives.containsKey(bp)) {
            CosmeticParticleItem.actives.get(bp).cancel();
        }
        CosmeticParticleItem.actives.put(bp, particleListener.start(bp, this));
    }

    @Override
    public void remove(BiomiaPlayer bp) {
        bp.getPlayer().sendMessage(getName() + " \u00A78wurde \u00A7cDeaktiviert\u00A78!");
        if (CosmeticParticleItem.actives.containsKey(bp)) {
            CosmeticParticleItem.actives.get(bp).cancel();
            CosmeticParticleItem.actives.remove(bp);
        }
    }

    public CosmeticParticleItem(int id, String name, ItemStack is, Commonness c) {
        super(id, name, is, c, Group.PARTICLES);
    }

    public boolean removeTime(BiomiaPlayer bp, int time) {
        int limit = Cosmetic.getLimit(bp, getID());

        if (limit == -1) {
            // 'Infinity'
        } else if (limit - time <= 0) {
            Cosmetic.setLimit(bp, getID(), 0);
            remove(bp);
            return true;
        } else {
            Cosmetic.setLimit(bp, getID(), limit - time);
            removeItemFromInventory(bp, time);
        }
        return false;
    }

    private void removeItemFromInventory(BiomiaPlayer bp, int menge) {
        new TakeItemEvent(getItem().getType(), getItem().getItemMeta().getDisplayName(), 1)
                .executeEvent(bp.getQuestPlayer());
    }
}