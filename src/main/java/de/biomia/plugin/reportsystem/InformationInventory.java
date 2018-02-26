package de.biomia.plugin.reportsystem;

import de.biomia.api.BiomiaPlayer;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

class InformationInventory {

    private final Inventory inv;
    private final int biomiaID;
    private final BiomiaPlayer bp;

    //TODO Command
    public InformationInventory(BiomiaPlayer bp, int biomiaPlayerID) {
        String name = BiomiaPlayer.getName(biomiaPlayerID);
        this.bp = bp;
        this.biomiaID = biomiaPlayerID;
        this.inv = Bukkit.createInventory(null, 27, "00A7cInformationen \u00fcber " + name);

        int level = ReportSQL.getLevel(biomiaPlayerID);
        int bans = 0;
        int entbannungen = 0;
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        if (con != null) {
            try {

                //Chached Ban List

                PreparedStatement sql = con.prepareStatement("Select * from CachedBanList where biomiaID = ?");
                sql.setInt(1, biomiaPlayerID);
                ResultSet rs = sql.executeQuery();
                while (rs.next()) {
                    String reason = rs.getString("Grund");
                    int length = rs.getInt("bis");
                    int timestemp = rs.getInt("timestamp");
                    boolean perm = rs.getBoolean("permanent");
                    int von = rs.getInt("von");
                    boolean wurdeEntbannt = rs.getBoolean("wurdeEntbannt");
                    int entbanntVon = rs.getInt("entbanntVon");

                    ItemStack is = ItemCreator.headWithSkin(BiomiaPlayer.getName(von), "00A7b" + (bans + 1) + ". Ban");
                    ItemMeta meta = is.getItemMeta();

                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");

                    Calendar bis = GregorianCalendar.getInstance();
                    bis.set(Calendar.SECOND, length);

                    Calendar wann = GregorianCalendar.getInstance();
                    wann.set(Calendar.SECOND, timestemp);

                    if (perm)
                        meta.setLore(Arrays.asList("", "00A7r00A7bWann: 00A7c" + df.format(wann.getTime()), "00A7r00A7bGrund: 00A7c" + reason, "00A7r00A7bGebannt von: 00A7c" + BiomiaPlayer.getName(von), "00A7r00A7bTempor\u00fcr: 00A7cNein", "00A7r00A7bWurde Entbannt: 00A7c" + (!wurdeEntbannt ? "Nein" : "Ja")));
                    else
                        meta.setLore(Arrays.asList("", "00A7r00A7bWann: 00A7c" + df.format(wann.getTime()), "00A7r00A7bBis: 00A7c" + df.format(bis.getTime()), "00A7r00A7bGrund: 00A7c" + reason, "00A7r00A7bGebannt von: 00A7c" + BiomiaPlayer.getName(von), "00A7r00A7bTempor\u00fcr: 00A7cJa", "00A7r00A7bWurde Entbannt: 00A7c" + (!wurdeEntbannt ? "Nein" : "Ja")));

                    if (wurdeEntbannt) {
                        meta.getLore().add("00A7r00A7bEntbannt von: 00A7c" + BiomiaPlayer.getName(entbanntVon));
                        entbannungen++;
                    }
                    meta.getLore().add("");
                    is.setItemMeta(meta);
                    inv.setItem(bans, is);
                    bans++;
                }
                rs.close();
                sql.close();

                //Actual BanList

                PreparedStatement banListStatement = con.prepareStatement("Select permanent from BanList where biomiaID = ?");
                banListStatement.setInt(1, biomiaPlayerID);

                ResultSet banListResult = banListStatement.executeQuery();

                boolean isBanned = false;
                ItemStack is = null;
                if (banListResult.next()) {
                    isBanned = true;
                    String reason = rs.getString("Grund");
                    int length = rs.getInt("bis");
                    int timestamp = rs.getInt("timestamp");
                    boolean perm = rs.getBoolean("permanent");
                    int von = rs.getInt("von");

                    is = ItemCreator.itemCreate(Material.STAINED_GLASS, "00A7aAktuell Gebannt!", (short) 5);
                    ItemMeta meta = is.getItemMeta();

                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");

                    Calendar bis = GregorianCalendar.getInstance();
                    bis.set(Calendar.SECOND, length);

                    Calendar wann = GregorianCalendar.getInstance();
                    wann.set(Calendar.SECOND, timestamp);

                    if (perm) {
                        meta.setLore(Arrays.asList("", "00A7r00A7bWann: 00A7c" + df.format(wann.getTime()), "00A7r00A7bGrund: 00A7c" + reason, "00A7r00A7bGebannt von: 00A7c" + BiomiaPlayer.getName(von), "00A7r00A7bTempor\u00fcr: 00A7cNein", ""));
                    } else {
                        meta.setLore(Arrays.asList("", "00A7r00A7bWann: 00A7c" + df.format(wann.getTime()), "00A7r00A7bBis: 00A7c" + df.format(bis.getTime()), "00A7r00A7bGrund: 00A7c" + reason, "00A7r00A7bGebannt von: 00A7c" + BiomiaPlayer.getName(von), "00A7r00A7bTempor\u00fcr: 00A7cJa", ""));
                    }
                    is.setItemMeta(meta);
                }

                if (isBanned) {
                    inv.setItem(14, is);
                } else {
                    inv.setItem(14, ItemCreator.itemCreate(Material.STAINED_GLASS, "00A7cZur Zeit nicht gebannt", (short) 14));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        ItemStack banns = ItemCreator.itemCreate(Material.PAPER, "00A7cBanns");
        ItemMeta bannsItemMeta = banns.getItemMeta();
        if (bans > 0)
            banns.setAmount(bans);
        bannsItemMeta.setLore(Collections.singletonList("00A7r00A7b" + bans));
        banns.setItemMeta(bannsItemMeta);
        inv.setItem(9, banns);

        inv.setItem(12, ItemCreator.itemCreate(Material.BARRIER, "00A7cBannen"));

        ItemStack reports = ItemCreator.itemCreate(Material.PAPER, "00A7cReport-Level");
        ItemMeta reportsMeta = reports.getItemMeta();
        if (level > 0)
            reports.setAmount(level);
        reportsMeta.setLore(Collections.singletonList("00A7r00A7b" + level));
        reports.setItemMeta(reportsMeta);
        inv.setItem(18, reports);

        inv.setItem(21, ItemCreator.itemCreate(Material.STAINED_GLASS, "00A7cReport Entfernen", (short) 5));
        inv.setItem(23, ItemCreator.itemCreate(Material.STAINED_GLASS, "00A7aReport Fertigstellen", (short) 14));

        inv.setItem(26, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, "00A7cZur\u00fcck"));
    }

    public int getBiomiaID() {
        return biomiaID;
    }

    public void openInventory() {
        bp.getPlayer().openInventory(inv);
    }

    public Inventory getInventory() {
        return inv;
    }

}
