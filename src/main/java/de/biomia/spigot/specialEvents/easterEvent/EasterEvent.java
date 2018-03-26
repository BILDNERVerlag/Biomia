package de.biomia.spigot.specialEvents.easterEvent;

import de.biomia.spigot.*;
import de.biomia.spigot.achievements.Stats;
import de.biomia.spigot.general.cosmetics.MysteryChest;
import de.biomia.spigot.server.quests.sonstigeQuests.Osterhase;
import de.biomia.spigot.tools.HeadCreator;
import de.biomia.spigot.tools.ItemCreator;
import de.biomia.spigot.tools.RewardItems;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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
                        ItemStack is;
                        switch (Biomia.getServerInstance().getServerType()) {
                            case TestLobby:
                            case Lobby:
                                MysteryChest.open(bp);
                                break;
                            case TestQuest:
                            case Quest:
                                is = ItemCreator.itemCreate(Material.DIAMOND);
                                is.setAmount(3);
                                RewardItems.addItem(bp, is, BiomiaServerType.Quest);
                                break;
                            case TestBedWars:
                            case BedWars:
                                is = ItemCreator.itemCreate(Material.BED);
                                break;
                            case TestSkyWars:
                            case SkyWars:
                                is = ItemCreator.itemCreate(Material.EMERALD_BLOCK);
                                break;
                            case TestFreebuild:
                            case Freebuild:
                                is = ItemCreator.itemCreate(Material.DIAMOND);
                                is.setAmount(3);
                                RewardItems.addItem(bp, is, BiomiaServerType.Freebuild);
                                break;
                            case BauServer:
                                is = ItemCreator.itemCreate(Material.CONCRETE);
                                is.setAmount(64);
                                RewardItems.addItem(bp, is, BiomiaServerType.Freebuild);
                                break;
                        }
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

    public void addEggs(int bpID, int eggs) {
        Stats.incrementStatBy(Stats.BiomiaStat.EasterEggsFound, bpID, eggs);
    }

    public static void giveReward(OfflineBiomiaPlayer p) {

        int eggsFound = Stats.getStat(Stats.BiomiaStat.EasterEggsFound, p.getBiomiaPlayerID());
        int eggsRewardsErhalten = Stats.getStat(Stats.BiomiaStat.EasterRewardsErhalten, p.getBiomiaPlayerID());
        int coinsToAdd = 0;

        int eggsWithoutRewards = eggsFound - eggsRewardsErhalten;
        ItemStack is;
        while (eggsWithoutRewards >= 0) {
            switch (eggsFound - eggsWithoutRewards) {
                case 1:
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.BEACON), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.GOLDEN_APPLE), BiomiaServerType.Quest);
                    p.addCoins(250, false);
                    break;
                case 3:
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.ELYTRA), BiomiaServerType.Freebuild);
                    is = ItemCreator.itemCreate(Material.ENDER_PEARL);
                    is.setAmount(16);
                    RewardItems.addItem(p, is, BiomiaServerType.Quest);
                    p.addCoins(250, false);
                    break;
                case 5:
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.JUKEBOX), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.RECORD_10), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.RECORD_7), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.RECORD_3), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.TOTEM), BiomiaServerType.Quest);
                    p.addCoins(300, false);
                    break;
                case 15:
                    is = ItemCreator.itemCreate(Material.NAME_TAG);
                    is.setAmount(16);
                    RewardItems.addItem(p, is, BiomiaServerType.Freebuild);
                    is = ItemCreator.itemCreate(Material.LEATHER_HELMET, "Farbklecks-Hut");
                    LeatherArmorMeta elfenHatMeta = (LeatherArmorMeta) is.getItemMeta();
                    elfenHatMeta.setColor(Color.fromRGB(179, 0, 12));
                    is.setItemMeta(elfenHatMeta);
                    is.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                    is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
                    RewardItems.addItem(p, is, BiomiaServerType.Quest);
                    p.addCoins(400, false);
                    break;
                case 25:
                    is = ItemCreator.itemCreate(Material.BOOKSHELF);
                    is.setAmount(64);
                    RewardItems.addItem(p, is, BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.ENCHANTMENT_TABLE), BiomiaServerType.Freebuild);
                    is = ItemCreator.itemCreate(Material.LEATHER_BOOTS, "Farbklecks-Schuhe");
                    LeatherArmorMeta elfenBootsMeta = (LeatherArmorMeta) is.getItemMeta();
                    elfenBootsMeta.setColor(Color.fromRGB(0, 179, 44));
                    is.setItemMeta(elfenBootsMeta);
                    is.addUnsafeEnchantment(Enchantment.FROST_WALKER, 2);
                    is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
                    RewardItems.addItem(p, is, BiomiaServerType.Quest);
                    p.addCoins(500, false);
                    break;
                case 50:
                    is = ItemCreator.itemCreate(Material.GOLD_BLOCK);
                    is.setAmount(32);
                    RewardItems.addItem(p, is, BiomiaServerType.Freebuild);
                    is = ItemCreator.itemCreate(Material.LEATHER_LEGGINGS, "Farbklecks-Hose");
                    LeatherArmorMeta elfenPantsMeta = (LeatherArmorMeta) is.getItemMeta();
                    elfenPantsMeta.setColor(Color.fromRGB(0, 179, 44));
                    is.setItemMeta(elfenPantsMeta);
                    is.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 2);
                    is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
                    RewardItems.addItem(p, is, BiomiaServerType.Quest);
                    p.addCoins(750, false);
                    break;
                case 100:
                    is = ItemCreator.itemCreate(Material.DIAMOND);
                    is.setAmount(32);
                    RewardItems.addItem(p, is, BiomiaServerType.Freebuild);
                    is = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE, "Farbklecks-Hemd");
                    LeatherArmorMeta elfenChestMeta = (LeatherArmorMeta) is.getItemMeta();
                    elfenChestMeta.setColor(Color.fromRGB(0, 179, 44));
                    is.setItemMeta(elfenChestMeta);
                    is.addUnsafeEnchantment(Enchantment.THORNS, 2);
                    is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
                    RewardItems.addItem(p, is, BiomiaServerType.Quest);
                    p.addCoins(1000, false);
                    break;
                default:
                    if (eggsFound > 100 && eggsWithoutRewards == 20) {
                        is = ItemCreator.itemCreate(Material.DIAMOND);
                        is.setAmount(5);
                        RewardItems.addItem(p, is, BiomiaServerType.Freebuild);
                        RewardItems.addItem(p, ItemCreator.itemCreate(Material.GOLDEN_APPLE), BiomiaServerType.Quest);
                        p.addCoins(250, false);
                    }
            }
            eggsWithoutRewards--;
        }
        Stats.incrementStatBy(Stats.BiomiaStat.EasterRewardsErhalten, p.getBiomiaPlayerID(), eggsWithoutRewards);


//        boolean b;
//
//        do {
//            int eggsWithoutRewards = eggsFound - eggsRewardsErhalten - eggsAbgegeben;
//
//            if(eggsWithoutRewards % 10 == 0){
//                if (eggsWithoutRewards < 10) {
//                    int missingEggs = 10 - eggsWithoutRewards;
//
//                    if (p.isOnline())
//                        if (missingEggs == 1) {
//                            p.getBiomiaPlayer().getPlayer().sendMessage("§cOsterhase: §7Finde mindestens noch §b1 §7weiteres Ei für die nächste Belohnung!!");
//                        } else {
//                            p.getBiomiaPlayer().getPlayer().sendMessage("§cOsterhase: §7Finde mindestens §b" + missingEggs + " §7weitere Eier für die nächste Belohnung!");
//                        }
//                } else {
//                }
//            }
//
//        } while (eggsFound);
//        Stats.incrementStatBy(Stats.BiomiaStat.EasterRewardsErhalten, p.getBiomiaPlayerID(), eggsAbgegeben);
//        if (eggsAbgegeben >= 10) {
//            p.getBiomiaPlayer().getPlayer().sendMessage("§cOsterhase: §7Vielen Dank für die " + eggsAbgegeben + "§7 Eier!");
//        }
//        p.addCoins(coinsToAdd, false);
    }
}
