package de.biomiaAPI.achievements.statEvents.cosmetics;

import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.achievements.statEvents.BiomiaPlayerEvent;
import de.biomiaAPI.cosmetics.Cosmetic;
import de.biomiaAPI.cosmetics.CosmeticItem;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class CosmeticUsedEvent extends BiomiaPlayerEvent {

    private static final HandlerList list = new HandlerList();

    private final Cosmetic.Group group;
    private final String name;
    private final ItemStack itemStack;
    private final CosmeticItem.Commonness commonness;
    private final int id;

    public CosmeticUsedEvent(BiomiaPlayer bp, Cosmetic.Group group, String name, ItemStack itemStack, CosmeticItem.Commonness commonness, int id) {
        super(bp);
        this.group = group;
        this.name = name;
        this.itemStack = itemStack;
        this.commonness = commonness;
        this.id = id;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public Cosmetic.Group getGroup() {
        return group;
    }

}
