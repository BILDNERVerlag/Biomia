package de.biomia.spigot.quests;

import de.biomia.spigot.server.quests.general.DialogMessage;
import de.biomia.universal.MySQL;
import de.biomia.universal.Time;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("all")
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

    public void registerNpc(String name, EntityType type, double x, double y, double z) {
        registerNpc(name, type, new Location(Bukkit.getWorld("Quests"), x, y, z));
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
        //TODO rename
        questID = MySQL.executeQuerygetint(String.format("SELECT id from `Quests` where name = '%s'", questName),
                "id", MySQL.Databases.quests_db);
        if (questID == -1) /*quest not in database*/ {
            //TODO: change to one query
            MySQL.executeUpdate(
                    String.format("INSERT INTO `Quests` (name, band) values ('%s', %s)", questName, band), MySQL.Databases.quests_db);
            questID = MySQL.executeQuerygetint(
                    String.format("SELECT name, id from `Quests` where name = '%s'", questName), "id", MySQL.Databases.quests_db);
        }
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

enum Band {
    Band1, Band2
}
