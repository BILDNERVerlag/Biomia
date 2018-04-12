package de.biomia.spigot.minigames.general;

import de.biomia.spigot.messages.MinigamesItemNames;
import de.biomia.spigot.minigames.GameMode;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.tools.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TeamSwitcher {

    public static Inventory getTeamSwitcher(GameMode mode) {

        Inventory inv = mode.getTeamSwitcher();

        if (inv == null)
            inv = Bukkit.createInventory(null, 9, MinigamesItemNames.teamWaehlerItem);

        int i = 0;

        switch (mode.getTeams().size()) {
            case (2):
                for (GameTeam team : mode.getTeams()) {
                    ItemStack itemstack = ItemCreator.itemCreate(Material.WOOL, team.getColorcode() + team.getColor().translate(), team.getColordata());

                    if (team.getPlayers().size() > 1) {
                        itemstack.setAmount(team.getPlayers().size());
                    }

                    if (i == 0)
                        i += 2;
                    else if (i == 2)
                        i += 3;

                    inv.setItem(i, itemstack);
                }
                break;
            case (4):
                for (GameTeam team : mode.getTeams()) {
                    ItemStack itemstack = ItemCreator.itemCreate(Material.WOOL, team.getColorcode() + team.getColor().translate(), team.getColordata());

                    if (team.getPlayers().size() > 1)
                        itemstack.setAmount(team.getPlayers().size());

                    i++;
                    inv.setItem(i, itemstack);
                    i++;
                }
                break;
            case (8):
                for (GameTeam team : mode.getTeams()) {
                    ItemStack itemstack = ItemCreator.itemCreate(Material.WOOL, team.getColorcode() + team.getColor().translate(), team.getColordata());

                    if (team.getPlayers().size() > 1) {
                        itemstack.setAmount(team.getPlayers().size());
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