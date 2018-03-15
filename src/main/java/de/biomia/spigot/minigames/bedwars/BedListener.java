package de.biomia.spigot.minigames.bedwars;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.events.bedwars.BedWarsDestroyBedEvent;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.minigames.bedwars.BedWarsTeam;
import de.biomia.spigot.minigames.bedwars.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BedListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        GameTeam bpTeam = bp.getTeam();

        if (e.getBlock().getType() == Material.BED_BLOCK && bpTeam != null) {
            for (GameTeam team : BedWars.getBedWars().getTeams()) {
                for (Block l : ((BedWarsTeam) team).getBed()) {
                    if (l.equals(e.getBlock())) {
                        if (team.equals(bpTeam)) {
                            e.setCancelled(true);
                            e.getPlayer().sendMessage(BedWarsMessages.cantDestroyYourOwnBed);
                            return;
                        }
                        Bukkit.getPluginManager().callEvent(new BedWarsDestroyBedEvent(Biomia.getBiomiaPlayer(e.getPlayer()), team.getTeamname()));
                        ((BedWarsTeam) team).destroyBed();
                        Bukkit.broadcastMessage("\u00A7cDas Bett von Team " + team.getColorcode() + team.getColor().translate() + " \u00A7cwurde zerst\u00F6rt!");
                        e.setDropItems(false);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 1, 1);
                        }
                        return;
                    }
                }
            }
        } else if (Variables.destroyableBlocks.contains(e.getBlock())) {
            Variables.destroyableBlocks.remove(e.getBlock());
            if (e.getBlock().getType() == Material.ENDER_CHEST) {
                e.setCancelled(false);
                e.getBlock().setType(Material.AIR);
                e.getBlock().getWorld().dropItem(e.getBlock().getLocation().add(0.5, 0, 0.5), Variables.teamChest);
                GameTeam t = Variables.getTeamByTeamChests(e.getBlock());
                Variables.teamChestsLocs.get(t).remove(e.getBlock());
            }
        } else if (BedWars.getBedWars().getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            e.getPlayer().sendMessage(BedWarsMessages.cantDestroyThisBlock);
            e.setCancelled(true);
        } else {
            if (!Biomia.getBiomiaPlayer(e.getPlayer()).canBuild()) {
                e.setCancelled(true);
            }
        }
    }
}
