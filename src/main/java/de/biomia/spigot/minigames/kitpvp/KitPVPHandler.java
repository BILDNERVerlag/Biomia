package de.biomia.spigot.minigames.kitpvp;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.GameHandler;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.WarteLobbyListener;
import de.biomia.spigot.minigames.bedwars.BedWarsTeam;
import de.biomia.universal.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

class KitPVPHandler extends GameHandler {

    KitPVPHandler(GameMode mode) {
        super(mode);
    }

    private final ArrayList<Block> destroyableBlocks = new ArrayList<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

        if (WarteLobbyListener.inLobbyOrSpectator(bp) || !bp.canBuild()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BedWarsMessages.cantPlaceBlock);
            return;
        }
        destroyableBlocks.add(e.getBlock());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (WarteLobbyListener.inLobbyOrSpectator(bp) || !bp.canBuild()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(BedWarsMessages.cantDestroyThisBlock);
            return;
        }
        if (destroyableBlocks.contains(e.getBlock())) {
            destroyableBlocks.remove(e.getBlock());
        } else if (e.getBlock().getType() == Material.BED_BLOCK) {
            for (GameTeam gt : mode.getTeams()) {
                if (gt instanceof BedWarsTeam) {
                    BedWarsTeam bt = ((BedWarsTeam) gt);
                    if (bt.getBed().contains(e.getBlock())) {
                        if (bt.equals(bp.getTeam())) {
                            e.getPlayer().sendMessage(MinigamesMessages.destroyOwnBed);
                            e.setCancelled(true);
                            return;
                        }
                        bt.destroyBed();
                        Bukkit.broadcastMessage(String.format(BedWarsMessages.bedWasDestroyed, bt.getColorcode(), Messages.COLOR_AUX, bt.getColorcode(), bt.getTeamname(), Messages.COLOR_AUX));
                        e.setDropItems(false);
                        return;
                    }
                }
            }
        } else {
            e.setCancelled(true);
        }
    }
}
