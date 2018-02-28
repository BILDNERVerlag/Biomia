package de.biomia.plugin.listeners;

import de.biomia.api.main.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;

public class CosmeticListener implements Listener {

    private static final HashMap<Location, Material> blocksMaterial = new HashMap<>();
    private static final HashMap<Location, Byte> blocksData = new HashMap<>();

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {

        if (e.getEntity().getCustomName() == null)
            return;

        switch (e.getEntity().getCustomName()) {
            case "Paintball":
                if (e.getHitBlock() != null) {
                    Block b = e.getHitBlock();

                    if (b.getType() == Material.SKULL || b.getType() == Material.WALL_SIGN) {
                        break;
                    }

                    if (!blocksMaterial.containsKey(b.getLocation())) {
                        blocksMaterial.put(b.getLocation(), b.getType());
                        blocksData.put(b.getLocation(), b.getData());
                    }
                    b.setType(Material.CONCRETE);
                    b.setData((byte) new Random().nextInt(16));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            b.setType(blocksMaterial.get(b.getLocation()));
                            b.setData(blocksData.get(b.getLocation()));
                        }
                    }.runTaskLater(Main.plugin, 20 * 25);
                } else if (e.getHitEntity() != null) {
                    Entity ent = e.getHitEntity();
                    if (ent instanceof ArmorStand) {
                        return;
                    }
                    ent.setGlowing(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ent.setGlowing(false);
                        }
                    }.runTaskLater(Main.plugin, 20 * 25);
                }
                break;
            case "Freezer":
                if (e.getHitEntity() != null && e.getHitEntity() instanceof Player) {
                    Player p = (Player) e.getHitEntity();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 100, false, false));
                }
                break;
            case "Switcher":
                Player shooter = (Player) e.getEntity().getShooter();
                e.getEntity().remove();
                shooter.getInventory().getItemInMainHand().setDurability((short) 0);
                if (e.getHitEntity() != null && e.getHitEntity() instanceof Player) {
                    Player p = (Player) e.getHitEntity();
                    Location l = p.getLocation();
                    p.teleport(shooter);
                    shooter.teleport(l);
                }
                break;
            default:
                break;
        }
    }

}
