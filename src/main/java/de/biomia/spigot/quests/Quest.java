package de.biomia.spigot.quests;

import de.biomia.spigot.server.quests.general.DialogMessage;
import de.biomia.universal.MySQL;
import de.biomia.universal.Time;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;

abstract class Quest {

    //TODO: neues questsystem weiterentwickeln

    /*
    aktive Spieler und Spieler die die Quest abgeschlossen haben in
    je einer tabelle speichern
     */

    private final ArrayList<NPC> npcs = new ArrayList<>();

    private int questID;
    private Band band;
    private String questName;
    private String displayName;
    private String infoText;

    private int cooldown;
    private boolean repeatable;

    private HashMap<NPC, ArrayList<DialogMessage>> dialoge;

    public Quest(Band band, String questName) {
        registerQuest();
    }

    public void registerNpc(String name, EntityType type, Location loc) {
        NPC temp = CitizensAPI.getNPCRegistry().createNPC(type, name);

        //TODO renew lookclose
        temp.addTrait(CitizensAPI.getTraitFactory().getTraitClass("lookclose"));
        temp.data().set("lookclose", true);
        temp.spawn(loc);
        npcs.add(temp);
    }

    public void registerNpc(NPC npc) {
        npcs.add(npc);
    }

    public void setCooldown(int cooldown, Time time) {
        this.cooldown = cooldown * time.getSekunden();
    }

    private void registerQuest() {
        //TODO rename | should quests be reset?
        questID = MySQL.executeQuerygetint("SELECT id from `Quests` where name = '" + questName + "'",
                "id", MySQL.Databases.quests_db);
        if (questID == -1) {
            MySQL.executeUpdate(
                    "INSERT INTO `Quests` (name, band) values ('" + questName + "', " + band + ")", MySQL.Databases.quests_db);
            questID = MySQL.executeQuerygetint(
                    "SELECT name, id from `Quests` where name = '" + questName + "'", "id", MySQL.Databases.quests_db);
        }
        // else: Quest already in database
    }

    //GETTER and SETTER

    public void setActiveNpc(NPC npc) {
        npcs.add(npcs.get(0));
        npcs.set(0, npc);
    }

    public NPC getActiveNPC() {
        return npcs.get(0);
    }

    public void setRepeatable() {
        this.repeatable = true;
    }
}

class QuestListener implements Listener {

    private static final ArrayList<Quest> quests = new ArrayList<>();

    @EventHandler
    public static void onInteract(NPCRightClickEvent e) {

    }

    public static void registerQuest(Quest q) {
        quests.add(q);
    }
}

enum Band {
    Band1, Band2
}
