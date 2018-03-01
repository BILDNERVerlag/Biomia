package de.biomia.bw.ingame;

import de.biomia.bw.gamestates.GameState;
import de.biomia.bw.gamestates.InGame;
import de.biomia.bw.BedWars;
import de.biomia.bw.var.Scoreboards;
import de.biomia.bw.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.Teams.Team;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Dead {

    public static void setDead(Player target) {

        Variables.livingPlayer.remove(target);
        BiomiaPlayer bp = Biomia.getBiomiaPlayer(target);

        Biomia.getTeamManager().getTeam(target).setPlayerDead(target);

        // Hide
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (Biomia.getTeamManager().isPlayerAlive(all))
                all.hidePlayer(target);
            else
                all.showPlayer(all);
        }

        // Disable Damage / Build
        bp.setGetDamage(false);
        bp.setDamageEntitys(false);
        bp.setBuild(false);

        target.setGameMode(GameMode.SPECTATOR);
        target.setSilent(true);
        target.getInventory().clear();

        // Fly settings
        target.setAllowFlight(true);
        target.setFlying(true);
        target.setFlySpeed(0.5F);


        // Scoreboard
        Scoreboards.spectatorSB.getTeam("spectator").addEntry(target.getName());
        Scoreboards.setSpectatorSB(target);

        // Check if only one or less Team(s) left
        if (BedWars.gameState != GameState.END) {

            ArrayList<Team> livingTeams = new ArrayList<>();
            if (Variables.livingPlayer.size() <= 1) {
                InGame.end();
                return;
            }
            for (Player player : Variables.livingPlayer) {
                Team t = Biomia.getTeamManager().getTeam(player);
                if (!livingTeams.contains(t)) {
                    livingTeams.add(t);
                }
            }
            if (livingTeams.size() <= 1) {
                InGame.end();
            }
        }
    }

    public static void respawn(Player p) {
        ((CraftPlayer) p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
    }

}
