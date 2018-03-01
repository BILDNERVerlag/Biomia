package de.biomia.minigames.bedwars.commands;

import de.biomia.general.configs.Config;
import de.biomia.general.messages.BedWarsItemNames;
import de.biomia.general.configs.BedWarsConfig;
import de.biomia.api.Biomia;
import de.biomia.api.Teams.Team;
import de.biomia.api.Teams.Teams;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BWCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.bedwars")) {
                if (args.length >= 1) {
                    if (args.length == 4 && args[0].equalsIgnoreCase("setup")) {
                        int spielerProTeam, teams;
                        try {
                            spielerProTeam = Integer.parseInt(args[1]);
                            teams = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            Bukkit.broadcastMessage("\u00A7c/sw setup <SpielerProTeam> <Teams> <MapName>");
                            return true;
                        }
                        String name = args[3];
                        Config.getConfig().set("Name", name);
                        Config.getConfig().set("TeamSize", spielerProTeam);
                        Config.getConfig().set("NumberOfTeams", teams);
                        Main.getPlugin().saveConfig();
                    } else if (args[0].equalsIgnoreCase("setup")) {
                        sender.sendMessage("\u00A7c/sw setup <SpielerProTeam> <Teams> <MapName>");
                    } else if (args[0].equalsIgnoreCase("addloc")) {
                        if (args.length >= 2) {
                            BedWarsConfig.addLocation(p.getLocation(), Teams.valueOf(args[1]));
                            sender.sendMessage("Spawnpoint wurde hinzugef\u00fcgt!");
                        } else {
                            sender.sendMessage("Bitte gib ein Team an!");
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("removelocs")) {
                        BedWarsConfig.removeAllLocations();
                        sender.sendMessage("Alle Spawnlocations entfernt!");
                    } else if (args[0].equalsIgnoreCase("getTeamjoinersetter")) {
                        for (Team t : Biomia.getTeamManager().getTeams()) {
                            p.getInventory().addItem(ItemCreator.itemCreate(Material.WOOL, BedWarsItemNames.teamJoinerSetter,
                                    t.getColordata()));
                        }
                    } else if (args[0].equalsIgnoreCase("getbedsetter")) {
                        for (Team t : Biomia.getTeamManager().getTeams()) {
                            p.getInventory().addItem(
                                    ItemCreator.itemCreate(Material.WOOL, BedWarsItemNames.bedSetter, t.getColordata()));
                        }
                    } else if (args[0].equalsIgnoreCase("getSpawner")) {
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.HARD_CLAY, BedWarsItemNames.bronzeSetter));
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.IRON_BLOCK, BedWarsItemNames.ironSetter));
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.GOLD_BLOCK, BedWarsItemNames.goldSetter));
                    } else if (args[0].equalsIgnoreCase("villager")) {
                        p.getInventory()
                                .addItem(ItemCreator.itemCreate(Material.MONSTER_EGG, BedWarsItemNames.villagerSpawner));
                    }
                } else {
                    sender.sendMessage("\u00A7c/bw setup (Setup f\u00fcr BedWars-Map)");
                    sender.sendMessage("\u00A7c/bw addloc (F\u00fcgt einen Spawnpunkt hinzu)");
                    sender.sendMessage("\u00A7c/bw removelocs (Entfernt alle Spawnpunkte)");
                    sender.sendMessage("\u00A7c/bw deleteallsigns (Entfernt alle Schilder)");
                    sender.sendMessage("\u00A7c/bw getTeamjoinersetter (Gibt den Teamjoinersetter zur\u00fcck)");
                    sender.sendMessage("\u00A7c/bw getbedsetter (Gibt den Bedsetter zur\u00fcck)");
                    sender.sendMessage("\u00A7c/bw getSpawner (Gibt alle Verf\u00fcgbaren Spawner zur\u00fcck)");
                    sender.sendMessage("\u00A7c/bw villager (Gibt einen Villager Spawner zur\u00fcck)");
                }
            }
        }
        return false;
    }
}
