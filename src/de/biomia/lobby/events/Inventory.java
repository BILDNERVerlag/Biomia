package de.biomia.lobby.events;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.pex.Rank;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Inventory implements Listener {

	private final static ItemStack elytra = ItemCreator.itemCreate(Material.ELYTRA, "§bElytra");
	private final static ItemStack bow = ItemCreator.itemCreate(Material.BOW, "§4Teleport-Bogen");
	private final static ItemStack compass = ItemCreator.itemCreate(Material.COMPASS, "§cNavigator");
	private final static ItemStack silentItem = ItemCreator.itemCreate(Material.FIREBALL, "§cSilent Lobby:§8 Off");
	private final static ItemStack arrow = ItemCreator.itemCreate(Material.ARROW, "§6Teleport-Pfeil");
	private final static ItemStack serverSwitcher = ItemCreator.itemCreate(Material.NETHER_STAR, "§dLobby Switcher");
	private final static ItemStack cosmeticItem = ItemCreator.itemCreate(Material.CHEST, "§eCosmetics");

	static {

		ItemMeta metaOfBow = bow.getItemMeta();
		metaOfBow.setUnbreakable(true);
		bow.setItemMeta(metaOfBow);
		bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		
		ItemMeta metaOfElytra = elytra.getItemMeta();
		metaOfElytra.setUnbreakable(true);
		elytra.setItemMeta(metaOfElytra);

	}

	@EventHandler
	public void onHitProt(ProjectileHitEvent e) {
		if (e.getEntity().getShooter() instanceof Player && e.getEntityType() == EntityType.ARROW) {
			Player p = (Player) e.getEntity().getShooter();
			Location loc = e.getEntity().getLocation();
			e.getEntity().remove();

			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			World world = loc.getWorld();

			p.teleport(new Location(world, x, y, z, p.getLocation().getYaw(), p.getLocation().getPitch()));
		}
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
        pl.getInventory().setItem(6, silentItem);
    }

    private static void addCosmeticItem(Player pl) {
        pl.getInventory().setItem(3, cosmeticItem);
	}

	private static void addEmptySlot(Player pl, int i) {
		pl.getInventory().setItem(i, null);
	}

	public static void setInventory(Player pl) {

		BiomiaPlayer biomiaPlayer = Biomia.getBiomiaPlayer(pl);

		addEmptySlot(pl, 0);
        addCompass(pl);
		// Freunde
        addCosmeticItem(pl);
		addEmptySlot(pl, 4);
		addEmptySlot(pl, 5);
		addEmptySlot(pl, 6);
		// 7 Verstecke Spieler
        addServerSwitcher(pl);

		if (Rank.getRank(pl).equals("Owner")) {
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
