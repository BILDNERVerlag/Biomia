package de.biomia.spigot.commands.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.configs.SkyWarsConfig;
import de.biomia.spigot.messages.SkyWarsItemNames;
import de.biomia.spigot.messages.SkyWarsMessages;
import de.biomia.spigot.server.minigames.general.teams.Team;
import de.biomia.spigot.server.minigames.skywars.gamestates.InLobby;
import de.biomia.spigot.server.minigames.skywars.var.Variables;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.biomia.spigot.configs.Config.saveConfig;

public class SWCommand extends BiomiaCommand {
    //TODO renew (merge)
    public SWCommand() {
        super("skywars", "sw");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

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

                        saveConfig();
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
                            sender.sendMessage(SkyWarsMessages.chestAddModeON);
                        } else {
                            sender.sendMessage(SkyWarsMessages.chestAddModeOFF);
                        }
                    } else if (args[0].equalsIgnoreCase("deleteallchests")) {
                        SkyWarsConfig.removeAllChests();
                        sender.sendMessage("\u00A7cAlle Kisten entfernt.");
                    } else if (args[0].equalsIgnoreCase("deleteallsigns")) {
                        SkyWarsConfig.removeAllSigns();
                        sender.sendMessage("\u00A7cAlle Signs entfernt.");
                    } else if (args[0].equalsIgnoreCase("getTeamjoinersetter")) {
                        for (Team t : Biomia.getTeamManager().getTeams()) {
                            p.getInventory().addItem(ItemCreator.itemCreate(Material.WOOL, SkyWarsItemNames.teamJoinerSetter,
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
