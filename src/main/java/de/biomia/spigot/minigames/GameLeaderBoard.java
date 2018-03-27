package de.biomia.spigot.minigames;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaServerType;
import de.biomia.spigot.OfflineBiomiaPlayer;
import de.biomia.spigot.achievements.Stats;
import de.biomia.spigot.messages.MinigamesMessages;
import de.biomia.spigot.tools.HeadCreator;
import de.biomia.universal.SkinValue;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.HashMap;

class GameLeaderBoard {

    private final ArrayList<Location> locations;
    private final BiomiaServerType type;
    private final Stats.BiomiaStat sortBy;

    public GameLeaderBoard(ArrayList<Location> locations, BiomiaServerType target, Stats.BiomiaStat sortBy) {
        //TODO tracken sowohl gemeinsam (als Versus) als auch separat
        this.locations = locations;
        this.type = target;
        this.sortBy = sortBy;
    }

    public void reloadLeaderBoardSigns() {

        HashMap<Integer, Integer> map = sortBy.getTop(locations.size(), null);

        for (int i = 1; i <= locations.size(); i++) {

            OfflineBiomiaPlayer bp = Biomia.getOfflineBiomiaPlayer(map.get(i));

            Location location = locations.get(i - 1);

            if (type.isMinigame()) {

                int wins = 0, kills = 0, deaths = 0, playedGames = 0;

                switch (type) {
                    case SkyWars:
                        deaths = 1;
                        break;
                    case BedWars:
                        deaths = 1;
                        break;
                }


                double kd = deaths == 0 ? 0 : (int) ((double) (kills / deaths) * 100) / 100;

                Sign sign = (Sign) location.getBlock().getState();

                sign.setLine(0, String.format(MinigamesMessages.rank, bp.getName(), i + ""));
                sign.setLine(1, MinigamesMessages.winns + wins);
                sign.setLine(2, String.format(MinigamesMessages.kd, kd + ""));
                sign.setLine(3, MinigamesMessages.played + playedGames);

                Block b = location.getBlock().getLocation().add(0, 1, 0).getBlock();
                HeadCreator.setSkullUrl(SkinValue.getSkin(bp), b);

            }
        }
    }
}