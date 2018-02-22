package de.biomia.versus.vs.game.bw;

import de.biomia.versus.bw.messages.ItemNames;
import de.biomia.versus.bw.messages.Messages;
import de.biomia.versus.global.Dead;
import de.biomia.versus.vs.game.GameHandler;
import de.biomia.versus.vs.game.GameTeam;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

class BedWarsHandler extends GameHandler {

    private final ArrayList<Block> destroyableBlocks = new ArrayList<>();

    BedWarsHandler(BedWars bedWars) {
        super(bedWars);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        destroyableBlocks.add(e.getBlock());
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
            if (e.getItem().getItemMeta().getDisplayName().equals(ItemNames.rettungsPlattform)) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    buildRettungsplattform(e.getPlayer().getLocation(), e.getItem());
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (mode.containsPlayer(bp))
            if (b.getType() == Material.BED_BLOCK) {
                for (GameTeam allTeams : mode.getTeams()) {
                    BedWarsTeam team = (BedWarsTeam) allTeams;
                    for (Block b1 : team.getBed())
                        if (b1.equals(b)) {
                            if (team.containsPlayer(bp)) {
                                e.setCancelled(true);
                                e.getPlayer().sendMessage(Messages.cantDestroyYourOwnBed);
                            } else {
                                Bukkit.broadcastMessage("�7Das Bett von Team " + team.getColorcode() + team.getTeamname() + " �7wurde zerst�rt!");
                                e.setDropItems(false);
                                e.setCancelled(false);
                                mode.getInstance().getWorld().getPlayers().forEach(each -> each.playSound(each.getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 1, 1));
                                team.destroyBed();
                            }
                        }
                }
            } else if (destroyableBlocks.contains(b)) {
                destroyableBlocks.remove(b);
            } else {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Messages.cantDestroyThisBlock);
            }

    }

    @EventHandler
    private void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        e.setKeepInventory(false);
        if (mode.containsPlayer(bp)) {
            Player killer = p.getKiller();
            e.setDeathMessage(null);
            p.getInventory().clear();
            if (!((BedWarsTeam) mode.getTeam(bp)).hasBed()) {
                for (BiomiaPlayer all : mode.getPlayers())
                    all.getPlayer().sendMessage(Messages.playerDiedFinally.replaceAll("%p", p.getName()));
                Dead.respawn(p);
                mode.getTeam(bp).setDead(bp);
                ((BedWars) mode).getBedWarsScoreboard().setScoreboard(bp, true);
            } else
                for (BiomiaPlayer all : mode.getPlayers())
                    if (killer == null)
                        all.getPlayer().sendMessage(Messages.playerDied.replaceAll("%p", p.getName()));
                    else
                        all.getPlayer().sendMessage(Messages.playerKilledByPlayer.replaceAll("%p1", p.getName()).replaceAll("%p2", killer.getName()));
        }
    }

    @Override
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (mode.containsPlayer(bp) && bp.getPlayer().getWorld().equals(mode.getInstance().getWorld())) {
            BedWarsTeam team = (BedWarsTeam) mode.getTeam(bp);
            if (team.hasBed()) {
                e.setRespawnLocation(team.getHome());
            } else {
                e.setRespawnLocation(mode.getInstance().getWorld().getSpawnLocation().add(0, 100, 0));
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void buildRettungsplattform(Location l, ItemStack is) {

        Location loc = l.clone();
        is.setAmount(is.getAmount() - 1);
        loc.subtract(1, 1, 1);
        for (int x = 0; x < 3; x++) {
            loc.add(1, 0, 0);
            for (int y = 0; y < 3; y++) {
                loc.add(0, 0, 1);
                if (loc.getBlock().getType() == Material.AIR) {
                    loc.getBlock().setType(Material.STAINED_GLASS);
                    loc.getBlock().setData((byte) 4);
                    destroyableBlocks.add(loc.getBlock());
                }
            }
            loc.subtract(0, 0, 3);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                l.subtract(1, 1, 1);
                for (int x = 0; x < 3; x++) {
                    l.add(1, 0, 0);
                    for (int y = 0; y < 3; y++) {
                        l.add(0, 0, 1);
                        if (l.getBlock().getType() == Material.STAINED_GLASS) {
                            l.getBlock().setType(Material.AIR);
                        }
                    }
                    l.subtract(0, 0, 3);
                }
            }
        }.runTaskLater(Main.getPlugin(), 20 * 15);
    }

}
