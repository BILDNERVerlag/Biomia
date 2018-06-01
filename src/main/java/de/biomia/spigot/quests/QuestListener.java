package de.biomia.spigot.quests;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

class QuestListener implements Listener {

    private static final ArrayList<Quest> quests = new ArrayList<>();

    @EventHandler
    public static void onInteract(NPCRightClickEvent e) {

    }

    public static void registerQuest(Quest q) {
        quests.add(q);
    }
}