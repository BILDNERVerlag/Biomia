package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.listeners.servers.BiomiaListener;
import de.biomia.spigot.messages.KitPVPMessages;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.minigames.kitpvp.KitPVPKit;
import de.biomia.spigot.minigames.kitpvp.KitPVPManager;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.spigot.tools.TeleportExecutor;
import de.biomia.spigot.tools.Teleporter;
import de.biomia.universal.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class WarteLobbyListener extends BiomiaListener {

    private final boolean isVersus;

    public WarteLobbyListener(boolean isVersus) {
        this.isVersus = isVersus;
        if (isVersus) {
            new Teleporter(new Location(Bukkit.getWorld("Spawn"), -51, 100, -44), new Location(Bukkit.getWorld("Spawn"), -13, 200, -6), new TeleportExecutor() {
                @Override
                public void execute(BiomiaPlayer bp) {
                    KitPVPManager.removeFromEditMode(bp);
                }
            }).setInverted();
            new Teleporter(new Location(Bukkit.getWorld("Spawn"), -51, 100, -44), new Location(Bukkit.getWorld("Spawn"), -13, 200, -6), new TeleportExecutor() {
                @Override
                public void execute(BiomiaPlayer bp) {
                    KitPVPManager.setToEditMode(bp);
                }
            });
        }
        Bukkit.getPluginManager().registerEvents(new GameRewardHandler(), Main.getPlugin());
    }

    @EventHandler
    public void onJoin_(PlayerJoinEvent e) {
        if (isVersus) {
            Bukkit.getOnlinePlayers().forEach(each -> {
                if (!each.equals(e.getPlayer()) && Biomia.getBiomiaPlayer(each).getTeam() != null)
                    each.hidePlayer(Main.getPlugin(), e.getPlayer());
            });
            Versus.getInstance().getManager().moveToLobby(e.getPlayer(), true);
            KitPVPManager.load(Biomia.getBiomiaPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getName().equals(KitPVPMessages.selectorInventory) && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            if (e.getCurrentItem().getType() == Material.BARRIER) {
                e.getWhoClicked().sendMessage("§cDu kannst dieses Kit nicht bearbeiten, da du einen zu niedrigen Rang hast!");
                e.setCancelled(true);
                return;
            }
            int kitNum = Integer.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().replace(KitPVPMessages.selectorKitItem.replace("$x", ""), "")) - 1;
            BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) e.getWhoClicked());
            KitPVPKit kit = KitPVPManager.getKit(bp, kitNum);
            if (kit == null)
                kit = new KitPVPKit(bp.getBiomiaPlayerID(), kitNum, e.getWhoClicked().getInventory().getContents(), true);
            KitPVPManager.setMainKit(kit);
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(PlayerSpawnLocationEvent e) {
        Location spawnLoc = GameMode.getSpawn(isVersus);
        if (!spawnLoc.getChunk().isLoaded())
            spawnLoc.getChunk().load();
        e.setSpawnLocation(spawnLoc);
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
