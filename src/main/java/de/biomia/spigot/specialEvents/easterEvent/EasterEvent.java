package de.biomia.spigot.specialEvents.easterEvent;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.achievements.Stats;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

import static de.biomia.spigot.tools.HeadCreator.setSkullUrl;

public class EasterEvent implements Listener {

    private static final int specialEggsAmount = 5;
    private static final String specialEggName = "ei_rot";
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
        switch (Main.getGroupName()) {
        //TODO addSpecialLocations
        case "Lobby":
            world = "LobbyBiomia";
            location = new Location(Bukkit.getWorld(world), 532, 112, 300);
            specialEggLocation = new Location(Bukkit.getWorld(world), 595, 74, 298);
            radius = 80;
            maxHight = 112;
            randomEggsPerServer = 20;
            break;
        case "QuestServer":
            world = "Quests";
            location = new Location(Bukkit.getWorld(world), 114, 0, -288);
            specialEggLocation = new Location(Bukkit.getWorld(world), 141, 66, -258);
            radius = 50;
            maxHight = 85;
            randomEggsPerServer = 4;
            break;
        case "BauWelt":
            world = "BauWelt";
            location = new Location(Bukkit.getWorld(world), 0, 0, 0);
            specialEggLocation = new Location(Bukkit.getWorld(world), 11, 67, 19);
            radius = 150;
            maxHight = 77;
            randomEggsPerServer = 5;
            break;
        case "SkyWars":
            world = "Spawn";
            location = new Location(Bukkit.getWorld(world), 0, 0, 0);
            specialEggLocation = new Location(Bukkit.getWorld(world), 0, 71, 51);
            radius = 50;
            maxHight = 86;
            randomEggsPerServer = 3;
            break;
        case "BedWars":
            world = "Spawn";
            location = new Location(Bukkit.getWorld(world), 0, 0, 0);
            specialEggLocation = new Location(Bukkit.getWorld(world), 45, 85, 37);
            radius = 50;
            maxHight = 86;
            randomEggsPerServer = 3;
            break;
        case "FreebuildServer":
            world = "world";
            location = new Location(Bukkit.getWorld(world), -261, 64, 350);
            specialEggLocation = new Location(Bukkit.getWorld(world), -255, 65, 323);
            radius = 150;
            maxHight = 75;
            randomEggsPerServer = 4;
            break;
        default:
            location = null;
            maxHight = 0;
            radius = 0;
            specialEggLocation = null;
            randomEggsPerServer = 0;
            return;
        }
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
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
                    } while (loc.getY() > maxHight && loc.getY() != 1);

                    Block b = loc.getBlock();
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
        }.runTaskTimer(Main.getPlugin(), 0, 20 * 60 * 2);
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
                int eggsFound = Stats.getStat(Stats.BiomiaStat.SpecialEggsFound, e.getPlayer(), Main.getGroupName());
                if (eggsFound == 0) {
                    Stats.incrementStat(Stats.BiomiaStat.SpecialEggsFound, e.getPlayer(), Main.getGroupName());
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

    public void giveReward(OfflineBiomiaPlayer p) {

        int eggsFound = Stats.getStat(Stats.BiomiaStat.EasterEggsFound, p.getBiomiaPlayerID());
        int eggsRewardsErhalten = Stats.getStat(Stats.BiomiaStat.EasterRewardsErhalten, p.getBiomiaPlayerID());

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

                if (p.isOnline())
                    if (missingEggs == 1) {
                        p.getBiomiaPlayer().getPlayer().sendMessage("Finde mindestens noch 1 weiteres Ei!");
                    } else {
                        p.getBiomiaPlayer().getPlayer().sendMessage("Finde mindestens " + missingEggs + " weitere Eier!");
                    }
            } else {
                eggsAbgegeben += 10;
                b = true;
                coinsToAdd += (100 + eggsAbgegeben + eggsRewardsErhalten) * 10;
            }
        } while (b);
        Stats.incrementStatBy(Stats.BiomiaStat.EasterRewardsErhalten, p.getBiomiaPlayerID(), eggsAbgegeben);
        if (p.isOnline())
            p.addCoins(coinsToAdd, false);
    }
}
