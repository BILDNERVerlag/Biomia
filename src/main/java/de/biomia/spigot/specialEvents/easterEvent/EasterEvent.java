package de.biomia.spigot.specialEvents.easterEvent;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.achievements.Stats;
import de.biomia.spigot.tools.HeadCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class EasterEvent implements Listener {

    private static final int specialEggsAmount = 6;
    //private static final String specialEggName = "ei_gold";
    //private static final String egg1Name = "ei_blaugruen";
    //private static final String egg2Name = "ei_rot";
    //private static final String egg3Name = "ei_gepunktet";
    //private static final String egg4Name = "ei_gestreift";

    private static final String specialEggName = "4e693cf3b7bf83248527aacc2714638b55587cde593963d1f861884f5be1638";
    private static final String egg1Name = "a448ad2dc04a753b88554f9aa9201846dde958331b072877565fda7f18f";
    private static final String egg2Name = "6ffdf1348c5284bbdf0e4acbcce9d102b3efd63e4f4f54684b2f48f8d41c70";
    private static final String egg3Name = "cbc38a7342f923460d980ec331d546862be119decc8af3b796bdb1cdb8b9f";
    private static final String egg4Name = "fa93594996be9526fd6e7afd109ea77170aff388311343ebc4e4f42a840";

    private final int randomEggsPerServer;
    private final Location location;
    private final int radius;
    private final int maxHight;
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final Location specialEggLocation;

    public EasterEvent() {
        final String world;
        switch (Main.getGroupName()) {
            case "Lobby":
                world = "LobbyBiomia";
                location = new Location(Bukkit.getWorld(world), 532, 112, 300);
                specialEggLocation = new Location(Bukkit.getWorld(world), 595, 74, 298);
                radius = 80;
                maxHight = 112;
                randomEggsPerServer = 20;
                break;
            case "TestQuest":
            case "QuestServer":
                world = "Quests";
                location = new Location(Bukkit.getWorld(world), 114, 0, -288);
                specialEggLocation = new Location(Bukkit.getWorld(world), 141, 66, -259);
                radius = 50;
                maxHight = 85;
                randomEggsPerServer = 5;
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
        HeadCreator.setSkullUrl(specialEggName, specialEggLocation.getBlock());
    }

    public void disable() {
        //despawn eggs
        blocks.forEach(each -> {
            if (each.getType() == Material.SKULL)
                each.setType(Material.AIR);
        });
        blocks.clear();
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
                        int degree = new Random().nextInt(360);

                        loc.add(r * Math.cos(degree), 0, r * Math.sin(degree));
                        Material highestBlockType = loc.getWorld().getHighestBlockAt(loc).getLocation().subtract(0, 1, 0).getBlock().getType();

                        if (highestBlockType != Material.DIRT && highestBlockType != Material.GRASS && highestBlockType != Material.STONE) {
                            i--;
                            continue;
                        }

                        loc = loc.getWorld().getHighestBlockAt(loc).getLocation();
                    } while (loc.getY() > maxHight && loc.getY() != 1);

                    Block b = loc.getBlock();

                    switch (new Random().nextInt(4)) {
                        case 0:
                            HeadCreator.setSkullUrl(egg1Name, b);
                            break;
                        case 1:
                            HeadCreator.setSkullUrl(egg2Name, b);
                            break;
                        case 2:
                            HeadCreator.setSkullUrl(egg3Name, b);
                            break;
                        case 3:
                            HeadCreator.setSkullUrl(egg4Name, b);
                            break;
                    }
                    blocks.add(b);
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20 * 60 * 2);
    }

    @EventHandler
    public void onEggClick(PlayerInteractEvent e) {
        if ((e.getAction() == Action.LEFT_CLICK_BLOCK || e.getHand().equals(EquipmentSlot.HAND)) && e.getClickedBlock().getType() == Material.SKULL) {

            BiomiaPlayer bp = Biomia.getBiomiaPlayer(e.getPlayer());
            if (blocks.contains(e.getClickedBlock())) {
                blocks.remove(e.getClickedBlock());
                e.getClickedBlock().setType(Material.AIR);
                Stats.incrementStat(Stats.BiomiaStat.EasterEggsFound, bp.getBiomiaPlayerID());
                bp.getPlayer().sendMessage("\u00A7cDu hast dein \u00A7b" + Stats.getStat(Stats.BiomiaStat.EasterEggsFound, bp.getBiomiaPlayerID()) + ".\u00A7c Osterei gefunden!");
                bp.getPlayer().sendMessage("\u00A77(Der Osterhase in der Lobby verteilt Belohnungen...)");
            } else if (e.getClickedBlock().getLocation().distance(specialEggLocation) < 0.5) {
                int specialEggsFoundOnThisServer = Stats.getStat(Stats.BiomiaStat.SpecialEggsFound, e.getPlayer(), Main.getGroupName());
                if (specialEggsFoundOnThisServer == 0) {
                    Stats.incrementStat(Stats.BiomiaStat.SpecialEggsFound, e.getPlayer(), Main.getGroupName());
                    int specialEggsFoundEverywhere = Stats.getStat(Stats.BiomiaStat.SpecialEggsFound, e.getPlayer());
                    if (specialEggsFoundEverywhere == specialEggsAmount) {
                        e.getPlayer().sendMessage("\u00A7cDu hast \u00A7lalle\u00A7r\u00A7c besonderen Eier gefunden! Daf\u00fcr erh\u00e4ltst du:");
                    } else {
                        e.getPlayer().sendMessage("\u00A7cDu hast das §b" + specialEggsFoundEverywhere + ".§c §bbesondere Ei §cgefunden!");
                        bp.addCoins(500, false);
                        int eggsRemaining = specialEggsAmount - specialEggsFoundEverywhere;

                        if (eggsRemaining == 1) {
                            e.getPlayer().sendMessage("\u00A7c\u00A7lEin\u00A7r\u00A7c besonderes Ei hast du noch nicht gefunden! Finde es und erhalte eine rieeesige Belohnung!");
                        } else {
                            e.getPlayer().sendMessage("\u00A7cDer Osterhase hat noch \u00A7d" + eggsRemaining + " \u00A7cweitere besondere Eier versteckt! Findest du sie?");
                        }
                    }
                } else {
                    e.getPlayer().sendMessage("\u00A7cDu hast das besondere Ei in dieser Welt schon gefunden!\nSuch in einer anderen Welt weiter oder frag den Osterhasen in der Lobby um Rat!");
                }
            }
        }
    }

    // TODO Rewards besprechen
                        /*
                            BESONDERE EIER:
                                je Ei:
                                    500 BC
                                für alle Eier:
                                    1500 BC
                                    exklusives SkyWars-Kit (Eierwerfer-Kit)

                            NORMALE EIER:
                            1:
                                "Du hast tatsächlich eins meiner Ostereier gefunden! Ich hoffe, dir gefallen die Preise!"
                                100 BC
                                1 Beacon (Freebuild)
                                Farbklecks-Hut (Quests)
                            3:
                                "3 Eier schon! Ein starker Start, Gratulation!"
                                150 BC
                                1 Verzauberte Elytra (Freebuild)
                                16 Enderperlen (Quests)
                            5:
                                "5 Eier waren für dich nicht mal eine Herausforderung! Da ist mir direkt nach Tanzen zumute! Hier, etwas musikalisches."
                                200 BC
                                Musikset: (Freebuild)
                                    1 Plattenspieler
                                    1 von jeder Schallplatte
                                1 Totem der Unsterblichkeit (Quests)
                            10:
                                "Schon 10 Eier! Dafür geb ich dir ein paar Namensschilder und den zweiten Teil der Rüstung! Die hast du dir verdient!"
                                250 BC
                                10 Namensschilder (Freebuild)
                                Farbklecks-Rüstung (Quests)
                                exklusives SkyWars-Kit (Farbklecks-Kit)
                            15:
                                "Ich hätte nicht gedacht, dass du so viele findest! Was deine Belohnung angeht... Es heißt ja immer, Erfahrung sei das wertvollste Gut..."
                                300 BC
                                32 Erfahrungsfläschen (Freebuild)
                                Farbklecks-Hose (Quests)
                            25:
                                "So viele Eier! Hier, nimm einen Teil meiner alten Büchersammlung. Ich bin mir sicher, du kannst mehr damit anfangen als ich!"
                                400 BC
                                Verzauberer-Set: (Freebuild)
                                    1 Stack Bücherregale
                                    1 Verzauberungsaltar
                                Farbklecks-Schuhe (Quests)
                            50:
                                "Die Biomia-Corp hat mir diesen Preis gesponsert. Sie wollen wohl junge, aufstrebende Architekten unterstützen."
                                500 BC
                                Builder-Set: (Freebuild)
                                    16 Redstone-Lampen
                                    16 Gemälde
                                    2 Stacks Glas
                                    4 Stacks Ziegelsteine
                                    4 Stacks weissen Trockenbeton
                                    2 Ambosse
                                    1 Endertruhe
                                Verzauberte Elytra (Quests)
                            100:
                                "Dies ist der letzte große Preis! Endlich kann ich mir ein bisschen Ruhe gönnen... Von jetzt an habe ich nur noch Kleinigkeiten für dich."
                                1500 BC
                                Gewinner-Set: (Freebuild)
                                    16 Diamanten
                                    16 Smaragde
                                    3 Totem der Unsterblichkeit
                                    1 Drachenkopf
                                Verzaubertes Diamantschwert (Quests)
                            ab dann in 20er-Schritten:
                                "Du bist schon wieder da? Und hast schon wieder so viele Eier mitgebracht? Warte, ich hab bestimmt noch etwas schönes für dich... Aber wie gesagt, es sind nur noch Kleinigkeiten übrig."
                                200 BC
                                Bonus-Set: (Freebuild)
                                    2 Diamanten
                                    16 Gold
                                1 goldener Apfel (Quests)

                        */

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
        p.addCoins(coinsToAdd, false);
    }
}
