package de.biomia.bw.commands;

import de.biomia.bw.messages.ItemNames;
import de.biomia.bw.var.Config;
import de.biomia.api.Biomia;
import de.biomia.api.Teams.Team;
import de.biomia.api.Teams.Teams;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.api.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BW implements CommandExecutor {

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
                            Bukkit.broadcastMessage("§c/sw setup <SpielerProTeam> <Teams> <MapName>");
                            return true;
                        }
                        String name = args[3];
                        Config.config.set("Name", name);
                        Config.config.set("TeamSize", spielerProTeam);
                        Config.config.set("NumberOfTeams", teams);
                        Main.getPlugin().saveConfig();
                    } else if (args[0].equalsIgnoreCase("setup")) {
                        sender.sendMessage("§c/sw setup <SpielerProTeam> <Teams> <MapName>");
                    } else if (args[0].equalsIgnoreCase("addloc")) {
                        if (args.length >= 2) {
                            Config.addLocation(p.getLocation(), Teams.valueOf(args[1]));
                            sender.sendMessage("Spawnpoint wurde hinzugefügt!");
                        } else {
                            sender.sendMessage("Bitte gib ein Team an!");
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("removelocs")) {
                        Config.removeAllLocations();
                        sender.sendMessage("Alle Spawnlocations entfernt!");
                    } else if (args[0].equalsIgnoreCase("getTeamjoinersetter")) {
                        for (Team t : Biomia.TeamManager().getTeams()) {
                            p.getInventory().addItem(ItemCreator.itemCreate(Material.WOOL, ItemNames.teamJoinerSetter,
                                    t.getColordata()));
                        }
                    } else if (args[0].equalsIgnoreCase("getbedsetter")) {
                        for (Team t : Biomia.TeamManager().getTeams()) {
                            p.getInventory().addItem(
                                    ItemCreator.itemCreate(Material.WOOL, ItemNames.bedSetter, t.getColordata()));
                        }
                    } else if (args[0].equalsIgnoreCase("getSpawner")) {
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.HARD_CLAY, ItemNames.bronzeSetter));
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.IRON_BLOCK, ItemNames.ironSetter));
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.GOLD_BLOCK, ItemNames.goldSetter));
                    } else if (args[0].equalsIgnoreCase("villager")) {
                        p.getInventory()
                                .addItem(ItemCreator.itemCreate(Material.MONSTER_EGG, ItemNames.villagerSpawner));
                    }
                } else {
                    sender.sendMessage("§c/bw setup (Setup für BedWars-Map)");
                    sender.sendMessage("§c/bw addloc (Fügt einen Spawnpunkt hinzu)");
                    sender.sendMessage("§c/bw removelocs (Entfernt alle Spawnpunkte)");
                    sender.sendMessage("§c/bw deleteallsigns (Entfernt alle Schilder)");
                    sender.sendMessage("§c/bw getTeamjoinersetter (Gibt den Teamjoinersetter zurück)");
                    sender.sendMessage("§c/bw getbedsetter (Gibt den Bedsetter zurück)");
                    sender.sendMessage("§c/bw getSpawner (Gibt alle Verfügbaren Spawner zurück)");
                    sender.sendMessage("§c/bw villager (Gibt einen Villager Spawner zurück)");
                }
            }
        }
        return false;
    }
}
