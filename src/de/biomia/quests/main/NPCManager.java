package de.biomia.quests.main;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.Quests.Quest;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.HashMap;

public class NPCManager {

	private static final HashMap<NPC, Location> npcStartLocations = new HashMap<>();
	
	public static void saveNPCLocations() {
		for (Quest q : Biomia.QuestManager().getQuests()) {
			for (NPC n : q.getNpcs()) {
				npcStartLocations.put(n, n.getStoredLocation());
			}
		}
	}

	public static void restoreNPCLocations() {
		for (Quest q : Biomia.QuestManager().getQuests()) {
			for (NPC n : q.getNpcs()) {
				if (!npcStartLocations.containsKey(n))
					return;
				else n.teleport(npcStartLocations.get(n), TeleportCause.COMMAND);
			}
		}
	}
	
}
