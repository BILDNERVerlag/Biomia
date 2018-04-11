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
                    bp.sendMessage("00A7cCheckpoint manuell gespeichert00A77.");
                    if (stopSave(bp)) {
                        bp.sendMessage("00A7cCheckpoint wird nun nicht mehr automatisch gespeichert00A77!");
                        bp.sendMessage("00A7cF00dcr automatisches Speichern benutze 00A7b/checkpoint auto00A77!");
                    }
                } else {
                    bp.sendMessage("00A7cCheckpoint wird weiterhin automatisch gespeichert00A77!");
                }
            } else if (args[0].equalsIgnoreCase("auto") || args[0].equalsIgnoreCase("automatic")) {
                startSave(bp);
                bp.sendMessage("00A7cCheckpoint wird automatisch alle 00A7b2 00A7cMinuten gespeichert00A77.");
            } else {
                bp.sendMessage("00A7c/checkpoint 00A77<00A7bset 00A77/ 00A7bauto00A77>");
            }
        } else {
            Location loc = getLastSavedLocation(bp);
            if (loc == null) {
                bp.sendMessage("00A7cDu kannst nur alle 00A7b30 00A7cSekunden zu deinem Checkpoint zur00dcckkehren00A77!");
            } else {
                bp.getPlayer().teleport(loc);
                bp.sendMessage("00A7cZu Checkpoint teleportiert00A77!");
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
            bp.sendMessage("00A7cDu kannst nur alle 00A7b30 00A7cSekunden deinen Checkpoint speichern00A77!");
            return false;
        }
    }
}
