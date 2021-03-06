package de.biomia.spigot.general.cosmetics;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.general.cosmetics.Cosmetic.Group;
import de.biomia.spigot.general.cosmetics.items.*;
import de.biomia.spigot.general.cosmetics.items.CosmeticItem.Commonness;
import de.biomia.spigot.tools.Base64;
import de.biomia.universal.MySQL;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CosmeticGroup {

    private final Group group;
    private final ArrayList<CosmeticItem> items = new ArrayList<>();
    private final ItemStack icon;

    public CosmeticGroup(Group group, ItemStack icon) {
        this.group = group;
        this.icon = icon;
        loadGroup();
    }

    public void remove(BiomiaPlayer bp) {
        if (!items.isEmpty()) items.get(0).remove(bp);
    }

    private void addItem(CosmeticItem item) {
        items.add(item);
    }

    private void loadGroup() {

        Connection con = MySQL.Connect(MySQL.Databases.cosmetics_db);
        try {

            PreparedStatement ps = con.prepareStatement("Select * from " + group.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                ItemStack is = (ItemStack) Base64.fromBase64(rs.getString("Item"));
                Commonness c = Commonness.valueOf(rs.getString("Commonness"));

                switch (group) {
                    case GADGETS:
                        ItemStack gadget = (ItemStack) Base64.fromBase64(rs.getString("GadgetItem"));
                        addItem(new CosmeticGadgetItem(id, name, is, c, gadget));
                        break;
                    case HEADS:
                        ItemStack head = (ItemStack) Base64.fromBase64(rs.getString("Head"));
                        addItem(new CosmeticHeadItem(id, name, is, c, head));
                        break;
                    case PARTICLES:
                        addItem(new CosmeticParticleItem(id, name, is, c));
                        break;
                    case PETS:
                        addItem(new CosmeticPetItem(id, name, is, c, EntityType.valueOf(rs.getString("Type"))));
                        break;
                    case SUITS:
                        CosmeticSuitItem suit = new CosmeticSuitItem(id, name, is, c);
                        String helmet = rs.getString("Helmet");
                        String chestplate = rs.getString("Chestplate");
                        String leggins = rs.getString("Leggins");
                        String boots = rs.getString("Boots");
                        if (boots != null)
                            suit.setHelmet((ItemStack) Base64.fromBase64(helmet));
                        if (chestplate != null)
                            suit.setChestplate((ItemStack) Base64.fromBase64(chestplate));
                        if (leggins != null)
                            suit.setLeggins((ItemStack) Base64.fromBase64(leggins));
                        if (boots != null)
                            suit.setBoots((ItemStack) Base64.fromBase64(boots));
                        addItem(suit);
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Group getGroup() {
        return group;
    }

    public ItemStack getIcon() {
        return icon;
    }

}
