package de.biomia.quests.general;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.mysql.MySQL;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Quest {

    final ArrayList<NPC> npcs = new ArrayList<>();
    final ArrayList<Integer> active_Player_biomiaIDs = getActivePlayerBiomiaIDs();
    final HashMap<States, DialogMessage> dialog = new HashMap<>();

    private String questName;
    private String displayName;
    private String infoText;
    private int questid;
    private int cooldown;
    private int band;
    private boolean repeatable;
    private boolean removeOnReload;
    private boolean playableWithParty;

    //CONSTRUCTOR
    public Quest(String questName, int bandNummer) {
        this.questName = questName;
        this.displayName = questName;
        this.band = bandNummer;
        this.infoText = null;
        this.questid = -1;
    }

    //METHODS
    public NPC createNPC(EntityType entity, String name) {
        NPC temp = CitizensAPI.getNPCRegistry().createNPC(entity, name);

        temp.addTrait(CitizensAPI.getTraitFactory().getTraitClass("lookclose"));
        temp.data().set("lookclose", true);
        npcs.add(temp);
        return temp;
    }

    public void addPlayer(QuestPlayer qp) {
        Objects.requireNonNull(active_Player_biomiaIDs).add(qp.getBiomiaPlayer().getBiomiaPlayerID());
    }

    public void removePlayer(QuestPlayer qp) {
        active_Player_biomiaIDs.remove(new Integer(qp.getBiomiaPlayer().getBiomiaPlayerID()));
    }

    public void registerQuestIfnotExist() {
        if (MySQL.executeQuery("SELECT name from `Quests` where name = '" + questName + "'",
                "name", MySQL.Databases.quests_db) == null) {
            MySQL.executeUpdate(
                    "INSERT INTO `Quests` (name, band) values ('" + questName + "', " + band + ")", MySQL.Databases.quests_db);
        }
        questid = MySQL.executeQuerygetint(
                "SELECT name, id from `Quests` where name = '" + questName + "'", "id", MySQL.Databases.quests_db);
    }


    // GETTERS AND SETTERS
    public HashMap<States, DialogMessage> getDialogs() {
        return dialog;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean b) {
        this.repeatable = b;
    }

    public void setDisplayName(String s) {
        displayName = s;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setInfoText(String s) {
        infoText = s;
    }

    public String getInfoText() {
        return infoText;
    }

    public List<QuestPlayer> getActiveOnlinePlayers() {

        ArrayList<QuestPlayer> onlinePlayers = new ArrayList<>();

        for (Integer i : active_Player_biomiaIDs) {

            String name = BiomiaPlayer.getName(i);

            Player p = Bukkit.getPlayer(name);

            if (p != null) {
                onlinePlayers.add(Biomia.getBiomiaPlayer(p).getQuestPlayer());
            }
        }

        return onlinePlayers;
    }

    public int getQuestID() {

        return questid;
    }

    public DialogMessage getDialog(States stat) {
        return dialog.get(stat);
    }

    public void setDialog(DialogMessage dialogpl, States stat) {
        dialog.put(stat, dialogpl);
    }

    public ArrayList<Integer> getActivePlayerBiomiaIDs() {

        ArrayList<Integer> listOfIDs = new ArrayList<>();

        if (active_Player_biomiaIDs == null) {

            Connection con = MySQL.Connect(MySQL.Databases.quests_db);

            if (con != null) {
                try {
                    ResultSet s = con
                            .prepareStatement(
                                    "SELECT * FROM `Quests_aktuell` WHERE questID = '" + questid + "'")
                            .executeQuery();

                    while (s.next()) {
                        listOfIDs.add(s.getInt("biomiaID"));
                    }
                    return listOfIDs;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            return active_Player_biomiaIDs;
        }

        return listOfIDs;
    }

    public boolean getRemoveOnReload() {
        return removeOnReload;
    }

    public void setRemoveOnReload(boolean b) {
        this.removeOnReload = b;
    }

    public int getBand() {
        return band;
    }

    public boolean getPlayableWithParty() {
        return playableWithParty;
    }

    public void setPlayableWithParty(boolean b) {
        this.playableWithParty = b;
    }

    public void setCooldown(int cooldown, TIME t) {
        switch (t) {
            case SEKUNDEN:
                this.cooldown = cooldown;
                break;
            case MINUTEN:
                this.cooldown = cooldown * 60;
                break;
            case STUNDEN:
                this.cooldown = cooldown * 60 * 60;
                break;
            case TAGE:
                this.cooldown = cooldown * 60 * 60 * 24;
                break;
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public ArrayList<Integer> getNpcIDs() {
        ArrayList<Integer> temp = new ArrayList<>();
        for (NPC npc : npcs) {
            temp.add(npc.getId());
        }
        return temp;
    }

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName0) {
        questName = questName0;
    }
}

