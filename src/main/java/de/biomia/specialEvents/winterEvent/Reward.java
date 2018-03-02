package de.biomia.specialEvents.winterEvent;//package de.biomia.specialEvents.winterEvent;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Random;
//
//import org.bukkit.Color;
//import org.bukkit.Material;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.LeatherArmorMeta;
//
//import de.biomia.BiomiaPlayer;
//import ItemCreator;
//import de.biomia.dataManager.MySQL;
//import QuestItems;
//import SkyWarsKitManager;
//
//public class Reward {
//
//    private int rewardID;
//    private byte id;
//    private rewardType type;
//
//    public static Reward getRandomReward(BiomiaPlayer biomiaPlayer) {
//        ArrayList<Integer> ids = getRewards(biomiaPlayer);
//        int random = new Random().nextInt(ids.size());
//        return getReward(ids.get(random));
//    }
//
//    private static Reward getReward(int id) {
//        return WinterEvent.rewards.get(id);
//    }
//
//    private static ArrayList<Integer> getRewards(BiomiaPlayer biomiaPlayer) {
//
//        ArrayList<Integer> rewardIDs = new ArrayList<>();
//
//        for (int i = 1; i < 24; i++)
//            rewardIDs.add(i);
//
//        Connection con = MySQL.Connect(MySQL.Databases.biomia_db);
//        if (con != null) {
//            try {
//                PreparedStatement ps = con.prepareStatement("Select rewardID from WinterEvent where BiomiaPlayer = ?");
//                ps.setInt(1, biomiaPlayer.getBiomiaPlayerID());
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    rewardIDs.remove(new Integer(rs.getInt("rewardID")));
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//        return rewardIDs;
//    }
//
//    public enum rewardType {
//        Shop_Gutschein, SkyWars_Kit, Ingame_Coins, QuestWelt_Items, Coins_Boost
//    }
//
//    public Reward(rewardType type, byte id, int rewardID) {
//        this.type = type;
//        this.id = id;
//        this.rewardID = rewardID;
//    }
//
//    public void give(BiomiaPlayer biomiaPlayer) {
//
//        biomiaPlayer.getPlayer().sendMessage("\u00A74Heute in deinem \u00A75Advents\u00A78-\u00A72Kalender:");
//
//        switch (type) {
//            case Coins_Boost:
//                biomiaPlayer.giveBoost(200, 86400);
//                biomiaPlayer.getPlayer().sendMessage("\u00A7424 Stunden doppelte Coins auf alles!");
//                biomiaPlayer.getPlayer().sendMessage("\u00A74(au\u00DFer Tiernahrung)");
//                break;
//            case Ingame_Coins:
//                biomiaPlayer.addCoins(3000, false);
//                biomiaPlayer.getPlayer().sendMessage("\u00A743000 Coins!");
//                break;
//            case QuestWelt_Items:
//                ItemStack is = null;
//                biomiaPlayer.getPlayer()
//                        .sendMessage("\u00A74Ein Item f\u00fcr die Quest Welt! Schau es dir direkt in der Quest Welt an!");
//                switch (id) {
//                    case 0:
//                        is = ItemCreator.itemCreate(Material.LEATHER_HELMET, "Hut des Weihnachtselfen");
//                        LeatherArmorMeta elfenHatMeta = (LeatherArmorMeta) is.getItemMeta();
//                        elfenHatMeta.setColor(Color.fromRGB(179, 0, 12));
//                        is.setItemMeta(elfenHatMeta);
//                        is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
//                        is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
//                        break;
//                    case 1:
//                        is = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE, "Hemd des Weihnachtselfen");
//                        LeatherArmorMeta elfenChestMeta = (LeatherArmorMeta) is.getItemMeta();
//                        elfenChestMeta.setColor(Color.fromRGB(0, 179, 44));
//                        is.setItemMeta(elfenChestMeta);
//                        is.addUnsafeEnchantment(Enchantment.THORNS, 2);
//                        is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
//                        break;
//                    case 2:
//                        is = ItemCreator.itemCreate(Material.LEATHER_LEGGINGS, "Hose des Weihnachtselfen");
//                        LeatherArmorMeta elfenPantsMeta = (LeatherArmorMeta) is.getItemMeta();
//                        elfenPantsMeta.setColor(Color.fromRGB(0, 179, 44));
//                        is.setItemMeta(elfenPantsMeta);
//                        is.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 2);
//                        is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
//                        break;
//                    case 3:
//                        is = ItemCreator.itemCreate(Material.LEATHER_BOOTS, "Schuhe des Weihnachtselfen");
//                        LeatherArmorMeta elfenBootsMeta = (LeatherArmorMeta) is.getItemMeta();
//                        elfenBootsMeta.setColor(Color.fromRGB(0, 179, 44));
//                        is.setItemMeta(elfenBootsMeta);
//                        is.addUnsafeEnchantment(Enchantment.FROST_WALKER, 2);
//                        is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
//                        break;
//                    case 4:
//                        is = ItemCreator.itemCreate(Material.GOLD_SWORD, "Goldklinge des Weihnachtselfen");
//                        is.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//                        is.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 2);
//                        break;
//                    case 5:
//                        is = ItemCreator.itemCreate(Material.BOW, "Bogen des Weihnachtselfen");
//                        is.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
//                        is.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
//                        break;
//                    case 6:
//                        is = ItemCreator.itemCreate(Material.GOLD_PICKAXE, "Spitzhacke des Weihnachtselfen");
//                        is.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);
//                        is.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 4);
//                        break;
//                }
//                if (is != null)
//                    QuestItems.addQuestItem(biomiaPlayer, is);
//                break;
//            case Shop_Gutschein:
//                biomiaPlayer.getPlayer().sendMessage("\u00A74Einen 5€ Gutschein f\u00fcr den BILDNER-Verlag Online Shop!");
//                biomiaPlayer.getPlayer().sendMessage("\u00A74(Mindestbestellwert 10€)");
//
//                switch (id) {
//                    case 0:
//                        biomiaPlayer.getPlayer().sendMessage("\u00A74Der Gutschein Code lautet: \u00A75ZHP5WXA");
//                        break;
//                    case 1:
//                        biomiaPlayer.getPlayer().sendMessage("\u00A74Der Gutschein Code lautet: \u00A75AHU9PWP");
//                        break;
//                    case 2:
//                        biomiaPlayer.getPlayer().sendMessage("\u00A74Der Gutschein Code lautet: \u00A75KEXNW2S");
//                        break;
//                    default:
//                        break;
//                }
//                break;
//            case SkyWars_Kit:
//                biomiaPlayer.getPlayer().sendMessage("\u00A74Ein SkyWars Kit! Schau direkt nach was f\u00fcr ein Kit es ist!");
//                biomiaPlayer.getPlayer().sendMessage("\u00A74Info: Du kannst das Kit nur im EventCommands benutzen!");
//                switch (id) {
//                    case 0:
//                        SkyWarsKitManager.addKit(biomiaPlayer, 20171, 12, 1, 2);
//                        break;
//                    case 1:
//                        SkyWarsKitManager.addKit(biomiaPlayer, 20172, 12, 1, 2);
//                        break;
//                    default:
//                        break;
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    public int getRewardID() {
//        return rewardID;
//    }
//}
