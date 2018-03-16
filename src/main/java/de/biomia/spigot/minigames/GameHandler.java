package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.BedWarsMessages;
import de.biomia.spigot.minigames.bedwars.BedWars;
import de.biomia.spigot.minigames.general.Scoreboards;
import de.biomia.spigot.minigames.bedwars.Variables;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.tools.BackToLobby;
import de.biomia.spigot.tools.ItemCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

public abstract class GameHandler implements Listener {

    protected final GameMode mode;

    protected GameHandler(GameMode mode) {
        this.mode = mode;
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onJoin_(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        BackToLobby.getLobbyItem(p, 8);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDamageEntitys(false);
        bp.setGetDamage(false);

        if (BedWars.getBedWars().getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {

            // Hide
            for (Player all : Bukkit.getOnlinePlayers()) {

                GameTeam team = bp.getTeam();

                if (team != null && team.lives(bp)) {
                    all.hidePlayer(p);
                } else {
                    all.showPlayer(all);
                }
            }

            // Disable Damage / Build
            bp.setGetDamage(false);
            bp.setDamageEntitys(false);
            bp.setBuild(false);
            p.setGameMode(org.bukkit.GameMode.ADVENTURE);

            // Fly settings
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setFlySpeed(0.5F);

            Scoreboards.setSpectatorSB(p);
            Scoreboards.spectatorSB.getTeam("spectator").addEntry(p.getName());

            p.teleport(new Location(Bukkit.getWorld(Variables.name), 0, 100, 0));

        } else if (BedWars.getBedWars().getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {

            p.teleport(Variables.warteLobbySpawn);

            if (p.hasPermission("biomia.sw.start")) {
                p.getInventory().setItem(0, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, BedWarsItemNames.startItem));
            }

            p.getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, BedWarsItemNames.teamWaehlerItem));

            bp.getPlayer().setLevel(BedWars.getBedWars().getStateManager().getLobbyState().getCountDown());

            if (bp.isPremium()) {
                Bukkit.broadcastMessage("\u00A76" + p.getName() + BedWarsMessages.joinedTheGame);
            } else {
                Bukkit.broadcastMessage("\u00A77" + p.getName() + BedWarsMessages.joinedTheGame);
            }

            Scoreboards.setLobbyScoreboard(p);
            Scoreboards.lobbySB.getTeam("xnoteam").addEntry(p.getName());
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getEntity());
            BiomiaPlayer damager = Biomia.getBiomiaPlayer((Player) e.getDamager());
            if (mode.getInstance().containsPlayer(bp) && mode.getInstance().containsPlayer(damager))
                if (bp.getTeam().equals(damager.getTeam()))
                    e.setCancelled(true);
        }
    }

    @EventHandler
    public final void onChat(AsyncPlayerChatEvent e) {
        Bukkit.broadcastMessage("%%%gamehandler.onchat");
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        GameTeam team = bp.getTeam();
        String msg = e.getMessage();
        String format;
        if (mode.getInstance().containsPlayer(bp)) {
            if (p.hasPermission("biomia.coloredchat"))
                msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());
            if (e.getMessage().startsWith("@")) {
                msg = msg.replaceAll("@all ", "");
                msg = msg.replaceAll("@all", "");
                msg = msg.replaceAll("@a ", "");
                msg = msg.replaceAll("@a", "");
                msg = msg.replaceAll("@ ", "");
                msg = msg.replaceAll("@", "");
                format = BedWarsMessages.chatMessageAll.replaceAll("%p", team.getColorcode() + p.getDisplayName()).replaceAll("%msg", msg);
            } else if (team != null) {
                e.setCancelled(true);
                format = BedWarsMessages.chatMessageTeam.replaceAll("%p", team.getColorcode() + p.getDisplayName()).replaceAll("%msg", msg);
                for (BiomiaPlayer teamPlayer : team.getPlayers()) {
                    teamPlayer.getPlayer().sendMessage(format);
                }
                return;
            } else {
                format = BedWarsMessages.chatMessageDead.replaceAll("%p", p.getDisplayName()).replaceAll("%msg", msg);
            }
            e.setFormat(format);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (e.getFrom().equals(mode.getInstance().getWorld()))
            if (mode.getInstance().containsPlayer(bp))
                if (bp.getTeam() != null)
                    bp.getTeam().leave(bp);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (mode.getInstance().containsPlayer(bp)) {
            if (bp.getTeam() != null)
                bp.getTeam().leave(bp);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (mode.getInstance().containsPlayer(bp)) {
            if (e.getTo().getBlockY() <= 0) {
                e.getPlayer().setHealth(0);
                Dead.respawn(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (mode.getInstance().containsPlayer(bp) && bp.getPlayer().getWorld().equals(mode.getInstance().getWorld())) {
            e.setRespawnLocation(mode.getInstance().getWorld().getSpawnLocation().add(0, 100, 0));
        }
    }
}
