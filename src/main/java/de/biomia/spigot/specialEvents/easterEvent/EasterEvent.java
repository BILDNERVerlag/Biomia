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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;

public class EasterEvent implements Listener {

    private static final int specialEggsAmount = 6;
    private static ArrayList<Material> allowedMaterials = new ArrayList<>(Arrays.asList(
            Material.DIRT, Material.GRASS,
            Material.STONE, Material.STONE_SLAB2,
            Material.WOOD, Material.LOG, Material.LOG_2,
            Material.QUARTZ_BLOCK, Material.MOSSY_COBBLESTONE, Material.HARD_CLAY));
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
                radius = 160;
                maxHight = 112;
                randomEggsPerServer = 5;
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
                randomEggsPerServer = 2;
                break;
            case BauServer:
                world = "BauWelt";
                location = new Location(Bukkit.getWorld(world), 0, 0, 0);
                specialEggLocation = new Location(Bukkit.getWorld(world), 11, 67, 19);
                radius = 80;
                maxHight = 77;
                randomEggsPerServer = 2;
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
                randomEggsPerServer = 3;
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
            Random r = new Random();
            double phi, theta, randomRadius;

            public void run() {
                removeAllEggs();
                for (int i = 0; i < randomEggsPerServer; i++) {
                    Location loc;
                    while (true) {
                        loc = location.clone();
                        int x = r.nextInt(radius * 2) - radius;
                        int z = r.nextInt(radius * 2) - radius;

                        if (loc.add(x, 0, z).distance(loc) > radius) continue;

                        Block highestBlock = loc.getWorld().getHighestBlockAt(loc).getLocation().getBlock();
                        loc = highestBlock.getLocation();
                        if ((loc.getY() <= maxHight && loc.getY() != 1) && allowedMaterials.contains(highestBlock.getType()))
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
        if ((e.getAction() == Action.LEFT_CLICK_BLOCK || e.getHand().equals(EquipmentSlot.HAND)) && e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.SKULL) {
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
        ArrayList<Integer> preisliste = new ArrayList<>(Arrays.asList(1, 3, 5, 15, 25, 50, 100));
        int eggsFound = Stats.getStat(Stats.BiomiaStat.EasterEggsFound, p.getBiomiaPlayerID());
        int eggsRewardsErhalten = Stats.getStat(Stats.BiomiaStat.EasterRewardsErhalten, p.getBiomiaPlayerID());
        int eggsWithoutRewards = eggsFound - eggsRewardsErhalten;
        ItemStack is;
        ArrayList<String> quest_rewards = new ArrayList<>();
        ArrayList<String> freebuild_rewards = new ArrayList<>();
        String osterhasenTalk = null;
        int coinsToAdd = 0;
        if (eggsWithoutRewards > 0) {
            Stats.incrementStatBy(Stats.BiomiaStat.EasterRewardsErhalten, p.getBiomiaPlayerID(), eggsWithoutRewards);
        }

        for (int i = eggsFound; i > eggsRewardsErhalten; i--) {
            switch (i) {
                case 1:
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.IRON_BLOCK), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.GOLDEN_APPLE), BiomiaServerType.Quest);
                    coinsToAdd += 250;
                    osterhasenTalk = ("§cOsterhase§7: §bDu hast tatsächlich ein Ei gefunden! Als Belohnung bekommst du ein paar Münzen und je eine Kleinigkeit auf dem Quest- und Freebuildserver.");
                    quest_rewards.add("1x Goldener Apfel");
                    freebuild_rewards.add("1x Goldener Apfel");
                    break;
                case 3:
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.ELYTRA), BiomiaServerType.Freebuild);
                    is = ItemCreator.itemCreate(Material.ENDER_PEARL);
                    is.setAmount(16);
                    RewardItems.addItem(p, is, BiomiaServerType.Quest);
                    coinsToAdd += 250;
                    quest_rewards.add("16x Enderperle");
                    freebuild_rewards.add("1x Elytra");
                    break;
                case 5:
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.JUKEBOX), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.RECORD_10), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.RECORD_7), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.RECORD_3), BiomiaServerType.Freebuild);
                    RewardItems.addItem(p, ItemCreator.itemCreate(Material.TOTEM), BiomiaServerType.Quest);
                    coinsToAdd += 300;
                    osterhasenTalk = ("§cOsterhase§7: §bNoch mehr Eier! Viel Spaß mit den Belohnungen!");
                    freebuild_rewards.addAll(Arrays.asList("1x Jukebox", "3x Schallplatte"));
                    quest_rewards.add("1x Totem der Unsterblichkeit");
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
                    coinsToAdd += 400;
                    osterhasenTalk = ("§cOsterhase§7: §bNoch mehr Eier! Viel Spaß mit den Belohnungen!");
                    quest_rewards.add("1x Farbklecks-Hut");
                    freebuild_rewards.add("16x Namensschild");
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
                    coinsToAdd += 500;
                    osterhasenTalk = ("§cOsterhase§7: §bSchon 25 Eier gefunden! Viel Spaß mit den Belohnungen!");
                    quest_rewards.add("1x Farbklecks-Schuhe");
                    freebuild_rewards.add("64x Bücherregal");
                    break;
                case 50:
                    is = ItemCreator.itemCreate(Material.GOLD_BLOCK);
                    is.setAmount(9);
                    RewardItems.addItem(p, is, BiomiaServerType.Freebuild);

                    is = ItemCreator.itemCreate(Material.LEATHER_LEGGINGS, "Farbklecks-Hose");
                    LeatherArmorMeta elfenPantsMeta = (LeatherArmorMeta) is.getItemMeta();
                    elfenPantsMeta.setColor(Color.fromRGB(0, 179, 44));
                    is.setItemMeta(elfenPantsMeta);
                    is.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 2);
                    is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
                    RewardItems.addItem(p, is, BiomiaServerType.Quest);
                    coinsToAdd += 750;
                    osterhasenTalk = ("§cOsterhase§7: §bSchon 50 Eier, nicht schlecht! Die letzte Belohnung bekommst du aber nur, wenn du 100 findest!");
                    quest_rewards.add("1x Farbklecks-Hose");
                    freebuild_rewards.add("9x Goldblock");
                    break;
                case 100:
                    is = ItemCreator.itemCreate(Material.BEACON);
                    RewardItems.addItem(p, is, BiomiaServerType.Freebuild);

                    is = ItemCreator.itemCreate(Material.LEATHER_CHESTPLATE, "Farbklecks-Hemd");
                    LeatherArmorMeta elfenChestMeta = (LeatherArmorMeta) is.getItemMeta();
                    elfenChestMeta.setColor(Color.fromRGB(0, 179, 44));
                    is.setItemMeta(elfenChestMeta);
                    is.addUnsafeEnchantment(Enchantment.THORNS, 2);
                    is.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
                    RewardItems.addItem(p, is, BiomiaServerType.Quest);
                    coinsToAdd += 1000;
                    osterhasenTalk = ("§cOsterhase§7: §bUff, jetzt hast du aber alle großen Belohnungen abgestaubt!");
                    quest_rewards.add("1x Farbklecks-Hemd");
                    freebuild_rewards.add("1x Beacon");
                    break;
                default:
                    if (i > 100 && i % 20 == 0) {
                        is = ItemCreator.itemCreate(Material.DIAMOND);
                        is.setAmount(5);
                        RewardItems.addItem(p, is, BiomiaServerType.Freebuild);

                        RewardItems.addItem(p, ItemCreator.itemCreate(Material.GOLDEN_APPLE), BiomiaServerType.Quest);
                        coinsToAdd += 250;
                        osterhasenTalk = ("§cOsterhase§7: b7Noch mehr Eier! Viel Spaß mit den Belohnungen!");
                        quest_rewards.add("1x Goldener Apfel");
                        freebuild_rewards.add("5x Diamanten");
                        break;
                    }
            }
        }

        if (osterhasenTalk != null) {
            p.sendMessage(osterhasenTalk);
        }
        if (coinsToAdd > 0) {
            p.addCoins(coinsToAdd, false);
        }
        if (!quest_rewards.isEmpty()) {
            p.sendMessage("§cBelohnungen für den Questserver:");
            for (String s : quest_rewards) {
                p.sendMessage(" §7" + s.replace("x ", " §7x§b "));
            }
        }
        if (!freebuild_rewards.isEmpty()) {
            p.sendMessage("§cBelohnungen für den Freebuildserver:");
            for (String s : freebuild_rewards) {
                p.sendMessage(" §7" + s.replace("x ", " §7x§b "));
            }
        }
        String preisOutput = "";
        for (Integer i : preisliste) {
            if (!preisOutput.equals("")) {
                preisOutput += "§7, ";
            }
            if (i <= eggsFound) {
                preisOutput += "§7[§m" + i + "§r§7]";
            } else {
                preisOutput += "§7[§b" + i + "§7]§r";
            }
        }
        if (!preisOutput.equals("")) {
            p.sendMessage("§cBenötigte Ostereier §7(§cDu hast §b" + eggsFound + "§c§7):");
            p.sendMessage(preisOutput);
        }
    }
}

//TODO: Farben ändern