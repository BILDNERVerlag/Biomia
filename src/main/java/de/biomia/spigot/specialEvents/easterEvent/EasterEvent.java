package de.biomia.spigot.specialEvents.easterEvent;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.achievements.Stats;
import de.biomia.spigot.server.quests.sonstigeQuests.Osterhase;
import de.biomia.spigot.tools.HeadCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class EasterEvent implements Listener {

    private static final int specialEggsAmount = 6;

    private static final String eggName = "9fbeb0aa688e97e463b518d4c0c8f5ba822a7386a46de3ca26ce2c5b3137985c";
    private static final String specialEggName = "7be7545297dfd6266bbaa2051825e8879cbfa42c7e7e24e50796f27ca6a18";

    //TODO: osterhasendialog funktioniert noch nicht

    private final int randomEggsPerServer;
    private final Location location;
    private final int radius;
    private final int maxHight;
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final Location specialEggLocation;
    private int delayInMin = 2;

    public EasterEvent() {
        final String world;
        switch (Biomia.getServerInstance().getServerType()) {
        case TestLobby:
        case Lobby:
            world = "LobbyBiomia";
            location = new Location(Bukkit.getWorld(world), 532, 112, 300);
            specialEggLocation = new Location(Bukkit.getWorld(world), 595, 74, 298);
            radius = 80;
            maxHight = 112;
            randomEggsPerServer = 20;
            HeadCreator.setSkullUrl(eggName, new Location(Bukkit.getWorld("LobbyBiomia"), 533.5, 70, 225.5).getBlock());
            HeadCreator.setSkullUrl(eggName, new Location(Bukkit.getWorld("LobbyBiomia"), 531.5, 70, 225.5).getBlock());
            HeadCreator.setSkullUrl(eggName, new Location(Bukkit.getWorld("LobbyBiomia"), 539.5, 70, 225.5).getBlock());
            HeadCreator.setSkullUrl(eggName, new Location(Bukkit.getWorld("LobbyBiomia"), 532.5, 70, 224.5).getBlock());
            HeadCreator.setSkullUrl(specialEggName, new Location(Bukkit.getWorld("LobbyBiomia"), 532.5, 70, 225.5).getBlock());
            Bukkit.getPluginManager().registerEvents(new Osterhase(), Main.getPlugin());
            break;
        case Quest:
        case TestQuest:
            world = "Quests";
            location = new Location(Bukkit.getWorld(world), 114, 0, -288);
            specialEggLocation = new Location(Bukkit.getWorld(world), 141, 66, -259);
            radius = 50;
            maxHight = 85;
            randomEggsPerServer = 5;
            break;
        case BauServer:
            world = "BauWelt";
            location = new Location(Bukkit.getWorld(world), 0, 0, 0);
            specialEggLocation = new Location(Bukkit.getWorld(world), 11, 67, 19);
            radius = 150;
            maxHight = 77;
            randomEggsPerServer = 5;
            break;
        case TestSkyWars:
        case SkyWars:
            world = "Spawn";
            location = new Location(Bukkit.getWorld(world), 0, 0, 0);
            specialEggLocation = new Location(Bukkit.getWorld(world), 0, 71, 51);
            radius = 200;
            maxHight = 86;
            randomEggsPerServer = 1;
            delayInMin = 4;
            break;
        case TestBedWars:
        case BedWars:
            world = "Spawn";
            location = new Location(Bukkit.getWorld(world), 0, 0, 0);
            specialEggLocation = new Location(Bukkit.getWorld(world), 45, 85, 37);
            radius = 200;
            maxHight = 86;
            randomEggsPerServer = 1;
            delayInMin = 4;
            break;
        case Freebuild:
        case TestFreebuild:
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

    public void removeAllEggs() {
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
                removeAllEggs();
                for (int i = 0; i < randomEggsPerServer; i++) {
                    Location loc;
                    while (true) {
                        loc = location.clone();
                        int r = new Random().nextInt(radius);
                        int degree = new Random().nextInt(360);

                        loc.add(r * Math.cos(degree), 0, r * Math.sin(degree));
                        Block highestBlock = loc.getWorld().getHighestBlockAt(loc).getLocation().getBlock();
                        loc = highestBlock.getLocation();
                        if ((loc.getY() <= maxHight && loc.getY() != 1) && (highestBlock.getType() == Material.DIRT || highestBlock.getType() == Material.GRASS || highestBlock.getType() == Material.STONE))
                            break;
                    }
                    Block b = loc.add(0, 1, 0).getBlock();
                    HeadCreator.setSkullUrl(eggName, b);
                    blocks.add(b);
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20 * 60 * delayInMin);
    }

    @EventHandler
    public void onEggClick(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.SKULL) {

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
                                wenn alle Eier:
                                    1500 BC
                                    exklusives SkyWars-Kit (Eierwerfer-Kit)

                            NORMALE EIER:
                              "Du hast tatsächlich eins meiner Ostereier gefunden! Ich hoffe, dir gefallen die Preise!"
                                100 BC
                                1 Beacon (Freebuild)
                                Farbklecks-Hut (Quests)
                            3:
                                "3 Eier schon! Ein starker Start, Gratulation!"
                                150 BC
                                1 Verz 1:
                             auberte Elytra (Freebuild)
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
                        p.getBiomiaPlayer().getPlayer().sendMessage("§cFinde mindestens noch §b1 §cweiteres Ei!");
                    } else {
                        p.getBiomiaPlayer().getPlayer().sendMessage("§cFinde mindestens §b" + missingEggs + " §cweitere Eier!");
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
