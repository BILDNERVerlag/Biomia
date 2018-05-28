package de.biomia.spigot.general.reportsystem;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.MySQL;
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

public class InformationInventory {

    private final Inventory inv;
    private final int biomiaID;

    public InformationInventory(BiomiaPlayer bp, int biomiaID) {
        String name = Biomia.getOfflineBiomiaPlayer(biomiaID).getName();
        this.biomiaID = biomiaID;
        this.inv = Bukkit.createInventory(null, 27, "§cInformationen über " + name);

        int level = ReportSQL.getLevel(biomiaID);
        int bans = 0;
        int entbannungen = 0;
        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
        if (con != null) {
            try {
                //Chached Ban List

                PreparedStatement sql = con.prepareStatement("Select * from CachedBanList where biomiaID = ?");
                sql.setInt(1, biomiaID);
                ResultSet rs = sql.executeQuery();
                while (rs.next()) {
                    String reason = rs.getString("Grund");
                    int length = rs.getInt("bis");
                    int timestemp = rs.getInt("timestamp");
                    boolean perm = rs.getBoolean("permanent");
                    int von = rs.getInt("von");
                    boolean wurdeEntbannt = rs.getBoolean("wurdeEntbannt");
                    int entbanntVon = rs.getInt("entbanntVon");

                    ItemStack is = ItemCreator.headWithSkin(Biomia.getOfflineBiomiaPlayer(von).getName(), "§b" + (bans + 1) + ". Ban");
                    ItemMeta meta = is.getItemMeta();

                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");

                    Calendar bis = GregorianCalendar.getInstance();
                    bis.set(Calendar.SECOND, length);

                    Calendar wann = GregorianCalendar.getInstance();
                    wann.set(Calendar.SECOND, timestemp);

                    if (perm)
                        meta.setLore(Arrays.asList("", "§r§bWann: §c" + df.format(wann.getTime()), "§r§bGrund: §c" + reason, "§r§bGebannt von: §c" + Biomia.getOfflineBiomiaPlayer(von).getName(), "§r§bTemporär: §cNein", "§r§bWurde Entbannt: §c" + (!wurdeEntbannt ? "Nein" : "Ja")));
                    else
                        meta.setLore(Arrays.asList("", "§r§bWann: §c" + df.format(wann.getTime()), "§r§bBis: §c" + df.format(bis.getTime()), "§r§bGrund: §c" + reason, "§r§bGebannt von: §c" + Biomia.getOfflineBiomiaPlayer(von).getName(), "§r§bTemporär: §cJa", "§r§bWurde Entbannt: §c" + (!wurdeEntbannt ? "Nein" : "Ja")));

                    if (wurdeEntbannt) {
                        meta.getLore().add("§r§bEntbannt von: §c" + Biomia.getOfflineBiomiaPlayer(entbanntVon).getName());
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
                banListStatement.setInt(1, biomiaID);

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
                        meta.setLore(Arrays.asList("", "§r§bWann: §c" + df.format(wann.getTime()), "§r§bGrund: §c" + reason, "§r§bGebannt von: §c" + Biomia.getOfflineBiomiaPlayer(von).getName(), "§r§bTemporär: §cNein", ""));
                    } else {
                        meta.setLore(Arrays.asList("", "§r§bWann: §c" + df.format(wann.getTime()), "§r§bBis: §c" + df.format(bis.getTime()), "§r§bGrund: §c" + reason, "§r§bGebannt von: §c" + Biomia.getOfflineBiomiaPlayer(von).getName(), "§r§bTemporär: §cJa", ""));
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

        ItemStack entbanns = ItemCreator.itemCreate(Material.PAPER, "§cEntbannungen");
        ItemMeta entbannsMeta = entbanns.getItemMeta();
        if (entbannungen > 0)
            entbanns.setAmount(entbannungen);
        entbannsMeta.setLore(Collections.singletonList("§r§b" + entbannungen));
        entbanns.setItemMeta(entbannsMeta);
        inv.setItem(9, entbanns);

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
        bp.getPlayer().openInventory(inv);
    }

    public int getBiomiaID() {
        return biomiaID;
    }

    public Inventory getInventory() {
        return inv;
    }

}
