package de.biomia.spigot.specialEvents.schnitzelEvent;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.commands.BiomiaCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Checkpoint extends BiomiaCommand {

    Checkpoint() {
        super("checkpoint", "cp");
    }

    private static final HashMap<BiomiaPlayer, CheckpointRunnable> checkPoints = new HashMap<>();

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("set")) {
                CheckpointRunnable cp = checkPoints.get(bp);
                if (cp.setLastSavedLocation()) {
                    bp.sendMessage("§cCheckpoint manuell gespeichert§7.");
                    if (stopSave(bp)) {
                        bp.sendMessage("§cCheckpoint wird nun nicht mehr automatisch gespeichert§7!");
                        bp.sendMessage("§cFür automatisches Speichern benutze §b/checkpoint auto§7!");
                    }
                } else {
                    bp.sendMessage("§cCheckpoint wird weiterhin automatisch gespeichert§7!");
                }
            } else if (args[0].equalsIgnoreCase("auto") || args[0].equalsIgnoreCase("automatic")) {
                startSave(bp);
                bp.sendMessage("§cCheckpoint wird automatisch alle §b2 §cMinuten gespeichert§7.");
            } else {
                bp.sendMessage("§c/checkpoint §7<§bset §7/ §bauto§7>");
            }
        } else {
            Location loc = getLastSavedLocation(bp);
            if (loc == null) {
                bp.sendMessage("§cDu kannst nur alle §b30 §cSekunden zu deinem Checkpoint zurückkehren§7!");
            } else {
                bp.getPlayer().teleport(loc);
                bp.sendMessage("§cZu Checkpoint teleportiert§7!");
            }
        }
        return true;
    }

    public static void startSave(BiomiaPlayer bp) {
        CheckpointRunnable cp = checkPoints.computeIfAbsent(bp, checkPoint -> new CheckpointRunnable(bp));
        try {
            cp.cancel();
            checkPoints.put(bp, cp = new CheckpointRunnable(bp));
        } catch (IllegalStateException ignored) {
        }
        cp.runTaskTimer(Main.getPlugin(), 20, 20 * 60 * 2);
    }

    private static boolean stopSave(BiomiaPlayer bp) {
        CheckpointRunnable cp = checkPoints.get(bp);
        try {
            cp.cancel();
        } catch (IllegalStateException ignored) {
            return false;
        }
        return true;
    }

    public static Location getLastSavedLocation(BiomiaPlayer bp) {
        return checkPoints.get(bp).getLastSavedLocation();
    }
}

class CheckpointRunnable extends BukkitRunnable {

    private BiomiaPlayer bp;
    private Location lastLoc = null;

    private int lastTeleport = 0;
    private int lastSave = 0;


    CheckpointRunnable(BiomiaPlayer bp) {
        this.bp = bp;
    }

    public Location getLastSavedLocation() {
        int now = (int) (System.currentTimeMillis() / 1000);
        if (now - lastTeleport > 30) {
            lastTeleport = now;
            return lastLoc;
        }
        return null;
    }

    @Override
    public void run() {
        if (bp.getPlayer() != null) {
            this.lastLoc = bp.getPlayer().getLocation();
        } else {
            cancel();
        }
    }

    public boolean setLastSavedLocation() {
        int now = (int) (System.currentTimeMillis() / 1000);
        if (now - lastSave > 30) {
            lastSave = now;
            this.lastLoc = bp.getPlayer().getLocation();
            return true;
        } else {
            bp.sendMessage("§cDu kannst nur alle §b30 §cSekunden deinen Checkpoint speichern§7!");
            return false;
        }
    }
}
