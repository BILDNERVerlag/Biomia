package de.biomia.spigot.specialEvents.winterEvent;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.specialEvents.winterEvent.Reward.rewardType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Calendar;
import java.util.HashMap;

class WinterEvent implements Listener {

    public static final HashMap<Integer, Reward> rewards = new HashMap<>();

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent e) {
        for (int day = 1; day < 25; day++) {
            if (Main.getPlugin().getConfig().getStringList("Calendar." + day)
                    .contains(e.getRightClicked().getUniqueId().toString())) {

                if (getDay() == 0) {
                    e.getPlayer().sendMessage("\u00A74Wir bauen gerade noch auf!");
                    return;
                } else if (getDay() == -1 || getDay() > 24) {
                    e.getPlayer().sendMessage("\u00A74Tja da hast du was verpasst!");
                    return;
                }

                if (getDay() == day) {
                    if (WinterTag.hasOpened(Biomia.getBiomiaPlayer(e.getPlayer()), day)) {
                        e.getPlayer().sendMessage("\u00A74Warst du heute nicht schonmal da?");
                    } else {
                        e.getPlayer().sendMessage("\u00A74Hoho! Schön dass du vorbei schaust!");
                        openCalendar(Biomia.getBiomiaPlayer(e.getPlayer()), day);
                    }
                } else if (getDay() < day)
                    e.getPlayer()
                            .sendMessage("\u00A74Heute hab ich leider nichts für dich... komm doch am " + day + ". wieder!");
                else
                    e.getPlayer().sendMessage(
                            "\u00A74Noch so einer der wieder zu spät kommt... ich war schon am " + day + ". dran!");
            }
        }
    }

    public WinterEvent() {
        for (int id = 1; id < 25; id++) {
            rewards.put(id, IDToReward(id));
        }
    }

    private Reward IDToReward(int id) {
        switch (id) {
            case 1:
                return new Reward(rewardType.Coins_Boost, (byte) 0, id);
            case 2:
                return new Reward(rewardType.Coins_Boost, (byte) 0, id);
            case 3:
                return new Reward(rewardType.Coins_Boost, (byte) 0, id);
            case 4:
                return new Reward(rewardType.SkyWars_Kit, (byte) 0, id);
            case 5:
                return new Reward(rewardType.SkyWars_Kit, (byte) 1, id);
            case 6:
                return new Reward(rewardType.Shop_Gutschein, (byte) 0, id);
            case 7:
                return new Reward(rewardType.Shop_Gutschein, (byte) 1, id);
            case 8:
                return new Reward(rewardType.Shop_Gutschein, (byte) 2, id);
            case 9:
                return new Reward(rewardType.QuestWelt_Items, (byte) 0, id);
            case 10:
                return new Reward(rewardType.QuestWelt_Items, (byte) 1, id);
            case 11:
                return new Reward(rewardType.QuestWelt_Items, (byte) 2, id);
            case 12:
                return new Reward(rewardType.QuestWelt_Items, (byte) 3, id);
            case 13:
                return new Reward(rewardType.QuestWelt_Items, (byte) 4, id);
            case 14:
                return new Reward(rewardType.QuestWelt_Items, (byte) 5, id);
            case 15:
                return new Reward(rewardType.QuestWelt_Items, (byte) 6, id);
            case 16:
                return new Reward(rewardType.Ingame_Coins, (byte) 0, id);
            case 17:
                return new Reward(rewardType.Ingame_Coins, (byte) 0, id);
            case 18:
                return new Reward(rewardType.Ingame_Coins, (byte) 0, id);
            case 19:
                return new Reward(rewardType.Ingame_Coins, (byte) 0, id);
            case 20:
                return new Reward(rewardType.Ingame_Coins, (byte) 0, id);
            case 21:
                return new Reward(rewardType.Ingame_Coins, (byte) 0, id);
            case 22:
                return new Reward(rewardType.Ingame_Coins, (byte) 0, id);
            case 23:
                return new Reward(rewardType.Ingame_Coins, (byte) 0, id);
            default:
                return null;
        }
    }

    //somehow intelliJ thinks you should compare months to week days
    @SuppressWarnings("MagicConstant")
    private static int getDay() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.MONTH) == Calendar.DECEMBER) {
            return cal.get(Calendar.DAY_OF_MONTH);
        } else {
            return -1;
        }
    }

    private void openCalendar(BiomiaPlayer biomiaPlayer, int day) {
        if (!WinterTag.hasOpened(biomiaPlayer, day)) {
            Reward rw = Reward.getRandomReward(biomiaPlayer);
            rw.give(biomiaPlayer);
            WinterTag.open(biomiaPlayer, day, rw.getRewardID());
        }
    }
}
