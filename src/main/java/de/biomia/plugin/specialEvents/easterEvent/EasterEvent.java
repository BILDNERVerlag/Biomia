package de.biomia.plugin.specialEvents.easterEvent;

import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.achievements.Stats;
import de.biomia.api.main.Main;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

import static de.biomia.api.tools.HeadCreator.setSkullUrl;

public class EasterEvent implements Listener {

    private static final int specialEggsAmount = 5;
    private static final String specialEggName = "";
    private static final String egg1Name = "ei_blaugruen";
    private static final String egg2Name = "ei_rot";
    private static final String egg3Name = "ei_gold";
    private static final String egg4Name = "ei_gestreift";
    private static final String egg5Name = "ei_gepunktet";
    private final int randomEggsPerServer;
    private final Location location;
    private final int radius;
    private final int maxHight;
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final Location specialEggLocation;

    public EasterEvent() {
        final String world;
        switch (de.biomia.api.main.Main.getGroupName()) {
        //TODO addServers
        case "Lobby":
            world = "Spawn";
            randomEggsPerServer = 1;
            location = new Location(Bukkit.getWorld(world), 0, 0, 0);
            specialEggLocation = new Location(Bukkit.getWorld(world), 0, 0, 0);
            radius = 2;
            maxHight = 70;
            break;
        default:
            randomEggsPerServer = 0;
            location = null;
            maxHight = 0;
            radius = 0;
            specialEggLocation = null;
            return;
        }
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);
        startSpawningEggs();
        spawnSpecialEgg();
    }

    private void spawnSpecialEgg() {
        specialEggLocation.getBlock().setType(Material.SKULL);
        SkullMeta meta = (SkullMeta) specialEggLocation.getBlock().getState();
        meta.setOwner(specialEggName);
    }

    private void startSpawningEggs() {
        new BukkitRunnable() {
            public void run() {

                blocks.forEach(each -> {
                    if (each.getType() == Material.SKULL)
                        each.setType(Material.AIR);
                });
                blocks.clear();

                for (int i = 0; i < randomEggsPerServer; i++) {
                    Location loc;
                    do {
                        loc = location.clone();
                        int r = new Random().nextInt(radius);
                        int degree = new Random().nextInt(361);

                        loc.add(r * Math.cos(degree), 0, r * Math.sin(degree));
                        loc = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0, 1, 0);
                    } while (loc.getY() > maxHight);

                    Block b = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0, 1, 0).getBlock();
                    BlockPosition pos = new BlockPosition(1, 2, 3);
                    b.setType(Material.SKULL);
                    Skull s = (Skull) b.getState();
                    s.setSkullType(SkullType.PLAYER);

                    switch (new Random().nextInt(5)) {
                    case 0:
                        setSkullUrl(egg1Name, b);
                        break;
                    case 1:
                        setSkullUrl(egg2Name, b);
                        break;
                    case 2:
                        setSkullUrl(egg3Name, b);
                        break;
                    case 3:
                        setSkullUrl(egg4Name, b);
                        break;
                    case 4:
                        setSkullUrl(egg5Name, b);
                        break;
                    }
                    s.update(true);
                    blocks.add(b);
                }
            }
        }.runTaskTimer(Main.plugin, 0, 20 * 60 * 2);
    }

    @EventHandler
    public void onEggClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.SKULL) {

            BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());

            if (blocks.contains(e.getClickedBlock())) {
                blocks.remove(e.getClickedBlock());
                e.getClickedBlock().setType(Material.AIR);
                Stats.incrementStat(Stats.BiomiaStat.EasterEggsFound, bp.getBiomiaPlayerID());
                bp.getPlayer().sendMessage("\u00A7cDu hast ein Normales Ei gefunden! Schau zum Osterhasen in der Lobby um Belohnungen zu erhalten!");
            } else if (((Skull) e.getClickedBlock().getState()).getOwningPlayer().getName().equals(specialEggName)) {
                int eggsFound = Stats.getStat(Stats.BiomiaStat.SpecialEggsFound, e.getPlayer(), de.biomia.api.main.Main.getGroupName());
                if (eggsFound == 0) {
                    Stats.incrementStat(Stats.BiomiaStat.SpecialEggsFound, e.getPlayer(), de.biomia.api.main.Main.getGroupName());
                    if (Stats.getStat(Stats.BiomiaStat.SpecialEggsFound, e.getPlayer()) == specialEggsAmount) {
                        e.getPlayer().sendMessage("\u00A7cDu hast alle besonderen Eier gefunden! Daf\u00fcr erh\u00fcltst du:");
                        // TODO setWinns ( + nachrichten)
                    } else {
                        e.getPlayer().sendMessage("\u00A7cDu hast ein besonderes Ei gefunden! Als Belohnung erh\u00fcltst du XXXX Coins!");
                        int temp = specialEggsAmount - eggsFound;

                        if (temp == 1) {
                            e.getPlayer().sendMessage("\u00A7cDer Osterhase hat noch ein weites, besonderes Ei versteckt! Finde es und erhalte eine riesen Belohnung!");
                        } else {
                            e.getPlayer().sendMessage("\u00A7cDer Osterhase hat noch \u00A7d" + temp + " \u00A7cweite, besondere Eier versteckt! Findest du sie?");
                        }
                        // TODO setWinns
                    }
                } else {
                    e.getPlayer().sendMessage("\u00A7cDu hast das Besondere Ei in dieser Welt schon Gefunden! Suche in einer anderen weiter!");
                }
            }
        }
    }


    public void addEggs(int bpID, int eggs) {
        Stats.incrementStatBy(Stats.BiomiaStat.EasterEggsFound, bpID, eggs);
    }

    public void giveReward(int bpID) {

        Player p = Bukkit.getPlayer(BiomiaPlayer.getName(bpID));

        int eggsFound = Stats.getStat(Stats.BiomiaStat.EasterEggsFound, bpID);
        int eggsRewardsErhalten = Stats.getStat(Stats.BiomiaStat.EasterRewardsErhalten, bpID);

        boolean b;
        int coinsToAdd = 0;
        int eggsAbgegeben = 0;

        do {
            int eggsWithoutRewards = eggsFound - eggsRewardsErhalten - eggsAbgegeben;

            if (eggsWithoutRewards < 10) {
                b = false;
                if (coinsToAdd == 0)
                    return;
                int missingEggs = 10 - eggsWithoutRewards;
                if (missingEggs == 1) {
                    if (p != null) p.sendMessage("Finde mindestens noch 1 weiteres Ei!");
                } else {
                    if (p != null) p.sendMessage("Finde mindestens " + missingEggs + " weitere Eier!");
                }
            } else {
                eggsAbgegeben += 10;
                b = true;
                coinsToAdd += (100 + eggsAbgegeben + eggsRewardsErhalten) * 10;
            }
        } while (b);
        Stats.incrementStatBy(Stats.BiomiaStat.EasterRewardsErhalten, bpID, eggsAbgegeben);
        if (p != null)
            Biomia.getBiomiaPlayer(p).addCoins(coinsToAdd, false);
    }
}
