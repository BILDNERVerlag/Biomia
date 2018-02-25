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
        this.inv = Bukkit.createInventory(null, 27, "§cInformationen über " + name);

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

                    ItemStack is = ItemCreator.headWithSkin(BiomiaPlayer.getName(von), "§b" + (bans + 1) + ". Ban");
                    ItemMeta meta = is.getItemMeta();

                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");

                    Calendar bis = GregorianCalendar.getInstance();
                    bis.set(Calendar.SECOND, length);

                    Calendar wann = GregorianCalendar.getInstance();
                    wann.set(Calendar.SECOND, timestemp);

                    if (perm)
                        meta.setLore(Arrays.asList("", "§r§bWann: §c" + df.format(wann.getTime()), "§r§bGrund: §c" + reason, "§r§bGebannt von: §c" + BiomiaPlayer.getName(von), "§r§bTemporär: §cNein", "§r§bWurde Entbannt: §c" + (!wurdeEntbannt ? "Nein" : "Ja")));
                    else
                        meta.setLore(Arrays.asList("", "§r§bWann: §c" + df.format(wann.getTime()), "§r§bBis: §c" + df.format(bis.getTime()), "§r§bGrund: §c" + reason, "§r§bGebannt von: §c" + BiomiaPlayer.getName(von), "§r§bTemporär: §cJa", "§r§bWurde Entbannt: §c" + (!wurdeEntbannt ? "Nein" : "Ja")));

                    if (wurdeEntbannt) {
                        meta.getLore().add("§r§bEntbannt von: §c" + BiomiaPlayer.getName(entbanntVon));
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

                    is = ItemCreator.itemCreate(Material.STAINED_GLASS, "§aAktuell Gebannt!", (short) 5);
                    ItemMeta meta = is.getItemMeta();

                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");

                    Calendar bis = GregorianCalendar.getInstance();
                    bis.set(Calendar.SECOND, length);

                    Calendar wann = GregorianCalendar.getInstance();
                    wann.set(Calendar.SECOND, timestamp);

                    if (perm) {
                        meta.setLore(Arrays.asList("", "§r§bWann: §c" + df.format(wann.getTime()), "§r§bGrund: §c" + reason, "§r§bGebannt von: §c" + BiomiaPlayer.getName(von), "§r§bTemporär: §cNein", ""));
                    } else {
                        meta.setLore(Arrays.asList("", "§r§bWann: §c" + df.format(wann.getTime()), "§r§bBis: §c" + df.format(bis.getTime()), "§r§bGrund: §c" + reason, "§r§bGebannt von: §c" + BiomiaPlayer.getName(von), "§r§bTemporär: §cJa", ""));
                    }
                    is.setItemMeta(meta);
                }

                if (isBanned) {
                    inv.setItem(14, is);
                } else {
                    inv.setItem(14, ItemCreator.itemCreate(Material.STAINED_GLASS, "§cZur Zeit nicht gebannt", (short) 14));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        ItemStack banns = ItemCreator.itemCreate(Material.PAPER, "§cBanns");
        ItemMeta bannsItemMeta = banns.getItemMeta();
        if (bans > 0)
            banns.setAmount(bans);
        bannsItemMeta.setLore(Collections.singletonList("§r§b" + bans));
        banns.setItemMeta(bannsItemMeta);
        inv.setItem(9, banns);

        inv.setItem(12, ItemCreator.itemCreate(Material.BARRIER, "§cBannen"));

        ItemStack reports = ItemCreator.itemCreate(Material.PAPER, "§cReport-Level");
        ItemMeta reportsMeta = reports.getItemMeta();
        if (level > 0)
            reports.setAmount(level);
        reportsMeta.setLore(Collections.singletonList("§r§b" + level));
        reports.setItemMeta(reportsMeta);
        inv.setItem(18, reports);

        inv.setItem(21, ItemCreator.itemCreate(Material.STAINED_GLASS, "§cReport Entfernen", (short) 5));
        inv.setItem(23, ItemCreator.itemCreate(Material.STAINED_GLASS, "§aReport Fertigstellen", (short) 14));

        inv.setItem(26, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, "§cZurück"));
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
