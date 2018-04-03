package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.listeners.servers.BiomiaListener;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.universal.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class WaitingLobbyListener extends BiomiaListener {

    private final boolean isVersus;

    public WaitingLobbyListener(boolean isVersus) {
        this.isVersus = isVersus;
    }

    @EventHandler
    public void onJoin_(PlayerJoinEvent e) {
        if (isVersus) {
            Versus.getInstance().getManager().moveToLobby(e.getPlayer(), true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(PlayerSpawnLocationEvent e) {
        e.setSpawnLocation(GameMode.getSpawn(isVersus));
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

        if (!Biomia.getBiomiaPlayer(e.getPlayer()).isSrStaff()) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "�cZUR ZEIT IN WARTUNGSARBEITEN!!");
            return;
        }

        if (e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {
            Ranks rank = Biomia.getBiomiaPlayer(e.getPlayer()).getRank();
            int i = rank.getLevel();
            if (rank == Ranks.RegSpieler || rank == Ranks.UnregSpieler) {
                e.disallow(PlayerLoginEvent.Result.KICK_FULL, MinigamesMessages.serverFull);
                return;
            }
            for (Player eachPlayer : Bukkit.getOnlinePlayers()) {
                BiomiaPlayer eachBP = Biomia.getBiomiaPlayer(eachPlayer);
                GameTeam team = eachBP.getTeam();
                if (team != null && team.getMode().getStateManager().getActualGameState() != GameStateManager.GameState.LOBBY) {
                    if (isVersus)
                        continue;
                    else {
                        e.allow();
                        return;
                    }
                }
                if (eachBP.getRank().getLevel() < i) {
                    eachPlayer.sendMessage(MinigamesMessages.kickedForPremium);
                    eachPlayer.kickPlayer("");
                    e.allow();
                    return;
                }
            }

            e.setKickMessage(MinigamesMessages.serverFull);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        String message;
        if (bp.getTeam() != null) {
            if (bp.getTeam().getMode().getStateManager().getActualGameState() == GameStateManager.GameState.INGAME)
                return;
            message = MinigamesMessages.chatMessageLobby.replaceAll("%p", bp.getTeam().getColorcode() + bp.getName()).replaceAll("%msg", e.getMessage());
        } else {
            message = MinigamesMessages.chatMessageLobby.replaceAll("%p", "\u00A77" + bp.getName()).replaceAll("%msg", e.getMessage());
        }
        e.setCancelled(true);
        for (Player spec : Bukkit.getOnlinePlayers()) {
            if (spec.getWorld().equals(GameMode.getSpawn(isVersus).getWorld()))
                spec.sendMessage(message);
        }
    }

    public static boolean inLobbyOrSpectator(BiomiaPlayer bp) {
        return bp.getTeam() == null || bp.getTeam().getMode().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME || bp.getTeam().getMode().isSpectator(bp);
    }
}
