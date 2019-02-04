package de.biomia.spigot.quests;

import de.biomia.universal.MySQL;
import de.biomia.universal.Time;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

@SuppressWarnings("all")
abstract class Quest {

    //TODO: neues questsystem weiterentwickeln

    /*
    aktive Spieler und Spieler die die Quest abgeschlossen haben in
    je einer tabelle speichern
     */

    @Getter
    private final ArrayList<Entity> npcs = new ArrayList<>();

    private int questID;
    private Band band;
    private String questName;
    private String displayName;
    private String infoText;
    @Setter
    @Getter(value = AccessLevel.PROTECTED)
    private DialogNode startNode;
    private int cooldown;
    @Setter
    private boolean repeatable;

    public Quest(Band band, String questName) {
        this.band = band;
        this.questName = questName;
        registerQuest();
        QuestListener.registerQuest(this);
    }

    public Entity registerNpc(String name, EntityType type, double x, double y, double z) {
        return registerNpc(name, type, new Location(Bukkit.getWorld("Quests"), x, y, z));
    }

    public Entity registerNpc(String name, EntityType type, Location loc) {
        Entity entity = loc.getWorld().spawnEntity(loc, type);
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);
        ((LivingEntity) entity).setAI(false);
        npcs.add(entity);
        return entity;
    }

    public void setCooldown(int cooldown, Time time) {
        this.cooldown = cooldown * time.getSekunden();
    }

    private void registerQuest() {
        //TODO make new (better) table
        questID = MySQL.executeQuerygetint(String.format("SELECT id from `Quests` where name = '%s'", questName),
                "id", MySQL.Databases.quests_db);
        if (questID == -1) /*quest not in database*/ {
            questID = MySQL.executeUpdate(
                    String.format("INSERT INTO `Quests` (name, band) values ('%s', %s)", questName, band), MySQL.Databases.quests_db);
        }
    }
}
