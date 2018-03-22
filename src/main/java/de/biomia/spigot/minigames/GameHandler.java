package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.minigames.general.Scoreboards;
import de.biomia.spigot.minigames.general.Teleport;
import de.biomia.spigot.minigames.skywars.SkyWars;
import de.biomia.spigot.tools.BackToLobby;
import de.biomia.spigot.tools.ItemCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.logging.Level;

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
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        BackToLobby.getLobbyItem(p, 8);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDamageEntitys(false);
        bp.setGetDamage(false);

        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {

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

            p.teleport(new Location(Bukkit.getWorld(MinigamesConfig.getMapName()), 0, 100, 0));

        } else if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {

            p.teleport(GameMode.getSpawn());

            if (p.hasPermission("biomia.sw.start")) {
                p.getInventory().setItem(0, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, BedWarsItemNames.startItem));
            }

            p.getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, BedWarsItemNames.teamWaehlerItem));

            bp.getPlayer().setLevel(mode.getStateManager().getLobbyState().getCountDown());

            Bukkit.broadcastMessage(bp.isPremium() ? "\u00A76" : "\u00A77" + p.getName() + MinigamesMessages.joinedTheGame);

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
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        GameTeam team = bp.getTeam();
        String msg = e.getMessage();
        String format;
        if (p.hasPermission("biomia.coloredchat"))
            msg = ChatColor.translateAlternateColorCodes('&', e.getMessage());

        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {

            if (team != null) {
                if (e.getMessage().startsWith("@")) {

                    msg = msg.replaceAll("@all ", "");
                    msg = msg.replaceAll("@all", "");
                    msg = msg.replaceAll("@a ", "");
                    msg = msg.replaceAll("@a", "");
                    msg = msg.replaceAll("@ ", "");
                    msg = msg.replaceAll("@", "");

                    e.setFormat(MinigamesMessages.chatMessageAll.replaceAll("%p", team.getColorcode() + p.getDisplayName())
                            .replaceAll("%msg", msg));
                } else {
                    e.setCancelled(true);
                    format = MinigamesMessages.chatMessageTeam.replaceAll("%p", team.getColorcode() + p.getDisplayName())
                            .replaceAll("%msg", msg);
                    for (BiomiaPlayer teamPlayer : team.getPlayers()) {
                        teamPlayer.sendMessage(format);
                    }
                }
            } else {
                format = MinigamesMessages.chatMessageDead.replaceAll("%p", p.getDisplayName()).replaceAll("%msg", msg);
                e.setCancelled(true);
                for (Player spec : Bukkit.getOnlinePlayers()) {

                    BiomiaPlayer specbp = Biomia.getBiomiaPlayer(spec);
                    if (!mode.getInstance().containsPlayer(specbp) || !specbp.getTeam().lives(bp)) {
                        spec.sendMessage(format);
                    }
                }

            }
        } else if (team != null) {
            format = MinigamesMessages.chatMessageLobby.replaceAll("%p", team.getColorcode() + p.getDisplayName())
                    .replaceAll("%msg", msg);
            e.setFormat(format);
        } else {
            format = MinigamesMessages.chatMessageLobby.replaceAll("%p", "\u00A77" + p.getDisplayName()).replaceAll("%msg", msg);
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
        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            if (e.getTo().getBlockY() <= 0) {
                e.getPlayer().setHealth(0);
                Dead.respawn(e.getPlayer());
                return;
            }
            BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
            Location loc = Teleport.getStartLocation(bp);
            if (loc != null && loc.distance(e.getTo()) > .5) {
                Teleport.removeFromStartLocs(bp);
            }
        } else if (e.getTo().getBlockY() <= 20) {
            e.getPlayer().teleport(GameMode.getSpawn());
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

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent e) {
        e.setSpawnLocation(new Location(Bukkit.getWorld("Spawn"), 0.5, 75, -0.5, 40, 0));
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (SkyWars.getSkyWars().getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        }

        if (e.getRightClicked() instanceof ArmorStand) {
            for (GameTeam allteams : SkyWars.getSkyWars().getTeams()) {

                Entity entity = mode.getJoiner().get(allteams.getColor());
                if (e.getRightClicked().equals(entity)) {
                    allteams.join(bp);
                    return;
                }
            }
        }
    }
}
