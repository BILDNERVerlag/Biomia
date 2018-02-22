package de.biomiaAPI.Quests;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class QuestPlayer {

    private final BiomiaPlayer biomiaPlayer;
    private final Player player;
    private ArrayList<Material> mineableBlocks = new ArrayList<>();
    private ArrayList<Material> buildableBlocks = new ArrayList<>();
    private DialogMessage aktuellerDialog;
    private final ItemStack book;

    public QuestPlayer(BiomiaPlayer bp) {
        this.biomiaPlayer = bp;
        this.player = bp.getPlayer();
        book = ItemCreator.itemCreate(Material.WRITTEN_BOOK, "\u00A7cTagebuch");
        for (ItemStack is : player.getInventory().getContents()) {
            try {
                if (is.getType() == Material.WRITTEN_BOOK && is.getItemMeta().getDisplayName().equals("\u00A7cTagebuch")) {
                    return;
                }
            } catch (NullPointerException e) {
                // do nothing
            }
        }
        if (player.getWorld().getName().equals("Quests"))
            player.getInventory().addItem(book);
    }

    public List<Quest> getActiveQuests() {

        ArrayList<Quest> quests = new ArrayList<>();

        Connection con = MySQL.Connect(MySQL.Databases.quests_db);

        if (con != null) {
            try {
                ResultSet s = con
                        .prepareStatement(
                                "SELECT questID FROM `Quests_aktuell` WHERE biomiaID = " + biomiaPlayer.getBiomiaPlayerID())
                        .executeQuery();

                while (s.next()) {
                    quests.add(Biomia.QuestManager().getQuest(s.getInt("questID")));
                }

                // quests.removeIf(Objects::isNull);

                return quests;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public ItemStack getBook() {
        return book;
    }

    public BiomiaPlayer getBiomiaPlayer() {
        return biomiaPlayer;
    }

    public void updateBook() {
        Bukkit.dispatchCommand(player, "qupdatebook");
    }

    public void addToQuest(Quest quest) {

        quest.addPlayer(this);
        MySQL.executeUpdate("INSERT INTO `Quests_aktuell`(`biomiaID`, `questID`, `state`) VALUES ("
                + biomiaPlayer.getBiomiaPlayerID() + "," + quest.getQuestID() + ",'STATUS1')", MySQL.Databases.quests_db);
        updateBook();
    }

    public void rmFromQuest(Quest quest) {

        quest.removePlayer(this);
        MySQL.executeUpdate("DELETE FROM `Quests_aktuell` WHERE biomiaID = " + biomiaPlayer.getBiomiaPlayerID()
                + " AND questID = " + quest.getQuestID(), MySQL.Databases.quests_db);
    }

    /*
     * gibt zurueck, zu wieviel prozent der spieler bereits die quests des
     * entsprechenden bands bearbeitet/abgeschlossen hat
     */
    public int getQuestPercentage(int band) {

        int[] questsProBand = new int[5];
        for (Quest q : Biomia.QuestManager().getQuests()) {
            questsProBand[q.getBand()]++;
        }
        for (int i = 0; i < questsProBand.length; i++) {
            int playerProgress = 0;
            if (questsProBand[i] != 0) {
                for (Quest q : getFinishedQuests()) {
                    if (q == null)
                        continue;
                    if (q.getBand() == i) {
                        playerProgress++;
                    }
                }
                if (i == band)
                    return (int) Math.round((double) playerProgress / (double) questsProBand[i] * 100);
            }
        }

        return -1;

    }

    public void updateState(Quest quest, States state) {

        if (getState(quest) != null)
            MySQL.executeUpdate("UPDATE `Quests_aktuell` SET `state` = '" + state.name() + "' WHERE `biomiaID`= "
                    + biomiaPlayer.getBiomiaPlayerID() + " AND `questID`= " + quest.getQuestID(), MySQL.Databases.quests_db);
    }

    public void addMineableBlock(Material material) {
        mineableBlocks.add(material);
    }

    public void removeMineableBlock(Material material) {
        mineableBlocks.remove(material);
    }

    public void addBuildableBlock(Material material) {
        buildableBlocks.add(material);
    }

    public void removeBuildableBlock(Material material) {
        buildableBlocks.remove(material);
    }

    public void addMineableBlocks(ArrayList<Material> list_of_materials) {
        mineableBlocks.addAll(list_of_materials);
    }

    public void removeMineableBlocks(ArrayList<Material> list_of_materialsm) {
        for (Material i : list_of_materialsm) {
            mineableBlocks.remove(i);
        }
    }

    public ArrayList<Quest> getFinishedQuests() {
        ArrayList<Quest> quests = new ArrayList<>();
        Connection con = MySQL.Connect(MySQL.Databases.quests_db);
        if (con != null) {
            try {
                ResultSet s = con.prepareStatement("SELECT questID FROM `Quests_erledigt` WHERE biomiaID = " + biomiaPlayer.getBiomiaPlayerID()).executeQuery();

                while (s.next()) {
                    quests.add(Biomia.QuestManager().getQuest(s.getInt("questID")));
                }
                } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // quests.removeIf(Objects::isNull);

        return quests;

    }

    public boolean isInQuest(Quest quest) {
        return Objects.requireNonNull(getActiveQuests()).contains(quest);
    }

    public void setDialog(DialogMessage dm) {
        aktuellerDialog = dm;
    }

    public DialogMessage setRandomDialog(ArrayList<DialogMessage> dm) {
        aktuellerDialog = dm.get(new Random().nextInt(dm.size() - 1));
        return aktuellerDialog;
    }

    public DialogMessage getDialog() {
        return this.aktuellerDialog;
    }

    public States getState(Quest q) {
        String s = MySQL.executeQuery("SELECT * FROM `Quests_aktuell` WHERE biomiaID = " + biomiaPlayer.getBiomiaPlayerID() + " AND questID = " + q.getQuestID(), "state", MySQL.Databases.quests_db);
        if (s == null)
            return null;
        else
            return States.valueOf(s);
    }

    public int getNumberOfFinishedQuests() {
        return getFinishedQuests().size();
    }

    public int getNumberOfActiveQuests() {
        return Objects.requireNonNull(getActiveQuests()).size();
    }

    public Player getPlayer() {
        return player;
    }

    public void finish(Quest quest) {
        rmFromQuest(quest);
        MySQL.executeUpdate("INSERT INTO `Quests_erledigt`(`questID`, `biomiaID`, `end_time`) VALUES (" + quest.getQuestID()
                + ", " + biomiaPlayer.getBiomiaPlayerID() + " ," + System.currentTimeMillis() / 1000 + ")", MySQL.Databases.quests_db);
        updateBook();
    }

    private int getFinishTime(Quest quest) {
        if (hasFinished(quest)) {
            return MySQL.executeQuerygetint("Select * from `Quests_erledigt` where `questID` = " + quest.getQuestID()
                    + " AND biomiaID = " + biomiaPlayer.getBiomiaPlayerID(), "end_time", MySQL.Databases.quests_db);
        }
        return -1;
    }

    public boolean checkCooldown(Quest q) {
        if (q.isRepeatble()) {
            if (Biomia.getBiomiaPlayer(getPlayer()).isPremium()) {
                return System.currentTimeMillis() / 1000 >= (getFinishTime(q) + (q.getCooldown() * 0.8));
            } else {
                return System.currentTimeMillis() / 1000 >= (getFinishTime(q) + q.getCooldown());
            }
        }
        return false;
    }

    public boolean hasFinished(Quest quest) {
        return getFinishedQuests().contains(quest);
    }

    public ArrayList<Material> getMineableBlocks() {
        return mineableBlocks;
    }

    public void setMineableBlocks(ArrayList<Material> mineableBlocks) {
        this.mineableBlocks = mineableBlocks;
    }

    public ArrayList<Material> getBuildableBlocks() {
        return buildableBlocks;
    }

    public void setBuildableBlocks(ArrayList<Material> mineableBlocks) {
        this.buildableBlocks = mineableBlocks;
    }

    public void unfinish(Quest quest) {
        MySQL.executeUpdate("Delete from `Quests_erledigt` where `biomiaID` = " + biomiaPlayer.getBiomiaPlayerID()
                + " AND `questID` = '" + quest.getQuestID() + "'", MySQL.Databases.quests_db);

        updateBook();
    }
}
