package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.listeners.servers.BiomiaListener;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.tools.RankManager;
import de.biomia.universal.UniversalBiomia;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class WaitingLobbyListener extends BiomiaListener {

    private final boolean isVersus;

    public WaitingLobbyListener(boolean isVersus) {
        this.isVersus = isVersus;
    }

    @EventHandler
    public void onHungerSwitch(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            e.setCancelled(inLobbyOrSpectator(Biomia.getBiomiaPlayer(p)));
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(inLobbyOrSpectator(Biomia.getBiomiaPlayer(e.getPlayer())));
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player)
            e.setCancelled(inLobbyOrSpectator(Biomia.getBiomiaPlayer((Player) e.getEntity())));
    }

    @EventHandler
    public void onPlayerSwap(PlayerSwapHandItemsEvent e) {
        e.setCancelled(inLobbyOrSpectator(Biomia.getBiomiaPlayer(e.getPlayer())));
    }

    @EventHandler
    public void onProjectileThrow(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player)
            e.setCancelled(inLobbyOrSpectator(Biomia.getBiomiaPlayer((Player) e.getEntity().getShooter())));
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {
            String rank = RankManager.getRank(e.getPlayer());
            int i = UniversalBiomia.getRankLevel(rank);
            if (i == 1 || i == 2)
                return;
            for (Player eachPlayer : Bukkit.getOnlinePlayers()) {

                GameTeam team = Biomia.getBiomiaPlayer(eachPlayer).getTeam();

                if (team != null && team.getMode().getStateManager().getActualGameState() != GameStateManager.GameState.LOBBY) {
                    if (isVersus)
                        continue;
                    else {
                        e.allow();
                        return;
                    }
                }

                if (UniversalBiomia.getRankLevel(RankManager.getRank(eachPlayer)) < i) {
                    eachPlayer.sendMessage(MinigamesMessages.kickedForPremium);
                    eachPlayer.kickPlayer("");
                    e.allow();
                    return;
                }
            }

            e.setKickMessage(MinigamesMessages.serverFull);
        }
    }

    public static boolean inLobbyOrSpectator(BiomiaPlayer bp) {
        return bp.getTeam() == null || bp.getTeam().getMode().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME || bp.getTeam().getMode().isSpectator(bp);
    }
}
