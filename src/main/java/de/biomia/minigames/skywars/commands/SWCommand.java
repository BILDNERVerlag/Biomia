package de.biomia.minigames.skywars.commands;

import de.biomia.general.configs.Config;
import de.biomia.minigames.skywars.gamestates.InLobby;
import de.biomia.minigames.skywars.messages.ItemNames;
import de.biomia.minigames.skywars.messages.Messages;
import de.biomia.general.configs.SkyWarsConfig;
import de.biomia.minigames.skywars.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.api.Teams.Team;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SWCommand implements CommandExecutor {

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
                        SkyWarsConfig.addLocation(p.getLocation());
                        sender.sendMessage("Spawnpoint wurde hinzugef\u00fcgt!");
                        return true;
                    } else if (args[0].equalsIgnoreCase("removelocs")) {
                        SkyWarsConfig.removeAllLocations();
                        sender.sendMessage("Alle Spawnlocations entfernt!");
                    } else if (args[0].equalsIgnoreCase("start")) {
                        SkyWarsConfig.removeAllLocations();
                        Bukkit.broadcastMessage("\u00A7cStart Erzwunngen!");
                        Variables.countDown.setCountdown(0);
                    } else if (args[0].equalsIgnoreCase("chestaddmode")) {
                        Variables.chestAddMode = !Variables.chestAddMode;
                        if (Variables.chestAddMode) {
                            sender.sendMessage(Messages.chestAddModeON);
                        } else {
                            sender.sendMessage(Messages.chestAddModeOFF);
                        }
                    } else if (args[0].equalsIgnoreCase("deleteallchests")) {
                        SkyWarsConfig.removeAllChests();
                        sender.sendMessage("\u00A7cAlle Kisten entfernt.");
                    } else if (args[0].equalsIgnoreCase("deleteallsigns")) {
                        SkyWarsConfig.removeAllSigns();
                        sender.sendMessage("\u00A7cAlle Signs entfernt.");
                    } else if (args[0].equalsIgnoreCase("getTeamjoinersetter")) {
                        for (Team t : Biomia.getTeamManager().getTeams()) {
                            p.getInventory().addItem(ItemCreator.itemCreate(Material.WOOL, ItemNames.teamJoinerSetter,
                                    t.getColordata()));
                        }
                    }
                } else {
                    sender.sendMessage("\u00A7c/sw setup (Setup f\u00fcr SkyWars-Map)");
                    sender.sendMessage("\u00A7c/sw addloc (F\u00fcgt einen Spawnpunkt hinzu)");
                    sender.sendMessage("\u00A7c/sw chestaddmode (Versetzt dich in den \u00A74KISTENHINZUF\u00fcGEMODUS\u00A7c)");
                    sender.sendMessage("\u00A7c/sw removelocs (Entfernt alle Spawnpunkte)");
                    sender.sendMessage("\u00A7c/sw deleteallchests (Entfernt alle Kisten aus der Liste)");
                    sender.sendMessage("\u00A7c/sw deleteallsigns (Entfernt alle Schilder aus der Liste)");
                    sender.sendMessage("\u00A7c/sw getTeamjoinersetter (Gibt den Teamjoinersetter zur\u00fcck)");
                }
            }
        }
        return false;
    }
}
