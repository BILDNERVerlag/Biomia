package de.biomia.minigames.skywars.lobby;

import de.biomia.minigames.GameState;
import de.biomia.minigames.skywars.SkyWars;
import de.biomia.general.messages.SkyWarsItemNames;
import de.biomia.general.messages.SkyWarsMessages;
import de.biomia.minigames.skywars.var.Scoreboards;
import de.biomia.minigames.skywars.var.Variables;
import de.biomia.api.Biomia;
import de.biomia.api.BiomiaPlayer;
import de.biomia.api.Teams.Team;
import de.biomia.api.itemcreator.ItemCreator;
import de.biomia.Main;
import de.biomia.api.messages.ActionBar;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class JoinTeam {

    public static void setAllToTeams() {

        ArrayList<Player> l = new ArrayList<>(Bukkit.getOnlinePlayers());

        int i = l.size() - 1;

        getallteams:
        for (Team team : Biomia.getTeamManager().getTeams()) {

            while (!team.full()) {
                if (i >= 0) {
                    Player p = l.get(i);
                    if (joinIfInNoTeam(p, team)) {
                        l.remove(p);
                    }
                    i--;
                } else {
                    break getallteams;
                }
            }
        }
    }

    public static void join(Player p, Team team) {

        if (SkyWars.gameState != GameState.LOBBY) {
            p.sendMessage("\u00A7cDas Spiel hat bereits begonnen!");
            return;
        }

        if (team.full()) {
            p.sendMessage(team.getColorcode() + SkyWarsMessages.teamFull);
            return;
        }

        if (Biomia.getTeamManager().isPlayerInAnyTeam(p)) {

            if (team.equals(Biomia.getTeamManager().getTeam(p))) {
                p.sendMessage(SkyWarsMessages.alreadyInTeam);
                return;
            }

            Biomia.getTeamManager().getTeam(p).removePlayer(p);
        }

        for (Player pl : team.getPlayers()) {
            ActionBar.sendActionBar(SkyWarsMessages.joinedTeam.replace("%p", team.getColorcode() + p.getName()), pl);
        }

        team.addPlayer(p);
        Variables.teamJoiner = JoinTeam.getTeamSwitcher();
        Scoreboards.lobbySB.getTeam("0" + team.getTeamname()).addEntry(p.getName());
        p.getInventory().setItem(4,
                ItemCreator.itemCreate(Material.WOOL, SkyWarsItemNames.teamWaehlerItem, team.getColordata()));
    }

    private static boolean joinIfInNoTeam(Player p, Team team) {

        if (team.full()) {
            p.sendMessage(team.getColorcode() + SkyWarsMessages.teamFull);
            return false;
        }

        if (Biomia.getTeamManager().isPlayerInAnyTeam(p)) {
            return true;
        }
        team.addPlayer(p);
        Scoreboards.lobbySB.getTeam("0" + team.getTeamname()).addEntry(p.getName());
        p.getInventory().setItem(4,
                ItemCreator.itemCreate(Material.WOOL, SkyWarsItemNames.teamWaehlerItem, team.getColordata()));
        return true;
    }

    public static void partyJoin(BiomiaPlayer bp) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Player> party = new ArrayList<>();

                for (PAFPlayer paf : bp.getParty().getAllPlayers()) {
                    Player target = Bukkit.getPlayer(paf.getUniqueId());
                    if (target != null) {
                        party.add(target);
                    }
                }
                if (party.size() > Variables.playerPerTeam) {
                    for (Player p : party) {
                        p.sendMessage(SkyWarsMessages.noFittingTeamParty);
                        cancel();
                    }
                } else {
                    for (Team t : Biomia.getTeamManager().getTeams()) {
                        if (t.getMaxPlayer() - t.getPlayersInTeam() <= party.size()) {
                            for (Player p : party) {
                                t.addPlayer(p);
                                Scoreboards.lobbySB.getTeam("0" + t.getTeamname()).addEntry(p.getName());
                                Variables.teamJoiner = JoinTeam.getTeamSwitcher();
                            }
                            cancel();
                            return;
                        }
                    }
                    for (Player p : party) {
                        p.sendMessage(SkyWarsMessages.noFittingTeamPlayer);
                    }
                }
            }
        }.runTaskLater(Main.getPlugin(), 20);
    }

    public static Inventory getTeamSwitcher() {

        Inventory inv = Bukkit.createInventory(null, 9, SkyWarsMessages.teamInventoryName);

        int i = 0;

        switch (Variables.teams) {
            case (2):
                for (Team team : Biomia.getTeamManager().getTeams()) {
                    ItemStack itemstack = ItemCreator.itemCreate(Material.WOOL,
                            team.getColorcode() + Biomia.getTeamManager().translate(team.getTeamname()), team.getColordata());

                    if (team.getPlayersInTeam() > 1) {
                        itemstack.setAmount(team.getPlayersInTeam());
                    }

                    if (i == 0)
                        i += 2;
                    else if (i == 2)
                        i += 3;

                    inv.setItem(i, itemstack);
                }
                break;
            case (4):
                for (Team team : Biomia.getTeamManager().getTeams()) {
                    ItemStack itemstack = ItemCreator.itemCreate(Material.WOOL,
                            team.getColorcode() + Biomia.getTeamManager().translate(team.getTeamname()), team.getColordata());

                    if (team.getPlayersInTeam() > 1) {
                        itemstack.setAmount(team.getPlayersInTeam());
                    }

                    i++;
                    inv.setItem(i, itemstack);
                    i++;
                }
                break;
            case (8):
                for (Team team : Biomia.getTeamManager().getTeams()) {
                    ItemStack itemstack = ItemCreator.itemCreate(Material.WOOL,
                            team.getColorcode() + Biomia.getTeamManager().translate(team.getTeamname()), team.getColordata());

                    if (team.getPlayersInTeam() > 1) {
                        itemstack.setAmount(team.getPlayersInTeam());
                    }

                    inv.setItem(i, itemstack);

                    i++;
                    if (i == 4)
                        i++;
                }
                break;
        }
        return inv;

    }
}
