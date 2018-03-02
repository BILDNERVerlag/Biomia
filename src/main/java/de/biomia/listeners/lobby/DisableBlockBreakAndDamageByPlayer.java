package de.biomia.listeners.lobby;

import de.biomia.Biomia;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DisableBlockBreakAndDamageByPlayer implements Listener {

    private final Location stonebutton = new Location(Bukkit.getWorld("LobbyBiomia"), 465, 97, 359);

    @EventHandler
    public void cancelInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                if (event.getClickedBlock().getType() == Material.STONE_BUTTON
                        && event.getClickedBlock().getLocation().distance(stonebutton) <= 2) {
                    return;
                }
                if (event.getItem() != null && event.getItem().getType() == Material.BOW) {
                    if (!(event.getClickedBlock().getType() == Material.STONE_BUTTON)
                            && !(event.getClickedBlock().getLocation().distance(stonebutton) <= 2))
                        return;
                    if (event.getClickedBlock().getType() == Material.DAYLIGHT_DETECTOR
                            || event.getClickedBlock().getType() == Material.DAYLIGHT_DETECTOR_INVERTED
                            || event.getClickedBlock().getType() == Material.WOOD_BUTTON
                            || event.getClickedBlock().getType() == Material.LEVER || event.getClickedBlock().getType() == Material.FENCE_GATE)
                        event.setCancelled(true);
                }
                if (!Biomia.getBiomiaPlayer(event.getPlayer()).canBuild()) {
                    event.setCancelled(true);
                }
            }
        }
    }

}