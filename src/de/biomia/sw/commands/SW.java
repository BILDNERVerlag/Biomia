package de.biomia.sw.commands;

import de.biomia.sw.gamestates.InLobby;
import de.biomia.sw.messages.ItemNames;
import de.biomia.sw.messages.Messages;
import de.biomia.sw.var.Config;
import de.biomia.sw.var.Variables;
import de.biomiaAPI.Biomia;
import de.biomiaAPI.Teams.Team;
import de.biomiaAPI.itemcreator.ItemCreator;
import de.biomiaAPI.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SW implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.skywars")) {

                if (args.length >= 1) {

                    if (args[0].equalsIgnoreCase("instantstart")) {
                        Variables.countDown.getBukkitTask().cancel();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.setLevel(0);
                        }
                        InLobby.end();
                    }

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
                        Config.addLocation(p.getLocation());
                        sender.sendMessage("Spawnpoint wurde hinzugefügt!");
                        return true;
                    } else if (args[0].equalsIgnoreCase("removelocs")) {
                        Config.removeAllLocations();
                        sender.sendMessage("Alle Spawnlocations entfernt!");
                    } else if (args[0].equalsIgnoreCase("start")) {
                        Config.removeAllLocations();
                        Bukkit.broadcastMessage("§cStart Erzwunngen!");
                        Variables.countDown.setCountdown(0);
                    } else if (args[0].equalsIgnoreCase("chestaddmode")) {
                        Variables.chestAddMode = !Variables.chestAddMode;
                        if (Variables.chestAddMode) {
                            sender.sendMessage(Messages.chestAddModeON);
                        } else {
                            sender.sendMessage(Messages.chestAddModeOFF);
                        }
                    } else if (args[0].equalsIgnoreCase("deleteallchests")) {
                        Config.removeAllChests();
                        sender.sendMessage("§cAlle Kisten entfernt.");
                    } else if (args[0].equalsIgnoreCase("deleteallsigns")) {
                        Config.removeAllSigns();
                        sender.sendMessage("§cAlle Signs entfernt.");
                    } else if (args[0].equalsIgnoreCase("getTeamjoinersetter")) {
                        for (Team t : Biomia.TeamManager().getTeams()) {
                            p.getInventory().addItem(ItemCreator.itemCreate(Material.WOOL, ItemNames.teamJoinerSetter,
                                    t.getColordata()));
                        }
                    }
                } else {
                    sender.sendMessage("§c/sw setup (Setup für SkyWars-Map)");
                    sender.sendMessage("§c/sw addloc (Fügt einen Spawnpunkt hinzu)");
                    sender.sendMessage("§c/sw chestaddmode (Versetzt dich in den §4KISTENHINZUFÜGEMODUS§c)");
                    sender.sendMessage("§c/sw removelocs (Entfernt alle Spawnpunkte)");
                    sender.sendMessage("§c/sw deleteallchests (Entfernt alle Kisten aus der Liste)");
                    sender.sendMessage("§c/sw deleteallsigns (Entfernt alle Schilder aus der Liste)");
                    sender.sendMessage("§c/sw getTeamjoinersetter (Gibt den Teamjoinersetter zurück)");
                }
            }
        }
        return false;
    }
}
