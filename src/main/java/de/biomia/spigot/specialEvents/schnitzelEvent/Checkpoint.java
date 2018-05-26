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
    public void onCommand(CommandSender sender, String label, String[] args) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer((Player) sender);

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("set")) {
                CheckpointRunnable cp = checkPoints.get(bp);
                if (cp.setLastSavedLocation()) {
                    bp.sendMessage("\u00A7cCheckpoint manuell gespeichert\u00A77.");
                    if (stopSave(bp)) {
                        bp.sendMessage("\u00A7cCheckpoint wird nun nicht mehr automatisch gespeichert\u00A77!");
                        bp.sendMessage("\u00A7cF00dcr automatisches Speichern benutze \u00A7b/checkpoint auto\u00A77!");
                    }
                } else {
                    bp.sendMessage("\u00A7cCheckpoint wird weiterhin automatisch gespeichert\u00A77!");
                }
            } else if (args[0].equalsIgnoreCase("auto") || args[0].equalsIgnoreCase("automatic")) {
                startSave(bp);
                bp.sendMessage("\u00A7cCheckpoint wird automatisch alle \u00A7b2 \u00A7cMinuten gespeichert\u00A77.");
            } else {
                bp.sendMessage("\u00A7c/checkpoint \u00A77<\u00A7bset \u00A77/ \u00A7bauto\u00A77>");
            }
        } else {
            Location loc = getLastSavedLocation(bp);
            if (loc == null) {
                bp.sendMessage("\u00A7cDu kannst nur alle \u00A7b30 \u00A7cSekunden zu deinem Checkpoint zur00dcckkehren\u00A77!");
            } else {
                bp.getPlayer().teleport(loc);
                bp.sendMessage("\u00A7cZu Checkpoint teleportiert\u00A77!");
            }
        }
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

    private final BiomiaPlayer bp;
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
            bp.sendMessage("\u00A7cDu kannst nur alle \u00A7b30 \u00A7cSekunden deinen Checkpoint speichern\u00A77!");
            return false;
        }
    }
}
