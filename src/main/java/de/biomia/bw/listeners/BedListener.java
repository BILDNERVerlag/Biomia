package de.biomia.bw.listeners;

import de.biomia.bw.gamestates.GameState;
import de.biomia.bw.main.BedWarsMain;
import de.biomia.bw.messages.Messages;
import de.biomia.bw.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.api.Teams.Team;
import de.biomia.api.achievements.statEvents.bedwars.BedWarsDestroyBedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BedListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {

        if (e.getBlock().getType() == Material.BED_BLOCK) {
            for (Team t : Biomia.TeamManager().getTeams()) {
                for (Location l : Variables.beds.get(t)) {
                    if (l != null && l.getBlock().getType() == Material.BED_BLOCK
                            && l.equals(e.getBlock().getLocation())) {
                        if (Biomia.TeamManager().isPlayerInAnyTeam(e.getPlayer())) {
                            Team playersTeam = Biomia.TeamManager().getTeam(e.getPlayer());
                            if (t.equals(playersTeam)) {
                                e.setCancelled(true);
                                e.getPlayer().sendMessage(Messages.cantDestroyYourOwnBed);
                                return;
                            }
                            Bukkit.getPluginManager().callEvent(new BedWarsDestroyBedEvent(Biomia.getBiomiaPlayer(e.getPlayer()), Variables.beds.get(t).get(0), Variables.beds.get(t).get(1), t.getTeamname()));
                            Variables.teamsWithBeds.remove(t);
                            Bukkit.broadcastMessage("00A7cDas Bett von Team " + t.getColorcode()
                                    + Biomia.TeamManager().translate(t.getTeamname()) + " 00A7cwurde zerst\u00F6rt!");
                            e.setDropItems(false);
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 1, 1);
                            }
                            return;
                        }
                    }
                }
            }
        } else if (Variables.destroyableBlocks.contains(e.getBlock())) {
            Variables.destroyableBlocks.remove(e.getBlock());
            if (e.getBlock().getType() == Material.ENDER_CHEST) {
                e.setCancelled(false);
                e.getBlock().setType(Material.AIR);
                e.getBlock().getWorld().dropItem(e.getBlock().getLocation().add(0.5, 0, 0.5), Variables.teamChest);
                Team t = Variables.getTeamByTeamChests(e.getBlock());
                Variables.teamChestsLocs.get(t).remove(e.getBlock());
            }
        } else if (BedWarsMain.gameState == GameState.INGAME) {
            e.getPlayer().sendMessage(Messages.cantDestroyThisBlock);
            e.setCancelled(true);
        } else {
            if (!Biomia.getBiomiaPlayer(e.getPlayer()).canBuild()) {
                e.setCancelled(true);
            }
        }
    }
}
