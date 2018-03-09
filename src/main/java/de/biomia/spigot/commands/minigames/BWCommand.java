package de.biomia.spigot.commands.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.messages.BedWarsItemNames;
import de.biomia.spigot.minigames.general.teams.Team;
import de.biomia.spigot.minigames.general.teams.Teams;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BWCommand extends BiomiaCommand {
    //TODO renew (merge)
    public BWCommand() {
        super("bedwars", "bedwars");
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
                                sender.sendMessage("\u00A77/\u00A7bsw setup \u00A77<\u00A7bSpielerProTeam\u00A77> <\u00A7bTeams\u00A77> <\u00A7bMapName\u00A77>");
                                return true;
                            }
                            String name = args[3];
                            Config.getConfig().set("Name", name);
                            Config.getConfig().set("TeamSize", spielerProTeam);
                            Config.getConfig().set("NumberOfTeams", teams);
                            Config.saveConfig();
                        } else
                            sender.sendMessage("\u00A77/\u00A7bsw setup \u00A77<\u00A7bSpielerProTeam\u00A77> <\u00A7bTeams\u00A77> <\u00A7bMapName\u00A77>");
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
                    sender.sendMessage("\u00A7c/bedwars setup (Setup f\u00fcr BedWars-Map)");
                    sender.sendMessage("\u00A7c/bedwars addloc (F\u00fcgt einen Spawnpunkt hinzu)");
                    sender.sendMessage("\u00A7c/bedwars removelocs (Entfernt alle Spawnpunkte)");
                    sender.sendMessage("\u00A7c/bedwars deleteallsigns (Entfernt alle Schilder)");
                    sender.sendMessage("\u00A7c/bedwars getTeamjoinersetter (Gibt den Teamjoinersetter zur\u00fcck)");
                    sender.sendMessage("\u00A7c/bedwars getbedsetter (Gibt den Bedsetter zur\u00fcck)");
                    sender.sendMessage("\u00A7c/bedwars getSpawner (Gibt alle Verf\u00fcgbaren Spawner zur\u00fcck)");
                    sender.sendMessage("\u00A7c/bedwars villager (Gibt einen Villager Spawner zur\u00fcck)");
                }
            }
        }
        return false;
    }
}
