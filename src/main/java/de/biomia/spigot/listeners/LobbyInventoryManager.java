package de.biomia.spigot.listeners;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Messages;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyInventoryManager {

    private final static ItemStack elytra = ItemCreator.itemCreate(Material.ELYTRA, String.format("%sElytra", Messages.COLOR_MAIN));
    @Getter
    private final static ItemStack bow = ItemCreator.itemCreate(Material.BOW, String.format("%sTeleport-Bogen", Messages.COLOR_SUB));
    @Getter
    private final static ItemStack compass = ItemCreator.itemCreate(Material.COMPASS, String.format("%sNavigator", Messages.COLOR_MAIN));
    @Getter
    private final static ItemStack silentItemOFF = ItemCreator.itemCreate(Material.FIREBALL, String.format("%sSilent Lobby:%s Off", Messages.COLOR_MAIN, Messages.COLOR_AUX));
    @Getter
    private final static ItemStack silentItemON = ItemCreator.itemCreate(Material.FIREBALL, String.format("%sSilent Lobby:%s On", Messages.COLOR_MAIN, Messages.COLOR_SUB));
    @Getter
    private final static ItemStack arrow = ItemCreator.itemCreate(Material.ARROW, String.format("%sTeleport-Pfeil", Messages.COLOR_AUX));
    @Getter
    private final static ItemStack serverSwitcher = ItemCreator.itemCreate(Material.NETHER_STAR, String.format("%sLobby-Switcher", Messages.COLOR_MAIN));
    @Getter
    private final static ItemStack cosmeticItem = ItemCreator.itemCreate(Material.CHEST, String.format("%sCosmetics", Messages.COLOR_MAIN));

    static {

        ItemMeta metaOfBow = bow.getItemMeta();
        metaOfBow.setUnbreakable(true);
        bow.setItemMeta(metaOfBow);
        bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);

        ItemMeta metaOfElytra = elytra.getItemMeta();
        metaOfElytra.setUnbreakable(true);
        elytra.setItemMeta(metaOfElytra);

    }

    private static void addBow(Player pl) {
        pl.getInventory().setItem(5, bow);
        addArrow(pl);
    }

    private static void addCompass(Player pl) {
        pl.getInventory().setItem(1, compass);
    }

    private static void addServerSwitcher(Player pl) {
        pl.getInventory().setItem(8, serverSwitcher);
    }

    private static void addArrow(Player pl) {
        pl.getInventory().setItem(22, arrow);
    }

    private static void addElytra(Player pl) {
        pl.getInventory().setItem(38, elytra);
    }

    private static void addSilentItem(Player pl) {
        pl.getInventory().setItem(6, silentItemOFF);
    }

    private static void addCosmeticItem(Player pl) {
        pl.getInventory().setItem(3, cosmeticItem);
    }

    public static void setInventory(Player pl) {

        BiomiaPlayer biomiaPlayer = Biomia.getBiomiaPlayer(pl);
        addCompass(pl);
        // 4 Freunde
        addCosmeticItem(pl);
        // 7 Verstecke Spieler
        addServerSwitcher(pl);

        if (biomiaPlayer.isOwnerOrDev()) {
            addBow(pl);
            addSilentItem(pl);
            addElytra(pl);
        } else if (biomiaPlayer.isStaff() || biomiaPlayer.isYouTuber()) {
            addBow(pl);
            addSilentItem(pl);
        } else if (biomiaPlayer.isPremium()) {
            addBow(pl);
        }
    }
}
