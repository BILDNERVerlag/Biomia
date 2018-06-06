package de.biomia.spigot.quests;

import de.biomia.spigot.Biomia;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

class QuestListener implements Listener {

    private static final ArrayList<Quest> quests = new ArrayList<>();

    @EventHandler
    public static void onInteract(PlayerInteractEntityEvent e) {
        Quest q = quests.stream().filter(quest -> quest.getNpcs().contains(e.getRightClicked())).findFirst().orElse(null);
        if (q == null) return;
        q.getStartNode().execute(Biomia.getBiomiaPlayer(e.getPlayer()));
        e.setCancelled(true);
    }

    protected static void registerQuest(Quest q) {
        quests.add(q);
    }

    @EventHandler
    public static void onDamage(EntityDamageEvent e) {
        if (quests.stream().anyMatch(quest -> quest.getNpcs().contains(e.getEntity()))) e.setCancelled(true);
    }

    @EventHandler
    public static void onMove(PlayerMoveEvent e) {
        quests.forEach(quest -> quest.getNpcs().stream()
                .filter(entity -> e.getPlayer().getLocation().distance(entity.getLocation()) < 6)
                .forEach(entity -> entity.teleport(entity.getLocation().setDirection( e.getPlayer().getLocation().subtract(entity.getLocation()).toVector()))));
    }

}