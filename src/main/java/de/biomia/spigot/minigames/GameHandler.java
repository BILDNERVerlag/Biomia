package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.events.game.GameDeathEvent;
import de.biomia.spigot.events.game.GameEndEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import de.biomia.spigot.events.game.GameLeaveEvent;
import de.biomia.spigot.messages.MinigamesItemNames;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.minigames.general.Scoreboards;
import de.biomia.spigot.minigames.general.Teleport;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

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
    public void onKill(GameKillEvent e) {
        GameRewards.KILL.giveReward(e.getBiomiaPlayer().getBiomiaPlayer(), e.getMode().getInstance());
    }

    @EventHandler
    public void onWin(GameEndEvent e) {
        e.getWinner().forEach(each -> {
            GameRewards.WIN.giveReward(each, e.getMode().getInstance());
            GameRewards.PLAYED.giveReward(each, e.getMode().getInstance());
        });
    }

    @EventHandler
    public void onPlayed(GameLeaveEvent e) {
        GameRewards.PLAYED.giveReward(e.getBiomiaPlayer().getBiomiaPlayer(), e.getMode().getInstance());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        p.getInventory().clear();
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

            mode.getInstance().registerPlayer(Biomia.getBiomiaPlayer(e.getPlayer()));
            p.teleport(GameMode.getSpawn());

            if (p.hasPermission("biomia.minigames.start")) {
                p.getInventory().setItem(1, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, MinigamesItemNames.startItem));
            }

            p.getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, MinigamesItemNames.teamWaehlerItem));

            bp.getPlayer().setLevel(mode.getStateManager().getLobbyState().getCountDown());

            Bukkit.broadcastMessage(bp.isPremium() ? "\u00A76" : "\u00A77" + p.getName() + MinigamesMessages.joinedTheGame);

            mode.partyJoin(bp);
            Scoreboards.setLobbyScoreboard(p);
            Scoreboards.lobbySB.getTeam("xnoteam").addEntry(p.getName());
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getEntity());
            BiomiaPlayer damager = Biomia.getBiomiaPlayer((Player) e.getDamager());
            if (!mode.isSpectator(bp) && !mode.isSpectator(damager))
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
            if (!WaitingLobbyListener.inLobbyOrSpectator(bp)) {
                Bukkit.getPluginManager().callEvent(new GameLeaveEvent(bp, mode));
                mode.getInstance().removePlayer(bp);
                mode.getInstance().getPlayers().forEach(each -> each.sendMessage(bp.getTeam().getColorcode() + e.getPlayer().getName() + MinigamesMessages.leftTheGame));
                if (bp.getTeam() != null) {
                    bp.getTeam().leave(bp);
                }
            }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (!WaitingLobbyListener.inLobbyOrSpectator(bp)) {
            Bukkit.getPluginManager().callEvent(new GameLeaveEvent(bp, mode));
            e.setQuitMessage(bp.getTeam().getColorcode() + e.getPlayer().getName() + MinigamesMessages.leftTheGame);
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

        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            if (!mode.getInstance().containsPlayer(bp) || !bp.getTeam().lives(bp)) {
                e.setRespawnLocation(bp.getTeam().getHome());
            } else
                e.setRespawnLocation(new Location(Bukkit.getWorld(MinigamesConfig.getMapName()), 0, 100, 0));
        } else {
            if (bp.getTeam() != null) {
                e.setRespawnLocation(bp.getTeam().getHome());
            } else {
                e.setRespawnLocation(GameMode.getSpawn());
            }
        }

        if (mode.getInstance().containsPlayer(bp) && bp.getPlayer().getWorld().equals(mode.getInstance().getWorld())) {
            e.setRespawnLocation(mode.getInstance().getWorld().getSpawnLocation().add(0, 100, 0));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME) {
            e.setCancelled(true);
        }

        if (e.getRightClicked() instanceof ArmorStand) {
            for (GameTeam allteams : mode.getTeams()) {

                Entity entity = mode.getJoiner().get(allteams.getColor());
                if (e.getRightClicked().equals(entity)) {
                    allteams.join(bp);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (!e.isRepair())
            e.getInventory().setResult(ItemCreator.itemCreate(Material.AIR));
    }

    @EventHandler
    public void onProjectileHit(PlayerEggThrowEvent event) {
        event.setHatching(false);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        if (e.getItem() != null) {
            if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {

                String displayname = e.getItem().getItemMeta().getDisplayName();
                switch (displayname) {
                case MinigamesItemNames.teamWaehlerItem:
                    p.openInventory(mode.getTeamSwitcher());
                    break;
                case MinigamesItemNames.startItem:
                    if (mode.getStateManager().getLobbyState().getCountDown() > 5)
                        mode.getStateManager().getLobbyState().setCountDown(5);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        Player killer = p.getKiller();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (!mode.isSpectator(bp)) {
            BiomiaPlayer bpKiller;
            if (killer != null) {
                bpKiller = Biomia.getBiomiaPlayer(killer);
                Bukkit.getPluginManager().callEvent(new GameKillEvent(bpKiller, bp, true, mode));
                e.setDeathMessage(MinigamesMessages.playerKilledByPlayer.replace("%p1", bp.getTeam().getColorcode() + p.getName()).replace("%p2", bpKiller.getTeam().getColorcode() + killer.getName()));
            } else {
                bpKiller = null;
                e.setDeathMessage(MinigamesMessages.playerDied.replace("%p", bp.getTeam().getColorcode() + p.getName()));
            }
            bp.getTeam().setDead(bp);
            Bukkit.getPluginManager().callEvent(new GameDeathEvent(bp, bpKiller, true, mode));
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelInvClick(InventoryClickEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getWhoClicked());
        if (WaitingLobbyListener.inLobbyOrSpectator(bp) && e.getCurrentItem() != null && !bp.canBuild()) {
            e.setCancelled(true);
            e.setCursor(new ItemStack(Material.AIR));
        }
    }
}
