package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.events.game.GameDeathEvent;
import de.biomia.spigot.events.game.GameKillEvent;
import de.biomia.spigot.events.game.GameLeaveEvent;
import de.biomia.spigot.messages.MinigamesItemNames;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.general.Dead;
import de.biomia.spigot.minigames.general.Scoreboards;
import de.biomia.spigot.tools.BackToLobby;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.universal.Messages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
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
    public void onJoin(PlayerJoinEvent e) {
        if (mode.getInstance().getType().isVersus()) return;

        Player p = e.getPlayer();
        p.getInventory().clear();
        BackToLobby.getLobbyItem(p, 8);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        bp.setDangerous(false);
        bp.setDamageable(false);

        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            Dead.setDead(bp);
            p.teleport(new Location(Bukkit.getWorld(MinigamesConfig.getMapName()), 0, 100, 0));

        } else if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.LOBBY) {

            mode.getInstance().registerPlayer(Biomia.getBiomiaPlayer(e.getPlayer()));
            p.teleport(GameMode.getSpawn(false));

            if (bp.isSrStaff() || bp.isModerator()) {
                if (mode.getInstance().getType() == GameType.SKY_WARS)
                    p.getInventory().setItem(1, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, MinigamesItemNames.startItem));
                else
                    p.getInventory().setItem(0, ItemCreator.itemCreate(Material.SPECTRAL_ARROW, MinigamesItemNames.startItem));
            }

            p.getInventory().setItem(4, ItemCreator.itemCreate(Material.WOOL, MinigamesItemNames.teamWaehlerItem));

            bp.getPlayer().setLevel(mode.getStateManager().getLobbyState().getCountDown());

            Bukkit.broadcastMessage((bp.isPremium() ? "§6" : "§7") + p.getName() + MinigamesMessages.joinedTheGame);

            mode.partyJoin(bp);
            Scoreboards.setLobbyScoreboard(p);
            Scoreboards.lobbySB.getTeam("xnoteam").addEntry(p.getName());
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getEntity().getWorld())) return;
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getEntity());
            BiomiaPlayer damager = Biomia.getBiomiaPlayer((Player) e.getDamager());
            if (bp.getTeam() != null && damager.getTeam() != null)
                if (bp.getTeam().equals(damager.getTeam()))
                    e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        GameTeam team = bp.getTeam();
        String msg = e.getMessage();
        String format;
        if (e.isCancelled()) return;
        e.setCancelled(true);
        if (bp.isPremium() || bp.isStaff())
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

                    format = MinigamesMessages.chatMessageAll.replaceAll("%p", team.getColorcode() + p.getDisplayName())
                            .replaceAll("%msg", msg);

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        BiomiaPlayer allbps = Biomia.getBiomiaPlayer(all);
                        if (mode.getInstance().containsPlayer(allbps) || mode.isSpectator(allbps)) {
                            all.sendMessage(format);
                        }
                    }

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
                for (Player spec : Bukkit.getOnlinePlayers()) {
                    BiomiaPlayer specbp = Biomia.getBiomiaPlayer(spec);
                    if (mode.isSpectator(specbp)) {
                        spec.sendMessage(format);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (!mode.getInstance().containsPlayer(bp))
            return;
        if (e.getFrom().equals(mode.getInstance().getWorld())) {
            mode.getInstance().removePlayer(bp);
            if (bp.getTeam() != null)
                bp.getTeam().leave(bp);
            if (!WarteLobbyListener.inLobbyOrSpectator(bp)) {
                Bukkit.getPluginManager().callEvent(new GameLeaveEvent(bp, mode));
                mode.getInstance().getPlayers().forEach(each -> each.sendMessage(bp.getTeam().getColorcode() + e.getPlayer().getName() + MinigamesMessages.leftTheGame));
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        mode.getInstance().removePlayer(bp);
        if (bp.getTeam() != null)
            bp.getTeam().leave(bp);
        if (!WarteLobbyListener.inLobbyOrSpectator(bp)) {
            Bukkit.getPluginManager().callEvent(new GameLeaveEvent(bp, mode));
            e.setQuitMessage(bp.getTeam().getColorcode() + e.getPlayer().getName() + MinigamesMessages.leftTheGame);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME && mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) {
            if (e.getTo().getBlockY() <= 0) {
                e.getPlayer().setHealth(0);
                Dead.respawn(e.getPlayer());
            }
        } else if (e.getTo().getBlockY() <= 20)
            e.getPlayer().teleport(GameMode.getSpawn(mode.getInstance().getType().isVersus()));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (!mode.getInstance().getWorld().equals(e.getPlayer().getWorld())) return;
        Player p = e.getPlayer();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (mode.getStateManager().getActualGameState() == GameStateManager.GameState.INGAME) {
            if (bp.getTeam() != null) {
                e.setRespawnLocation(bp.getTeam().getHome());
            } else if (mode.isSpectator(bp)) {
                e.setRespawnLocation(mode.getInstance().getWorld().getSpawnLocation().add(0, 100, 0));
            } else {
                e.setRespawnLocation(GameMode.getSpawn(mode.getInstance().getType().isVersus()));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
        if (mode.getStateManager().getActualGameState() != GameStateManager.GameState.INGAME)
            e.setCancelled(true);
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
    public void onInteractAt(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.ARMOR_STAND) onInteract(e);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);
        if (WarteLobbyListener.inLobbyOrSpectator(bp) && e.getCurrentItem() != null && !bp.isInBuildmode()) {
            e.setCancelled(true);
            e.setCursor(new ItemStack(Material.AIR));
        }
        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.getClickedInventory().getName().equals(mode.getTeamSwitcher().getName())) {
                    //noinspection deprecation
                    mode.getTeamFromData(e.getCurrentItem().getData().getData()).join(bp);
                    e.setCancelled(true);
                    p.closeInventory();
                }
            }
        }
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
        if (!mode.getInstance().getWorld().equals(e.getEntity().getWorld())) return;
        Player p = e.getEntity();
        Player killer = p.getKiller();
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(p);

        if (!mode.isSpectator(bp)) {
            BiomiaPlayer bpKiller;
            if (killer != null) {
                bpKiller = Biomia.getBiomiaPlayer(killer);
                Bukkit.getPluginManager().callEvent(new GameKillEvent(bpKiller, bp, true, mode));
                e.setDeathMessage(Messages.format(MinigamesMessages.playerKilledByPlayer, bp.getTeam().getColorcode() + p.getName(), bpKiller.getTeam().getColorcode() + killer.getName()));
            } else {
                bpKiller = null;
                e.setDeathMessage(Messages.format(MinigamesMessages.playerDied, bp.getTeam().getColorcode() + p.getName()));
            }
            Bukkit.getPluginManager().callEvent(new GameDeathEvent(bp, bpKiller, true, mode));
            bp.getTeam().setDead(bp);
        }
    }
}
