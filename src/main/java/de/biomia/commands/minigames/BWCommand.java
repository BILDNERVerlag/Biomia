package de.biomia.commands.minigames;

import de.biomia.Biomia;
import de.biomia.commands.BiomiaCommand;
import de.biomia.data.configs.BedWarsConfig;
import de.biomia.data.configs.Config;
import de.biomia.messages.BedWarsItemNames;
import de.biomia.server.minigames.general.teams.Team;
import de.biomia.server.minigames.general.teams.Teams;
import de.biomia.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BWCommand extends BiomiaCommand {
    //TODO renew
    public BWCommand() {
        super("bedwars", "bw");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("biomia.bedwars")) {
                if (args.length >= 1) {

                    args[0] = args[0].toLowerCase();

                    switch (args[0]) {
                    case "setup":
                        if (args.length >= 4) {
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
                            Config.saveConfig();
                        } else
                            sender.sendMessage("\u00A7c/sw setup <SpielerProTeam> <Teams> <MapName>");
                        break;
                    case "addloc":
                        if (args.length >= 2) {
                            BedWarsConfig.addLocation(p.getLocation(), Teams.valueOf(args[1]));
                            sender.sendMessage("Spawnpoint wurde hinzugef\u00fcgt!");
                        } else {
                            sender.sendMessage("Bitte gib ein Team an!");
                        }
                        return true;
                    case "removelocs":
                        BedWarsConfig.removeAllLocations();
                        sender.sendMessage("Alle Spawnlocations entfernt!");
                        break;
                    case "getTeamjoinersetter":
                        for (Team t : Biomia.getTeamManager().getTeams()) {
                            p.getInventory().addItem(ItemCreator.itemCreate(Material.WOOL, BedWarsItemNames.teamJoinerSetter,
                                    t.getColordata()));
                        }
                        break;
                    case "getbedsetter":
                        for (Team t : Biomia.getTeamManager().getTeams()) {
                            p.getInventory().addItem(
                                    ItemCreator.itemCreate(Material.WOOL, BedWarsItemNames.bedSetter, t.getColordata()));
                        }
                        break;
                    case "getspawner":
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.HARD_CLAY, BedWarsItemNames.bronzeSetter));
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.IRON_BLOCK, BedWarsItemNames.ironSetter));
                        p.getInventory().addItem(ItemCreator.itemCreate(Material.GOLD_BLOCK, BedWarsItemNames.goldSetter));
                        break;
                    case "villager":
                        p.getInventory()
                                .addItem(ItemCreator.itemCreate(Material.MONSTER_EGG, BedWarsItemNames.villagerSpawner));
                        break;
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
