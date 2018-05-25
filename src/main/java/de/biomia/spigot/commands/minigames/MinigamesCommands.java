package de.biomia.spigot.commands.minigames;

import com.boydti.fawe.object.FawePlayer;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;
import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.configs.BedWarsConfig;
import de.biomia.spigot.configs.Config;
import de.biomia.spigot.configs.MinigamesConfig;
import de.biomia.spigot.configs.ParrotConfig;
import de.biomia.spigot.minigames.GameStateManager;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.TeamColor;
import de.biomia.universal.Messages;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinigamesCommands extends BiomiaCommand {

    public MinigamesCommands(String command, String... args) {
        super(command, args);
    }

    @Override
    protected void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        if (!Biomia.getBiomiaPlayer(p).isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        switch (label) {
            case "start":
                GameTeam team = Biomia.getBiomiaPlayer(p).getTeam();
                if (team == null) {
                    p.sendMessage("Bitte gehe in ein Team um das Spiel zu starten");
                    return;
                }
                if (team.getMode().getStateManager().getActualGameState() != GameStateManager.GameState.LOBBY) {
                    p.sendMessage("Das Spiel hat bereits gestartet!");
                    return;
                }
                team.getMode().getStateManager().getLobbyState().stop();
                p.sendMessage("Spiel startet!");
                break;
            case "addship":
                Region region = FawePlayer.wrap(p.getName()).getSelection();
                if (region == null) {
                    p.sendMessage(String.format("%sBitte wähle zuerst einen Bereich aus! %s(//wand)", Messages.COLOR_MAIN, Messages.COLOR_SUB));
                    return;
                }
                Vector pos1 = region.getMinimumPoint();
                Vector pos2 = region.getMaximumPoint();
                if (args.length < 1) {
                    p.sendMessage(String.format("%sBitte nutze: %s/addship teamName", Messages.COLOR_MAIN, Messages.COLOR_SUB));
                    return;
                }
                TeamColor color = TeamColor.valueOf(args[0].toUpperCase());
                ParrotConfig.addShip(new Location(null, pos1.getX(), pos1.getY(), pos1.getZ()), new Location(null, pos2.getX(), pos2.getY(), pos2.getZ()), color);
                break;
            case "addloc":
            case "al":
                if (args.length >= 2) {
                    BedWarsConfig.addSpawnLocation(p.getLocation(), TeamColor.valueOf(args[1]), GameType.valueOf(args[0]));
                    sender.sendMessage("Spawnpoint wurde hinzugefügt!");
                } else
                    sender.sendMessage(String.format("/%s gametype team", label));
                break;
            case "setup":
                if (args.length >= 3) {
                    int spielerProTeam, teams;
                    try {
                        spielerProTeam = Integer.parseInt(args[0]);
                        teams = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("\u00A77/\u00A7bsetup \u00A77<\u00A7bSpielerProTeam\u00A77> <\u00A7bTeams\u00A77> <\u00A7bMapName\u00A77>");
                        return;
                    }
                    String name = args[2];
                    Config.getConfig().set("Name", name);
                    MinigamesConfig.mapName = name;
                    Config.getConfig().set("TeamSize", spielerProTeam);
                    Config.getConfig().set("NumberOfTeams", teams);
                    Config.saveConfig();
                } else
                    sender.sendMessage("\u00A77/\u00A7bsetup \u00A77<\u00A7bSpielerProTeam\u00A77> <\u00A7bTeams\u00A77> <\u00A7bMapName\u00A77>");
                break;
        }
    }
}
